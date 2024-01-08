package com.example.demo.business.unavailableDate;

import com.example.demo.business.roomNumber.RoomNumber;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Integer> {
    Optional<List<UnavailableDate>> findByRoomNumber(RoomNumber roomNumber);
    @Modifying
    @Transactional
    @Query("DELETE from UnavailableDate ud where ud.unavailableDate = :date and ud.roomNumber = :roomNumber")
    void deleteUnavailableDateByDateAndRoomNumber(@Param("roomNumber") RoomNumber roomNumber, @Param("date") LocalDate date);
    @Query("SELECT COUNT(ud) from UnavailableDate ud where ud.roomNumber = :roomNumber AND ud.unavailableDate = :unavailableDate")
    Integer findByRoomNumberAndUnavailableDate(@Param("roomNumber") RoomNumber roomNumber, @Param("unavailableDate") LocalDate unavailableDate);
}
