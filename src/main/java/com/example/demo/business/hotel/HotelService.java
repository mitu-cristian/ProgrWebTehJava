package com.example.demo.business.hotel;

import com.example.demo.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    private final HotelRepository hotelRepo;

    public HotelService(HotelRepository hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    public void createHotel (HotelRequest hotelRequest) {
        try {
            Hotel hotel = new Hotel.Builder()
                    .name("Hotel Creta")
                    .build();
            hotelRepo.save(hotel);
            System.out.println("Hotel created");
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception thrown when creating the hotel: " + e.getMessage());
            throw new RuntimeException("Failed to create the hotel", e);
        }
    }
}
