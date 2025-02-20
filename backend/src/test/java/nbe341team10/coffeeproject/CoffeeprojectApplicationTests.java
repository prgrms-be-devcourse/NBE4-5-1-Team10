package nbe341team10.coffeeproject;

import nbe341team10.coffeeproject.domain.user.dto.JoinDTO;
import nbe341team10.coffeeproject.domain.user.entity.UserEntity;
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
import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class CoffeeprojectApplicationTests {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private LoginService loginService;
    @Autowired
    private UserRepository userRepository;

	@Test
	@DisplayName("가입 성공 확인")
	void join() throws Exception {
		JoinDTO joinDTO = new JoinDTO();
		joinDTO.setUsername("testuser");
		joinDTO.setEmail("testuser@example.com");
		joinDTO.setPassword("password123");
		joinDTO.setAddress("Test Address");

		UserEntity user=loginService.join(joinDTO);

		assertEquals("testuser", user.getUsername());
		assertEquals("testuser@example.com", user.getEmail());
		assertEquals("Test Address", user.getAddress());
	}

}
