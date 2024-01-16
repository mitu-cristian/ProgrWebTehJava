package com.example.demo.business.roomNumber;

import com.example.demo.business.roomType.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomNumberRepository extends JpaRepository<RoomNumber, Integer> {
    Optional<List<RoomNumber>> findByRoomType(RoomType roomType);
    Optional<RoomNumber> findByNumber(Integer number);
}
