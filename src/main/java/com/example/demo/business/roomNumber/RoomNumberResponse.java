package com.example.demo.business.roomNumber;

import com.example.demo.business.roomType.RoomType;

public class RoomNumberResponse {
    private Integer id;
    private Integer number;

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

    public static class Builder {
        private final RoomNumberResponse response;

        public Builder() {
            response = new RoomNumberResponse();
        }

        public Builder id(Integer id){
            response.id = id;
            return this;
        }


        public Builder number(Integer number){
            response.number = number;
            return this;
        }

        public RoomNumberResponse build(){
            return response;
        }
    }
}
