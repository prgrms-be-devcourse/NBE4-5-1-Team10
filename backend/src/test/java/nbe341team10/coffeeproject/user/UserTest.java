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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class UserTest {

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

}
