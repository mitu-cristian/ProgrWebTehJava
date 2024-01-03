package com.example.demo;

import com.example.demo.auth.AuthenticationService;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StartupInitialiser {
    private final UserRepository userRepo;
    private final AuthenticationService authService;

    public StartupInitialiser(AuthenticationService authService, UserRepository userRepo) {
        this.userRepo = userRepo;
        this.authService = authService;
    }

    @PostConstruct
    public void initialise() throws Exception{
        Optional<UserEntity> optionalAdminAcc = userRepo.findByEmail("admin@gmail.com");
        if(optionalAdminAcc.isEmpty()) {
            RegisterRequest createAdminRequest = new RegisterRequest.Builder()
                    .firstname("admin")
                    .lastname("admin")
                    .email("admin@gmail.com")
                    .password("password")
                    .build();
            authService.createAdminAccount(createAdminRequest);
        }
    }
}
