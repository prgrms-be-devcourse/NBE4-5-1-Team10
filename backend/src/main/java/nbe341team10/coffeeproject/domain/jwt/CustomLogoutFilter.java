package nbe341team10.coffeeproject.domain.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nbe341team10.coffeeproject.domain.user.repository.RefreshRepository;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // uri가 로그아웃 경로인지 검증
        if(!checkUri(request)){
            filterChain.doFilter(request, response);
            return;
        }

        // refresh 토큰
        String refresh=extractRefresh(request);

        //refresh 토큰을 얻지 못한경우(null)
        if (refresh == null) {
            RsData<String> error=new RsData<>("401","refresh token is null");
            JsonResponseUnauthorized(response, error);
            return;
        }
        
        // 유효성 검증
        RsData<String> data=validateRefresh(refresh);
        if(data!=null){
            JsonResponseUnauthorized(response, data);
            return;
        }

        //로그아웃 진행
        Customlogout(refresh,response);
    }

    private void Customlogout(String refresh,HttpServletResponse response) throws IOException {
        // db 삭제
        refreshRepository.deleteByRefresh(refresh);

        // 쿠키 초기화
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private RsData<String> validateRefresh(String refresh) {
        try{
            // 만료 검증
            jwtUtil.isExpired(refresh);

            // refresh 검증
            String category = jwtUtil.getCategory(refresh);
            if(!category.equals("refresh")){
                return new RsData<>("401","refresh token is Needed");
            }

            // DB 검증
            if(!refreshRepository.existsByRefresh(refresh)){
                return new RsData<>("401","refresh does not exist");
            }

            // 다 통과하면 null
            return null;

        }catch (ExpiredJwtException e){
            return new RsData<>("401","refresh token is Expired");
        }
    }

    private String extractRefresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean checkUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("/api/v1/user/logout")) {
            return false;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            return false;
        }
        return true;
    }


    private void JsonResponseUnauthorized(HttpServletResponse response,RsData<String> error) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
