package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.user.Role;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService (UserRepository userRepository, PasswordEncoder passwordEncoder,
                                  JwtService jwtService, AuthenticationManager authenticationManager,
                                  TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register (RegisterRequest request) {
        try {
            UserEntity user = new UserEntity.Builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            saveUserToken(user, jwtToken);
            return new AuthenticationResponse.Builder()
                    .token(jwtToken)
                    .build();
        }
        catch (Exception e) {
            // Log the exception or print the stack trace
            e.printStackTrace();
            System.out.println("Exception during registration: " + e.getMessage());
            // Handle the exception or rethrow it as needed
            throw new RuntimeException("Registration failed", e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception{
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwtToken = jwtService.generateToken(user);
            revokeTokens(user);
            saveUserToken(user, jwtToken);
            return new AuthenticationResponse.Builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            // Log the exception or print the stack trace
            e.printStackTrace();
            System.out.println("Exception during authentication: " + e.getMessage());
            // Handle the exception or rethrow it as needed
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private void revokeTokens(UserEntity user) {
        List<Token> oldTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        oldTokens.forEach(
                oldToken -> {
                    oldToken.setExpired(true);
                    oldToken.setRevoked(true);
                    tokenRepository.save(oldToken);
                }
        );
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        Token token = new Token.Builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

}
