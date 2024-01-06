package com.example.demo.business.unavailableDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UnavailableDateResponse {
    private Integer id;
    @JsonProperty("unavailable-date")
    private LocalDate unavailableDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(LocalDate unavailableDate) {
        this.unavailableDate = unavailableDate;
    }

    public static class Builder{
        private final UnavailableDateResponse response;

        public Builder() {
            response = new UnavailableDateResponse();
        }

        public Builder id(Integer id){
            response.id = id;
            return this;
        }

        public Builder unavailableDate(LocalDate unavailableDate) {
            response.unavailableDate = unavailableDate;
            return this;
        }

        public UnavailableDateResponse build() {
            return response;
        }
    }
}
