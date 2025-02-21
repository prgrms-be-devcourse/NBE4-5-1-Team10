package nbe341team10.coffeeproject.domain.user.controller;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.jwt.JWTUtil;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserResponse;
import nbe341team10.coffeeproject.domain.user.entity.Refresh;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.RefreshRepository;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import nbe341team10.coffeeproject.global.dto.RsData;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class LoginController {

    private final LoginService loginService;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<RsData<UserResponse>> Join(@Valid @RequestBody UserJoinRequest dto) {
        // 중복 검사
        if(loginService.checkName(dto.username())){
            throw new ServiceException("400-1","Username already exists");
        } else if (loginService.checkEmail(dto.email())) {
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

    // 토큰 재발급
    // refresh 토큰으로 access 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken=null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refresh")){
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 만료 체크
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if (!isExist) {  // 없으면
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새로운 JWT를 생성
        // 액세스 토큰 생성
        String newAccess = jwtUtil.createJwt("access",username, role, 30 * 60 * 1000L); // 30분
        // 리프레시 토큰 생성
        String newRefresh = jwtUtil.createJwt("refresh",username, role, 7 * 24 * 60 * 60 * 1000L); // 1주일


        // 응답
        Map<String, String> responseBody = new LinkedHashMap<>();
        responseBody.put("new_Access", newAccess);
        responseBody.put("new_Refresh", newRefresh);
        // 쿠키에 리프레시 저장
        response.addCookie(createCookie("refresh", newRefresh));

        // 기존 refresh 삭제 후 새 refresh 생성 후 엔티티에 저장
        refreshRepository.deleteByRefresh(refreshToken);
        addRefreshEntity(username, newRefresh, 86400000L);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // 쿠키 생성
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshToken = new Refresh();
        refreshToken.setEmail(email);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());
        refreshRepository.save(refreshToken);
    }


}


