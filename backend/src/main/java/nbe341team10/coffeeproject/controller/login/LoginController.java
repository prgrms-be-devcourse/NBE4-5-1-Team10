package nbe341team10.coffeeproject.controller.login;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.DTO.login.JoinDTO;
import nbe341team10.coffeeproject.DTO.login.UserResponse;
import nbe341team10.coffeeproject.domain.user.UserEntity;
import nbe341team10.coffeeproject.repository.UserRepository;
import nbe341team10.coffeeproject.service.login.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class LoginController {

    private final UserRepository userRepository;
    private final LoginService loginService;


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserResponse> Join(@Valid @RequestBody JoinDTO dto) {
        // 중복 검사
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("Username is already in use");
        }
        UserEntity user = loginService.join(dto);
        UserResponse response=new UserResponse(user);
        return ResponseEntity.ok(response);
    }


}


