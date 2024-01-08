package com.example.demo.business.reservation;

import com.example.demo.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user = :user AND r.status = 'COMPLETED'")
    Integer getCompletedReservation(@Param("user") UserEntity user);
}
