package com.example.demo.business.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ReservationUserRequest {
    @JsonProperty("check-in")
    private LocalDate checkIn;
    @JsonProperty("check-out")
    private LocalDate checkOut;
    @JsonProperty("number-of-people")
    private Integer nrPeople;

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

    public Integer getNrPeople() {
        return nrPeople;
    }

    public void setNrPeople(Integer nrPeople) {
        this.nrPeople = nrPeople;
    }
}
