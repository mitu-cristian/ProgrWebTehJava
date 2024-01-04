package com.example.demo.business.review;

import com.example.demo.user.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn (name = "users_id")
    private UserEntity user;
    @Enumerated(EnumType.ORDINAL)
    private Rating rating;
    private String description;

    public Review() {}

    public Review(Integer id, UserEntity user, Rating rating, String description) {
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
