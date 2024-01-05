package com.example.demo.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh-token")
    private String refreshToken;
//    private String message;

    public String getAccessToken() {
     return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public static class Builder {
        private final AuthenticationResponse authResponse;

        public Builder() {
            authResponse = new AuthenticationResponse();
        }

        public Builder accessToken(String token) {
            authResponse.accessToken = token;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            authResponse.refreshToken = refreshToken;
            return this;
        }

//        public Builder message(String message) {
//            authResponse.message = message;
//            return this;
//        }

        public AuthenticationResponse build() {
            return authResponse;
        }
    }
}
