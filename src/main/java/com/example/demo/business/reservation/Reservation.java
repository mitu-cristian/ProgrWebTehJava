package com.example.demo.business.reservation;

import com.example.demo.business.roomNumber.RoomNumber;
import com.example.demo.user.UserEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;
    private BigDecimal price;
    private Integer nrPeople;
    @ManyToOne
    @JoinColumn(name = "room_numbers_id")
    private RoomNumber roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Reservation() {}

    public Reservation(Integer id, UserEntity user, BigDecimal price, Integer nrPeople, RoomNumber roomNumber, LocalDate checkIn, LocalDate checkOut, Status status) {
        this.id = id;
        this.user = user;
        this.price = price;
        this.nrPeople = nrPeople;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNrPeople() {
        return nrPeople;
    }

    public void setNrPeople(Integer nrPeople) {
        this.nrPeople = nrPeople;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class Builder {
        private final Reservation reservation;

        public Builder() {
            reservation = new Reservation();
        }

        public Builder user(UserEntity user){
            reservation.user = user;
            return this;
        }

        public Builder price(BigDecimal price) {
            reservation.price = price;
            return this;
        }

        public Builder nrPeople(Integer nrPeople){
            reservation.nrPeople = nrPeople;
            return this;
        }

        public Builder roomNumber(RoomNumber roomNumber) {
            reservation.roomNumber = roomNumber;
            return this;
        }

        public Builder checkIn(LocalDate checkIn){
            reservation.checkIn = checkIn;
            return this;
        }

        public Builder checkOut(LocalDate checkOut) {
            reservation.checkOut = checkOut;
            return this;
        }

        public Builder status(Status status) {
            reservation.status = status;
            return this;
        }

        public Reservation builder(){
            return reservation;
        }
    }
}
