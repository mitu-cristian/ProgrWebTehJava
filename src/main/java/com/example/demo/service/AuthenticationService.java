package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserEntity addNewUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public JwtResponse authenticateAndGetToken(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken
                        (authRequest.getUsername(), authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
//            Find the userEntity
            UserEntity userEntity = userRepository.findByName(authRequest.getUsername()).get();
//            Check the refresh_token table and delete the record if exists
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserEntity(userEntity);
            refreshToken.ifPresent(token -> refreshTokenRepository.delete(token));
//            Generate the response
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(jwtService.generateToken(authRequest.getUsername()));
            jwtResponse.setToken(refreshTokenService.createRefreshToken(authRequest.getUsername()).getToken());
            return jwtResponse;
        }
        else
            throw new UsernameNotFoundException("Invalid credentials.");
    }

    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserEntity)
                .map(userEntity -> {
                    String accessToken = jwtService.generateToken(userEntity.getName());
                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setAccessToken(accessToken);
                    jwtResponse.setToken(refreshTokenRequest.getToken());
                    return jwtResponse;
                })
                .orElseThrow(()-> new RuntimeException("RefreshToken is not in the database"));
    }
}
