package com.loopin.loopinbackend.global.security.jwt.provider;

import com.loopin.loopinbackend.domain.auth.exception.EmptyTokenException;
import com.loopin.loopinbackend.domain.auth.exception.ExpiredCustomJwtException;
import com.loopin.loopinbackend.domain.auth.exception.InvalidJwtException;
import com.loopin.loopinbackend.domain.auth.model.CustomUserDetails;
import com.loopin.loopinbackend.domain.user.enums.Role;
import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.key}")
    private String secretKey; // 인스턴스 필드에 주입

    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60 * 24;      // 24시간
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 일주일

    /**
     * AccessToken 생성
     */
    public String generateAccessToken(Authentication auth) {
        return generateToken(auth, ACCESS_TOKEN_VALIDITY);
    }

    /**
     * RefreshToken 생성
     */
    public String generateRefreshToken(Authentication auth) {
        return generateToken(auth, REFRESH_TOKEN_VALIDITY);
    }

    /**
     * RefreshToken으로 AccessToken 재발급
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) return null;

        Long userId = extractUserId(refreshToken);
        String username = extractUsername(refreshToken);
        String role = extractRole(refreshToken);

        return generateToken(username, userId, Role.valueOf(role), ACCESS_TOKEN_VALIDITY);
    }

    /**
     * 토큰 생성 (인증 객체 버전)
     */
    private String generateToken(Authentication auth, long validity) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return generateToken(userDetails.getUsername(), userDetails.getUserId(), userDetails.user().getRole(), validity);
    }

    /**
     * 토큰 생성 (직접 userId/username 전달 버전)
     */
    private String generateToken(String username, Long userId, Role role, long validity) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("role", role.name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSignKey())
                .compact();
    }

    /**
     * 클레임 추출
     */
    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims(); // 만료된 토큰이라도 claims는 추출 가능
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public long extractExpiration(String token) {
        return extractClaims(token).getExpiration().getTime();
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }


    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) throw new EmptyTokenException();

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new ExpiredCustomJwtException();
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            System.out.println("asddasddasasd");
            throw new InvalidJwtException();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.JWT_VALIDATION_ERROR);
        }
    }

    /**
     * 서명 키 생성
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
