package com.example.demo.business.unavailableDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class UnavailableDateRequest {
    @JsonProperty("unavailable-date")
    private LocalDate unavailableDate;

    public LocalDate getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(LocalDate unavailableDate) {
        this.unavailableDate = unavailableDate;
    }
}
