package com.example.demo.business.review;

public class ReviewResponse {
    private String firstname;
    private String lastname;
    private Integer rating;
    private String description;

    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public static class Builder {
        private final ReviewResponse response;

        public Builder() {
            response = new ReviewResponse();
        }

        public Builder firstname(String firstname) {
            response.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname){
            response.lastname = lastname;
            return this;
        }

        public Builder rating(Integer rating) {
            response.rating = rating;
            return this;
        }

        public Builder description(String description) {
            response.description = description;
            return this;
        }

        public ReviewResponse build(){
            return response;
        }
    }
}
