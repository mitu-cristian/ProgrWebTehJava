package com.example.demo;

import com.example.demo.auth.AuthenticationService;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.business.hotel.Hotel;
import com.example.demo.business.hotel.HotelRepository;
import com.example.demo.business.hotel.HotelRequest;
import com.example.demo.business.hotel.HotelService;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StartupInitialiser {
    private final UserRepository userRepo;
    private final AuthenticationService authService;
    private final HotelRepository hotelRepo;
    private final HotelService hotelService;

    public StartupInitialiser(AuthenticationService authService, UserRepository userRepo,
                              HotelRepository hotelRepo, HotelService hotelService
    ) {
        this.userRepo = userRepo;
        this.authService = authService;
        this.hotelRepo = hotelRepo;
        this.hotelService = hotelService;
    }

    @PostConstruct
    public void initialise() throws Exception{
//        create the hotel
        Optional<Hotel> optionalHotel = hotelRepo.findByName("Hotel Creta");
        if(optionalHotel.isEmpty()) {
            HotelRequest hotelRequest = new HotelRequest.Builder()
                    .name("Hotel Creta")
                    .build();
            hotelService.createHotel(hotelRequest);
        }

//        create the admin account
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
