package nbe341team10.coffeeproject.user;

import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
                "password123",
                "Test Address");

        Users user=loginService.join(userJoinRequest);

        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("Test Address", user.getAddress());
    }

}
