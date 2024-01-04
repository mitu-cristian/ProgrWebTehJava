package com.example.demo.business.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    Optional<Hotel> findByName(String name);
}
