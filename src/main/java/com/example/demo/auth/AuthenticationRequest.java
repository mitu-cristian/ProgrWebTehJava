package com.example.demo.auth;

public class AuthenticationRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder{
        private AuthenticationRequest authRequest;

        public Builder() {
            authRequest = new AuthenticationRequest();
        }

        public Builder email(String email) {
            authRequest.email = email;
            return this;
        }

        public Builder password(String password){
            authRequest.password = password;
            return this;
        }

        public AuthenticationRequest build() {
            return authRequest;
        }
    }
}
