package nbe341team10.coffeeproject.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true) // 쿠키를 포함할 수 있도록 허용
                .allowedHeaders("*") // 모든 요청 헤더 허용
                .exposedHeaders("Authorization", "Refresh"); // 클라이언트에서 사용할 수 있는 응답 헤더
    }


}