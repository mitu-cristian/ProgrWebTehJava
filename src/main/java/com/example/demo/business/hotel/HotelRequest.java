package com.example.demo.business.hotel;

public class HotelRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private final HotelRequest hotelRequest;

        public Builder() {
            hotelRequest = new HotelRequest();
        }

        public Builder name(String name) {
            hotelRequest.name = name;
            return this;
        }

        public HotelRequest build() {
            return hotelRequest;
        }
    }
}
