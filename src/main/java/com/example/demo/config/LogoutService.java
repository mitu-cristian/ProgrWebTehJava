package com.example.demo.config;

import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    public LogoutService (TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Token storedJwt = tokenRepository.findByToken(jwt).orElse(null);
        if(storedJwt != null) {
            storedJwt.setExpired(true);
            storedJwt.setRevoked(true);
            tokenRepository.save(storedJwt);
        }
    }
}
