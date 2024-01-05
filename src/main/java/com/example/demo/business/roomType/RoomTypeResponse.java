package com.example.demo.business.roomType;

import com.example.demo.business.roomNumber.RoomNumber;

import java.math.BigDecimal;
import java.util.List;

public class RoomTypeResponse {
    private String name;
    private BigDecimal price;
    private Integer maxPeople;
    private List<RoomNumber> roomNumbers;

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

    public static class Builder {
        private final RoomTypeResponse response;

        public Builder() {
            response = new RoomTypeResponse();
        }

        public Builder name(String name) {
            response.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            response.price = price;
            return this;
        }

        public Builder maxPeople(Integer maxPeople) {
            response.maxPeople = maxPeople;
            return this;
        }

        public Builder roomNumbers(List<RoomNumber> roomNumbers) {
            response.roomNumbers = roomNumbers;
            return this;
        }

        public RoomTypeResponse build() {
            return response;
        }
    }
}
