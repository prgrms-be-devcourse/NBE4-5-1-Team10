package nbe341team10.coffeeproject.domain.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserResponse;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class LoginController {

    private final UserRepository userRepository;
    private final LoginService loginService;


    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserResponse> Join(@Valid @RequestBody UserJoinRequest dto) {
        // 중복 검사
        if(userRepository.existsByUsername(dto.username())){
            throw new RuntimeException("Username is already in use");
        } else if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email is already in use");
        }

        Users user = loginService.join(dto);
        UserResponse response=new UserResponse(user);
        return ResponseEntity.ok(response);
    }


}


