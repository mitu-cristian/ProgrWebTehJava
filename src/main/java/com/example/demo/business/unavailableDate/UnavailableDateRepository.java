package com.example.demo.business.unavailableDate;

import com.example.demo.business.roomNumber.RoomNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Integer> {
    Optional<List<UnavailableDate>> findByRoomNumber(RoomNumber roomNumber);
}
