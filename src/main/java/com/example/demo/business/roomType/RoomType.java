package com.example.demo.business.roomType;

import com.example.demo.business.hotel.Hotel;
import com.example.demo.business.roomNumber.RoomNumber;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer maxPeople;
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomNumber> roomNumbers;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public RoomType() {}

    public RoomType(Integer id, String name, BigDecimal price, Integer maxPeople, List<RoomNumber> roomNumbers,
                    Hotel hotel) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.maxPeople = maxPeople;
        this.roomNumbers = roomNumbers;
        this.hotel = hotel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public List<RoomNumber> getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(List<RoomNumber> roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public static class Builder {
        private final RoomType roomType;

        public Builder() {
            roomType = new RoomType();
        }

        public Builder name(String name) {
            roomType.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            roomType.price = price;
            return this;
        }

        public Builder maxPeople(Integer maxPeople) {
            roomType.maxPeople = maxPeople;
            return this;
        }

        public RoomType build() {
            return roomType;
        }
    }
}
