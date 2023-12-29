package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/new-user", method = RequestMethod.POST)
    public UserEntity addNewUser(@RequestBody UserEntity userEntity) {
        return authenticationService.addNewUser(userEntity);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        return authenticationService.authenticateAndGetToken(authRequest);
    }

    @RequestMapping(value="/refresh-token", method = RequestMethod.POST)
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }

}
