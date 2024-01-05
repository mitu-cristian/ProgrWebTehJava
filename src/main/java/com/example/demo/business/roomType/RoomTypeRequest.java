package com.example.demo.business.roomType;

import com.example.demo.business.hotel.Hotel;

import java.math.BigDecimal;

public class RoomTypeRequest {
    private String name;
    private BigDecimal price;
    private Integer maxPeople;
    private Hotel hotel;

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

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public static class Builder {
        private final RoomTypeRequest request;

        public Builder() {
            request = new RoomTypeRequest();
        }

        public Builder name(String name){
            request.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            request.price = price;
            return this;
        }

        public Builder maxPeople(Integer maxPeople){
            request.maxPeople = maxPeople;
            return this;
        }

        public Builder hotel(Hotel hotel) {
            request.hotel = hotel;
            return this;
        }

        public RoomTypeRequest build(){
            return request;
        }
    }
}
