package nbe341team10.coffeeproject.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Claims getPayload(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public Long getId(String token) {
        Claims payload = getPayload(token);
        return payload.get("id", Long.class);
    }

    // 이메일 반환
    public String getEmail(String token) {
        Claims payload = getPayload(token);
        return payload.get("email", String.class);
    }

    // role 반환
    public String getRole(String token) {
        Claims payload = getPayload(token);
        return payload.get("role", String.class);
    }

    // category: access/refresh
    public String getCategory(String token) {
        Claims payload = getPayload(token);
        return payload.get("category", String.class);
    }

    // 만료검증
    public Boolean isExpired(String token) {
        Claims payload = getPayload(token);
        return payload.getExpiration().before(new Date());
    }
    // jwt 생성
    public String createJwt(Long userId, String category,String email, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("id", userId)
                .claim("category", category)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))     // 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs))       // 소멸 시간
                .signWith(secretKey)    // H256기반
                .compact();
    }

}
