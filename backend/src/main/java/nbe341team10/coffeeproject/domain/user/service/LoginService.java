package nbe341team10.coffeeproject.domain.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.jwt.JWTUtil;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.entity.Refresh;
import nbe341team10.coffeeproject.domain.user.entity.Role;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.RefreshRepository;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
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

    // 쿠키에서 토큰 추출
    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 쿠키 생성
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    // DB 저장
    public void addRefreshEntity(String email, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshToken = new Refresh();
        refreshToken.setEmail(email);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());
        refreshRepository.save(refreshToken);
    }

    // DB 삭제
    public void deleteRefresh(String refreshToken) {
        refreshRepository.deleteByRefresh(refreshToken);
    }

    // 있는지
    public boolean existRefresh(String refreshToken) {
        return refreshRepository.existsByRefresh(refreshToken);
    }

    // 생성
    public Map<String,String> createJwt(String refreshToken) {
        Long userId = jwtUtil.getId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 토큰 유지 시간
        String newAccess=jwtUtil.createJwt(userId, username, "access",email, role, 60 * 60 * 1000L);   // 1시간
        String newRefresh=jwtUtil.createJwt(userId, username, "refresh",email, role, 7 * 24 * 60 * 60 * 1000L);    // 1주일

        Map<String, String> newToken = new LinkedHashMap<>();
        newToken.put("access", newAccess);
        newToken.put("refresh", newRefresh);

        return newToken;
    }

    // 이메일 반환
    public String getEmail(String refreshToken) {
        return jwtUtil.getEmail(refreshToken);
    }

    public long count() {
        return userRepository.count();
    }


}
