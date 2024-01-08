package com.example.demo.business.review;

import com.example.demo.user.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private java.lang.Integer id;
    @OneToOne
    @JoinColumn (name = "users_id")
    private UserEntity user;
    private Integer rating;
    private String description;

    public Review() {}

    public Review(java.lang.Integer id, UserEntity user, Integer rating, String description) {
        this.id = id;
        this.user = user;
        this.rating = rating;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

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

    public static class Builder {
        private final Review review;

        public Builder() {
            review = new Review();
        }

        public Builder user(UserEntity user){
            review.user = user;
            return this;
        }

        public Builder rating(Integer rating) {
            review.rating = rating;
            return this;
        }

        public Builder description(String description){
            review.description = description;
            return this;
        }

        public Review build(){
            return review;
        }
    }
}
