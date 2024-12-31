package com.example.testspringboot.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

//@Data
//public class TokenResponse {
//    private String accessToken;
//    private String refreshToken;
//
//    public TokenResponse(String accessToken, String refreshToken) {
//        this.accessToken = accessToken;
//        this.refreshToken = refreshToken;
//    }
//
//    // Getter
//}

@Getter
@AllArgsConstructor
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
}