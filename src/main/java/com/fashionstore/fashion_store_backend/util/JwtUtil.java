package com.fashionstore.fashion_store_backend.util;

import com.fashionstore.fashion_store_backend.exception.TokenInvalidException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my_secret_key";
    private final long EXPIRATION_TIME_ONE_HOUR = 1000L * 60 * 60; // 1 giờ
    private final long EXPIRATION_TIME_THIRTY_DAYS = 1000L * 60 * 60 * 24 * 30; // 30 ngày

    public String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateToken(String username, List<String> roles, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        long expirationTime = rememberMe ? EXPIRATION_TIME_THIRTY_DAYS : EXPIRATION_TIME_ONE_HOUR;
        return createToken(claims, username, expirationTime);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new TokenInvalidException("Token không hợp lệ: " + e.getMessage());
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        if (!username.equals(extractUsername(token))) {
            throw new TokenInvalidException("Token không hợp lệ. Tên người dùng không khớp.");
        }
        if (isTokenExpired(token)) {
            throw new TokenInvalidException("Token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return true;
    }
}
