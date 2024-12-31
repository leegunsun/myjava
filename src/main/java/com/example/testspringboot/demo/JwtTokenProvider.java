package com.example.testspringboot.demo;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 256-bit 비밀 키
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 1; // 15분
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 24시간

    private static io.jsonwebtoken.security.Keys Keys;
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) {
        try {
            // 토큰 파싱 및 서명 검증
            return Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 검증용 키 설정
                    .build()
                    .parseClaimsJws(token) // 서명 검증 및 클레임 추출
                    .getBody(); // Claims 반환
        } catch (ExpiredJwtException e) {
            // 만료된 토큰에 대한 명시적 처리
            throw new IllegalArgumentException("JWT 토큰이 만료되었습니다.");
        } catch (SignatureException e) {
            // 서명이 유효하지 않을 경우
            throw new IllegalArgumentException("유효하지 않은 JWT 서명입니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.");
        }
    }

}