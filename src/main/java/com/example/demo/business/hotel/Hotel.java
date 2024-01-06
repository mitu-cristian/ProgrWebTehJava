package com.example.demo.business.hotel;

import com.example.demo.business.roomType.RoomType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes;

    public Hotel() {}

    public Hotel(Integer id, String name, List<RoomType> roomTypes) {
        this.id = id;
        this.name = name;
        this.roomTypes = roomTypes;
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

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public static class Builder {
        private final Hotel hotel;

        public Builder() {
            hotel = new Hotel();
        }

        public Builder name(String name) {
            hotel.name = name;
            return this;
        }

        public Hotel build() {
            return hotel;
        }
    }
}
