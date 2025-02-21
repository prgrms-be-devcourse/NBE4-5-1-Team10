package nbe341team10.coffeeproject.domain.user.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.entity.Role;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 일반 회원가입
    public Users join(UserJoinRequest userJoinRequest) {
        String username = userJoinRequest.username();
        String email = userJoinRequest.email();
        String password = userJoinRequest.password();
        String address = userJoinRequest.address();

        Users user = Users.builder()
                .username(username)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .address(address)
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return user;
    }

    public boolean checkName(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public String getAccessToken(Users user) {
        return "";
    }
}
