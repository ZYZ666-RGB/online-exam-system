package com.example.exam.utils;

import com.example.exam.common.BusinessException;
import com.example.exam.common.LoginUser;
import com.example.exam.config.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final AuthProperties authProperties;

    public TokenInfo createToken(LoginUser loginUser) {
        String jti = UUID.randomUUID().toString().replace("-", "");
        long expireMillis = authProperties.getTokenExpireMinutes() * 60L * 1000L;
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + expireMillis);
        String roles = String.join(",", loginUser.getRoles());

        String token = Jwts.builder()
                .setId(jti)
                .setSubject(String.valueOf(loginUser.getUserId()))
                .claim("username", loginUser.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(secretKey(), SignatureAlgorithm.HS256)
                .compact();
        return new TokenInfo(token, jti, expireMillis / 1000L);
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(401, "Token无效或已过期");
        }
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(authProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Data
    @AllArgsConstructor
    public static class TokenInfo {
        private String token;
        private String jti;
        private Long expireSeconds;
    }
}

