package com.example.demo.business.unavailableDate;

import com.example.demo.business.roomNumber.RoomNumber;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "unavailable_dates")
public class UnavailableDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate unavailableDate;
    @ManyToOne
    @JoinColumn(name = "room_numbers_id")
    private RoomNumber roomNumber;

    public UnavailableDate() {}

    public UnavailableDate(Integer id, LocalDate unavailableDate, RoomNumber roomNumber) {
        this.id = id;
        this.unavailableDate = unavailableDate;
        this.roomNumber = roomNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(LocalDate unavailableDate) {
        this.unavailableDate = unavailableDate;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
    }
}
