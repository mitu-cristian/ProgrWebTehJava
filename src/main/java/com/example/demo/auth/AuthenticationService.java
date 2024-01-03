package com.example.demo.auth;

import com.example.demo.MessageResponse;
import com.example.demo.config.JwtService;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.user.Role;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
            String refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(user, jwtToken);
            return new AuthenticationResponse.Builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
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

    public AuthenticationResponse authenticate( AuthenticationRequest request) throws Exception{
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeTokens(user);
            saveUserToken(user, jwtToken);
            return new AuthenticationResponse.Builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            // Log the exception or print the stack trace
            e.printStackTrace();
            System.out.println("Exception during authentication: " + e.getMessage());
            // Handle the exception or rethrow it as needed
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public MessageResponse registerAdmin(RegisterRequest request) throws Exception {
        try {
            UserEntity admin = new UserEntity.Builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Admin account successfully created")
                    .build();
        }
        catch (Exception e) {
            // Log the exception or print the stack trace
            e.printStackTrace();
            System.out.println("Exception during authentication: " + e.getMessage());
            // Handle the exception or rethrow it as needed
            throw new RuntimeException("Registration failed", e);
        }
    }

    public void createAdminAccount(RegisterRequest request) throws Exception {
        try {
            UserEntity admin = new UserEntity.Builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception during creation of admin account " + e.getMessage());
            throw new RuntimeException("Registration failed", e);
        }
    }

    public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtService.extractEmail(refreshToken);
        if(email != null) {
            UserEntity userEntity = userRepository.findByEmail(email).get();
            if(jwtService.isTokenValid(refreshToken, userEntity)) {
                String accessToken = jwtService.generateToken(userEntity);
                revokeTokens(userEntity);
                saveUserToken(userEntity, accessToken);
                AuthenticationResponse authResponse = new AuthenticationResponse.Builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
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

    public MessageResponse deleteMyAccount(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token = authHeader.substring(7);
        final String email = jwtService.extractEmail(token);
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        try {
            userRepository.deleteById(optionalUserEntity.get().getId());
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Account successfully deleted.")
                    .build();
        }
        catch (Exception e) {
            // Log the exception or print the stack trace
            e.printStackTrace();
            System.out.println("Exception thrown when deleting the account: " + e.getMessage());
            // Handle the exception or rethrow it as needed
            throw new RuntimeException("Failed to delete the account", e);
        }
    }

}
