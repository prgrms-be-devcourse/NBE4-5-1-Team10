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
    public ResponseEntity<RsData<UserResponse>> Join(@Valid @RequestBody UserJoinRequest dto) {
        // 중복 검사
        if(userRepository.existsByUsername(dto.username())){
            throw new ServiceException("400-1","Username already exists");
        } else if (userRepository.existsByEmail(dto.email())) {
            throw new ServiceException("400-1","Email is already in use");
        }

        Users user = loginService.join(dto);
        UserResponse response=new UserResponse(user);

        RsData<UserResponse> rsData=new RsData<>(
                "200",
                "success",
                response
        );

        return ResponseEntity.ok(rsData);
    }

}


