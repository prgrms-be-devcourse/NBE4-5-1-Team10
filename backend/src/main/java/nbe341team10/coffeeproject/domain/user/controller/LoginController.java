package nbe341team10.coffeeproject.domain.user.controller;


import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.jwt.JWTUtil;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.dto.UserResponse;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import nbe341team10.coffeeproject.global.dto.RsData;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class LoginController {

    private final LoginService loginService;
    private final JWTUtil jwtUtil;

    // 회원가입
    @Operation(summary = "회원가입 하기")
    @PostMapping("/join")
    public ResponseEntity<RsData<UserResponse>> Join(@Valid @RequestBody UserJoinRequest dto) {
        // 중복 검사
        if(loginService.checkName(dto.username())){
            throw new ServiceException("400-1","Username already exists");
        } else if (loginService.checkEmail(dto.email())) {
            throw new ServiceException("400-2","Email is already in use");
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
    @Operation(summary = "refresh 토큰으로 access 토큰 재발급 하기")
    @PostMapping("/reissue")
    public RsData<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken=loginService.extractRefreshToken(request);

        // null 체크
        if(refreshToken==null){
            RsData<String> error=new RsData<>("400","Refresh token is Null");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            return error;
        }

        // 만료 체크
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            RsData<String> error=new RsData<>("400","Refresh token expired");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            return error;
        }

        // 토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            RsData<String> error=new RsData<>("400","Refresh token is required");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            return error;
        }

        // DB에 저장되어 있는지 확인
        boolean isExist = loginService.existRefresh(refreshToken);
        if (!isExist) {  // 없으면
            RsData<String> error=new RsData<>("400","Unsaved refresh token");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            return error;

        }

        // 새로운 토큰 생성
        Map<String,String> token=loginService.createJwt(refreshToken);
        String newRefresh=token.get("refresh");
        String email= loginService.getEmail(newRefresh);

        // 기존 refresh 삭제 후 새 refresh 생성 후 엔티티에 저장
        loginService.deleteRefresh(refreshToken);
        loginService.addRefreshEntity(email, newRefresh, 86400000L);

        // 쿠키에 리프레시 저장
        response.addCookie(loginService.createCookie("refresh", token.get("refresh")));

        RsData<Map<String,String>> success=new RsData<>("200","success",token);
//        return ResponseEntity.ok(success);
        return success;
    }


}


