package com.example.demo.business.roomNumber;

import com.example.demo.business.reservation.Reservation;
import com.example.demo.business.roomType.RoomType;
import com.example.demo.business.unavailableDate.UnavailableDate;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "room_numbers", uniqueConstraints = @UniqueConstraint(columnNames = "number"))
public class RoomNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "room_types_id")
    private RoomType roomType;
    private Integer number;
    @OneToMany(mappedBy = "roomNumber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
    @OneToMany (mappedBy = "roomNumber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnavailableDate> unavailableDates;

    public RoomNumber() {}

    public RoomNumber(Integer id, Integer number,
                      List<UnavailableDate> unavailableDates,
                      RoomType roomType, List<Reservation> reservations) {

        this.id = id;
        this.number = number;
        this.unavailableDates = unavailableDates;
        this.roomType = roomType;
        this.reservations = reservations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<UnavailableDate> getUnavailableDates() {
        return unavailableDates;
    }

    public void setUnavailableDates(List<UnavailableDate> unavailableDates) {
        this.unavailableDates = unavailableDates;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public static class Builder {
        private final RoomNumber roomNumber;

        public Builder() {
            roomNumber = new RoomNumber();
        }

        public Builder number(Integer number) {
            roomNumber.number = number;
            return this;
        }

        public Builder roomType(RoomType roomType) {
            roomNumber.roomType = roomType;
            return this;
        }

        public RoomNumber build() {
            return roomNumber;
        }
    }
}
