package com.example.demo.business.roomType;

import com.example.demo.business.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    Optional<List<RoomType>> findByHotel (Hotel hotel);
}
