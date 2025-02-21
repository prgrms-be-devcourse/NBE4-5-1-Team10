package nbe341team10.coffeeproject.domain.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.global.dto.RsData;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Provider;
import java.util.*;

@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String,String> loginData=new ObjectMapper().readValue(request.getInputStream(), Map.class);

            // 이메일,비밀번호 추출
            String email=loginData.get("email");
            String password=loginData.get("password");
            System.out.println("사용자: "+email);

            Users user = userRepository.findByEmail(email);
            if (user == null) {
                // 존재하지 않는 사용자
                throw new UsernameNotFoundException("");
            }

            UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(email,password);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 실패:Invalid Input format",e);
        }
    }

    //로그인 성공시 실행하는 메소드 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String code;
        String message;

        // 실패한 인증의 원인에 따라 메시지 결정
        if (failed instanceof UsernameNotFoundException) {
            code = "401-1";
            message = "Invalid email"; // 이메일이 없는 경우
        } else if (failed instanceof BadCredentialsException) {
            code = "401-2";
            message = "Invalid password"; // 비밀번호가 잘못된 경우
        } else {
            code = "401-3";
            message = "Authentication failed."; // 일반적인 오류 메시지
        }

        // JSON 형태로 응답 작성
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(new RsData<>(code, message)));
//        try{
//            response.setContentType("application/json; charset=UTF-8");
//            response.getWriter().write("{\"error\": \"다시 로그인하세요.\"}");
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }


}
