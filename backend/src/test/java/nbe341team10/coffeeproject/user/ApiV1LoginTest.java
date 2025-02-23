package nbe341team10.coffeeproject.user;

import com.jayway.jsonpath.JsonPath;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



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
        UserJoinRequest userJoinRequest = new UserJoinRequest("user",
                "user@naver.com",
                "1234",
                "Test Address");

        // 사용자 가입
        loginService.join(userJoinRequest);

        // 로그인 요청할 JSON 데이터
        String requestBody = "{ \"email\": \"user@naver.com\", \"password\": \"1234\" }";

        Optional<Users> user = userRepository.findByEmail("user@naver.com");
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
                .andExpect(jsonPath("$.data").value("user@naver.com"));
    }

}
