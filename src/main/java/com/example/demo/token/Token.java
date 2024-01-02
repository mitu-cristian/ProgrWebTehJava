package com.example.demo.token;

import com.example.demo.user.UserEntity;
import jakarta.persistence.*;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;

    public Token() {}

    public Token(Integer id, String token, boolean expired, boolean revoked) {
        this.id = id;
        this.token = token;
        this.expired = expired;
        this.revoked = revoked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public static class Builder {
        private final Token token;

        public Builder() {
            token = new Token();
        }

        public Builder token(String _token) {
            token.token = _token;
            return this;
        }

        public Builder expired (boolean expired) {
            token.expired = expired;
            return this;
        }

        public Builder revoked (boolean revoked) {
            token.revoked = revoked;
            return this;
        }

        public Builder user (UserEntity user) {
            token.user = user;
            return this;
        }

        public Token build() {
            return token;
        }
    }
}
