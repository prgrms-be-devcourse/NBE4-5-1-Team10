package nbe341team10.coffeeproject.domain.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nbe341team10.coffeeproject.domain.user.dto.CustomUserDetails;
import nbe341team10.coffeeproject.domain.user.entity.Role;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 다음 필터로
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.substring(7);

        try{
            String category=jwtUtil.getCategory(token);
            // 토큰 만료검증
            jwtUtil.isExpired(token);

            if(category.equals("access")){
                // 액세스 토큰 만료 검증
                jwtUtil.isExpired(token);
            }else if(category.equals("refresh")){
                if(request.getRequestURI().equals("api/**/user/reissue")){  // 아직 안만듬
                    // 재발급 처리
                    filterChain.doFilter(request, response);
                    return;
                }else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    RsData<String> errorResponse=new RsData<>("401","unauthorized","refresh token cannot be used for access");
                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return;
                }
            }

        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            RsData<String> errorResponse= new RsData<>("401","unauthorized","Token is expired");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        //토큰에서 username과 role 획득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set
        Users user = Users.builder()
                .email(email)
                .role(Role.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로
        filterChain.doFilter(request, response);
    }

}
