package com.example.demo.auth;

public class AuthenticationResponse {
    private String token;

    public String getToken() {
     return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Builder {
        private final AuthenticationResponse authResponse;

        public Builder() {
            authResponse = new AuthenticationResponse();
        }

        public Builder token(String token) {
            authResponse.token = token;
            return this;
        }

        public AuthenticationResponse build() {
            return authResponse;
        }
    }
}
