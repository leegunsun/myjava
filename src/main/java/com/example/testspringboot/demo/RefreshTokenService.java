//package com.example.testspringboot.demo;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class RefreshTokenService {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public void saveRefreshToken(String userId, String refreshToken) {
//        redisTemplate.opsForValue().set(userId, refreshToken);
//    }
//
//    public boolean validateRefreshToken(String userId, String refreshToken) {
//        String storedToken = redisTemplate.opsForValue().get(userId);
//        return refreshToken.equals(storedToken);
//    }
//}
