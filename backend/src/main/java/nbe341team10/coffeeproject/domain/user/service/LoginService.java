package nbe341team10.coffeeproject.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.jwt.JWTUtil;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.entity.Refresh;
import nbe341team10.coffeeproject.domain.user.entity.Role;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.RefreshRepository;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

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

    public long count() {
        return userRepository.count();
    }


}
