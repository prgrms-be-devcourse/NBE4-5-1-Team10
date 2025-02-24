package nbe341team10.coffeeproject.user;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserResponse;
import nbe341team10.coffeeproject.domain.user.entity.Blacklist;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.BlacklistRepository;
import nbe341team10.coffeeproject.domain.user.repository.RefreshRepository;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ApiV1LoginTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserRepository userRepository;

    private String accessToken;
    private String refreshToken;
    @Autowired
    private RefreshRepository refreshRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;

//    @Test
//    @DisplayName("DB연결확인")
//    public void testDatabaseConnection() {
//        // 데이터베이스에서 사용자 조회
//        Optional<Users> user = userRepository.findByEmail("user@naver.com");
//
//        // 확인할 조건
//        // 사용자가 null이 아님을 확인
//        assertNotNull(user, "User repository should not return null.");
//
//        // 만약 user가 존재해야 한다면:
//        if (user.isPresent()) {
//            // 추가적인 검증 등을 하면 좋습니다.
//            UserResponse userResponse = new UserResponse(user.get());
//            System.out.println("User found: " + userResponse);
//        } else {
//            System.out.println("No user found with the given email.");
//        }
//    }

    @Test
    @DisplayName("가입 성공 확인")
    void join() throws Exception {
        UserJoinRequest userJoinRequest = new UserJoinRequest("testuser",
                "testuser@example.com",
                "1234",
                "Test Address");

        Users user=loginService.join(userJoinRequest);

        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("Test Address", user.getAddress());
    }

    @Test
    @DisplayName("로그인 성공 확인")
    public void testLoginSuccess() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("testuser",
                "testuser@example.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 로그인 요청할 JSON 데이터
        String requestBody = "{ \"email\": \"testuser@example.com\", \"password\": \"1234\" }";

        // 로그인 수행
        mvc.perform(post("/api/v1/user/login") // 로그인 URL 경로
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()) // HTTP 200 응답 검증
                .andExpect(jsonPath("$.code").value("200")) // code 값이 200인지 검증
                .andExpect(jsonPath("$.msg").value("login-success")) // msg 값이 "login-success"인지 검증
                .andExpect(jsonPath("$.data.access").isNotEmpty()) // access 토큰이 비어있지 않은지 검증
                .andExpect(jsonPath("$.data.refresh").isNotEmpty()); // refresh 토큰이 비어있지 않은지 검증
    }

    @Test
    @DisplayName("토큰 인증 검증")
    public void testLoginSuccess2() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("testUser",
                "testUser@naver.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 로그인 요청할 JSON 데이터
        String requestBody = "{ \"email\": \"testUser@naver.com\", \"password\": \"1234\" }";

        Optional<Users> user = userRepository.findByEmail("testUser@naver.com");
        assertTrue(user.isPresent(), "User should exist in the database");

        // 로그인 수행
        String loginResponse = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // JSON 응답에서 액세스 토큰 추출
        String accessToken = JsonPath.read(loginResponse, "$.data.access");

        // 액세스 토큰 확인
        assertNotNull(accessToken); // 액세스 토큰이 null이 아님을 확인
        assertFalse(accessToken.isEmpty()); // 액세스 토큰이 비어있지 않음을 확인

        // Bearer 토큰으로 /user 엔드포인트에 접근
        mvc.perform(get("/user")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("welcome"))
                .andExpect(jsonPath("$.data").value("testUser@naver.com"));
    }

    @Test
    @DisplayName("Json 응답에서 추출한 리프레시 토큰으로 이메일을 가져와 해당 유저 정보를 확인")
    public void testGetUserFromRefreshToken() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("testUser",
                "testUser@example.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 리프레시 토큰을 새로 생성하기 위한 로그인 요청할 JSON 데이터
        String loginRequestBody = "{ \"email\": \"testUser@example.com\", \"password\": \"1234\" }";

        // 로그인 수행
        String loginResponse = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // JSON 응답에서 리프레시 토큰 추출
        String refreshToken = JsonPath.read(loginResponse, "$.data.refresh");

        // 리프레시 토큰을 사용하여 이메일을 가져오고 해당 유저를 탐색
        String email = loginService.getEmail(refreshToken);
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            // 사용자가 존재하는 경우
            assertEquals("testUser@example.com", user.get().getEmail());
            assertEquals("testUser", user.get().getUsername());
            UserResponse userResponse = new UserResponse(user.get());
            System.out.println("사용자: "+ userResponse);
        } else {
            // 사용자가 존재하지 않는 경우
            fail("User should exist in the database but was not found");
        }
    }

    @Test
    @DisplayName("쿠키에 저장된 리프레시 토큰을 사용하여 유저 정보를 가져오는 검증")
    public void testGetUserInfoFromRefreshTokenCookie() throws Exception {
        // 로그인 요청할 JSON 데이터 (이미 존재하는 사용자)
        String requestEmail= "user@naver.com";
        String requestPassword = "1234";
        String loginRequestBody = "{ \"email\": \"" + requestEmail + "\", \"password\": \"" + requestPassword + "\" }";

        // 로그인 수행 및 쿠키 검증
        MvcResult mvcResult = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh"))  // 쿠키 생성 여부 검증
                .andReturn(); // MvcResult를 받아옴

        // 응답에서 쿠키 추출
        MockHttpServletResponse response = mvcResult.getResponse();
        String refreshToken = null;

        // 응답 쿠키에서 리프레시 토큰 찾기
        Cookie[] cookies = response.getCookies();
        assertNotNull(cookies, "Expected cookies to be present in response.");
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        // 리프레시 토큰이 null인지 확인
        assertNotNull(refreshToken, "Refresh token should not be null after login.");

        // 쿠키에서 리프레시 토큰으로 이메일을 가져오고 해당 유저를 탐색
        String email = loginService.getEmail(refreshToken);
        Optional<Users> user = userRepository.findByEmail(email);

        // 유저 존재 여부 검증
        assertTrue(user.isPresent(), "User should exist in the database");

        if (user.isPresent()) {
            // 사용자가 존재하는 경우
            Users user1 = user.get();
            assertEquals("user@naver.com", user1.getEmail());
            UserResponse userResponse = new UserResponse(user1);
            System.out.println(userResponse);
        }else{
            fail("User should exist in the database but was not found.");
        }
    }

    @Test
    @DisplayName("로그아웃")
    public void testLogout() throws Exception {

        package nbe341team10.coffeeproject.user;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserResponse;
import nbe341team10.coffeeproject.domain.user.entity.Blacklist;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.BlacklistRepository;
import nbe341team10.coffeeproject.domain.user.repository.RefreshRepository;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ApiV1LoginTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserRepository userRepository;

    private String accessToken;
    private String refreshToken;
    @Autowired
    private RefreshRepository refreshRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;

//    @Test
//    @DisplayName("DB연결확인")
//    public void testDatabaseConnection() {
//        // 데이터베이스에서 사용자 조회
//        Optional<Users> user = userRepository.findByEmail("user@naver.com");
//
//        // 확인할 조건
//        // 사용자가 null이 아님을 확인
//        assertNotNull(user, "User repository should not return null.");
//
//        // 만약 user가 존재해야 한다면:
//        if (user.isPresent()) {
//            // 추가적인 검증 등을 하면 좋습니다.
//            UserResponse userResponse = new UserResponse(user.get());
//            System.out.println("User found: " + userResponse);
//        } else {
//            System.out.println("No user found with the given email.");
//        }
//    }

    @Test
    @DisplayName("가입 성공 확인")
    void join() throws Exception {
        UserJoinRequest userJoinRequest = new UserJoinRequest("testuser",
                "testuser@example.com",
                "1234",
                "Test Address");

        Users user=loginService.join(userJoinRequest);

        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("Test Address", user.getAddress());
    }

    @Test
    @DisplayName("로그인 성공 확인")
    public void testLoginSuccess() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("testuser",
                "testuser@example.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 로그인 요청할 JSON 데이터
        String requestBody = "{ \"email\": \"testuser@example.com\", \"password\": \"1234\" }";

        // 로그인 수행
        mvc.perform(post("/api/v1/user/login") // 로그인 URL 경로
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk()) // HTTP 200 응답 검증
                .andExpect(jsonPath("$.code").value("200")) // code 값이 200인지 검증
                .andExpect(jsonPath("$.msg").value("login-success")) // msg 값이 "login-success"인지 검증
                .andExpect(jsonPath("$.data.access").isNotEmpty()) // access 토큰이 비어있지 않은지 검증
                .andExpect(jsonPath("$.data.refresh").isNotEmpty()); // refresh 토큰이 비어있지 않은지 검증
    }

    @Test
    @DisplayName("토큰 인증 검증")
    public void testLoginSuccess2() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("testUser",
                "testUser@naver.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 로그인 요청할 JSON 데이터
        String requestBody = "{ \"email\": \"testUser@naver.com\", \"password\": \"1234\" }";

        Optional<Users> user = userRepository.findByEmail("testUser@naver.com");
        assertTrue(user.isPresent(), "User should exist in the database");

        // 로그인 수행
        String loginResponse = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // JSON 응답에서 액세스 토큰 추출
        String accessToken = JsonPath.read(loginResponse, "$.data.access");

        // 액세스 토큰 확인
        assertNotNull(accessToken); // 액세스 토큰이 null이 아님을 확인
        assertFalse(accessToken.isEmpty()); // 액세스 토큰이 비어있지 않음을 확인

        // Bearer 토큰으로 /user 엔드포인트에 접근
        mvc.perform(get("/user")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("welcome"))
                .andExpect(jsonPath("$.data").value("testUser@naver.com"));
    }

    @Test
    @DisplayName("Json 응답에서 추출한 리프레시 토큰으로 이메일을 가져와 해당 유저 정보를 확인")
    public void testGetUserFromRefreshToken() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("testUser",
                "testUser@example.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 리프레시 토큰을 새로 생성하기 위한 로그인 요청할 JSON 데이터
        String loginRequestBody = "{ \"email\": \"testUser@example.com\", \"password\": \"1234\" }";

        // 로그인 수행
        String loginResponse = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // JSON 응답에서 리프레시 토큰 추출
        String refreshToken = JsonPath.read(loginResponse, "$.data.refresh");

        // 리프레시 토큰을 사용하여 이메일을 가져오고 해당 유저를 탐색
        String email = loginService.getEmail(refreshToken);
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            // 사용자가 존재하는 경우
            assertEquals("testUser@example.com", user.get().getEmail());
            assertEquals("testUser", user.get().getUsername());
            UserResponse userResponse = new UserResponse(user.get());
            System.out.println("사용자: "+ userResponse);
        } else {
            // 사용자가 존재하지 않는 경우
            fail("User should exist in the database but was not found");
        }
    }

    @Test
    @DisplayName("쿠키에 저장된 리프레시 토큰을 사용하여 유저 정보를 가져오는 검증")
    public void testGetUserInfoFromRefreshTokenCookie() throws Exception {

        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("user",
                "user@naver.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 로그인 요청할 JSON 데이터 (이미 존재하는 사용자)
        String requestEmail= "user@naver.com";
        String requestPassword = "1234";
        String loginRequestBody = "{ \"email\": \"" + requestEmail + "\", \"password\": \"" + requestPassword + "\" }";

        // 로그인 수행 및 쿠키 검증
        MvcResult mvcResult = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh"))  // 쿠키 생성 여부 검증
                .andReturn(); // MvcResult를 받아옴

        // 응답에서 쿠키 추출
        MockHttpServletResponse response = mvcResult.getResponse();
        String refreshToken = null;

        // 응답 쿠키에서 리프레시 토큰 찾기
        Cookie[] cookies = response.getCookies();
        assertNotNull(cookies, "Expected cookies to be present in response.");
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        // 리프레시 토큰이 null인지 확인
        assertNotNull(refreshToken, "Refresh token should not be null after login.");

        // 쿠키에서 리프레시 토큰으로 이메일을 가져오고 해당 유저를 탐색
        String email = loginService.getEmail(refreshToken);
        Optional<Users> user = userRepository.findByEmail(email);

        // 유저 존재 여부 검증
        assertTrue(user.isPresent(), "User should exist in the database");

        if (user.isPresent()) {
            // 사용자가 존재하는 경우
            Users user1 = user.get();
            assertEquals("user@naver.com", user1.getEmail());
            UserResponse userResponse = new UserResponse(user1);
            System.out.println(userResponse);
        }else{
            fail("User should exist in the database but was not found.");
        }
    }

    @Test
    @DisplayName("로그아웃")
    public void testLogout() throws Exception {
        // 회원가입하는 사용자 데이터 설정
        UserJoinRequest userJoinRequest = new UserJoinRequest("user",
                "user@naver.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 2. 로그인
        String requestEmail = "user@naver.com";
        String requestPassword = "1234";
        String loginRequestBody = "{ \"email\": \"" + requestEmail + "\", \"password\": \"" + requestPassword + "\" }";

        MvcResult loginResult = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        // 3. 토큰 추출
        String loginResponse = loginResult.getResponse().getContentAsString();
        String accessToken = JsonPath.read(loginResponse, "$.data.access");
        String refreshToken = JsonPath.read(loginResponse, "$.data.refresh");

        // 4. 로그인 성공 검증
        assertNotNull(accessToken, "액세스 토큰이 null이 아니어야 합니다.");
        assertNotNull(refreshToken, "리프레시 토큰이 null이 아니어야 합니다.");

        // 5. 로그아웃 시도
        mvc.perform(post("/api/v1/user/logout")
                        .header("Authorization", "Bearer " + accessToken)
                        .cookie(new Cookie("refresh", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}

        // 2. 로그인
        String requestEmail = "user@naver.com";
        String requestPassword = "1234";
        String loginRequestBody = "{ \"email\": \"" + requestEmail + "\", \"password\": \"" + requestPassword + "\" }";

        MvcResult loginResult = mvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        // 3. 토큰 추출
        String loginResponse = loginResult.getResponse().getContentAsString();
        String accessToken = JsonPath.read(loginResponse, "$.data.access");
        String refreshToken = JsonPath.read(loginResponse, "$.data.refresh");

        // 4. 로그인 성공 검증
        assertNotNull(accessToken, "액세스 토큰이 null이 아니어야 합니다.");
        assertNotNull(refreshToken, "리프레시 토큰이 null이 아니어야 합니다.");

        // 5. 로그아웃 시도
        mvc.perform(post("/api/v1/user/logout")
                        .header("Authorization", "Bearer " + accessToken)
                        .cookie(new Cookie("refresh", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // // 6. 리프레시 토큰 DB에서 삭제 확인
        // boolean isRefreshDeleted = !refreshRepository.existsByRefresh(refreshToken);
        // assertTrue(isRefreshDeleted, "리프레시 토큰이 DB에서 삭제되어야 합니다.");

        // // 7. 액세스 토큰 블랙리스트 등록 확인
        // boolean isTokenAlreadyBlacklisted = blacklistRepository.existsByToken(accessToken);
        // if (!isTokenAlreadyBlacklisted) {
        //     Blacklist entry = new Blacklist();
        //     entry.setToken(accessToken);
        //     blacklistRepository.save(entry);
        // }

        // // 8. 최초의 로그인 시 받은 액세스 토큰으로 /user로 접근 시도
        // mvc.perform(get("/user")
        //                 .header("Authorization", "Bearer " + accessToken)
        //                 .contentType(MediaType.APPLICATION_JSON))
        //         .andExpect(status().isUnauthorized())
        //         .andExpect(jsonPath("$.code").value("401"))
        //         .andExpect(jsonPath("$.msg").value("unauthorized"))
        //         .andExpect(jsonPath("$.data").value("this token is blacklisted"));
    }

}
