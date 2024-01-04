package com.example.demo.business.reservation;

import com.example.demo.business.roomNumber.RoomNumber;
import com.example.demo.user.UserEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
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
    private Date checkIn;
    private Date checkOut;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Reservation() {}

    public Reservation(Integer id, UserEntity user, BigDecimal price, Integer nrPeople, RoomNumber roomNumber, Date checkIn, Date checkOut, Status status) {
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

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
