package com.example.demo.business.review;

import java.util.Objects;

public enum Rating {

    VALUE_1(1),
    VALUE_2(2),
    VALUE_3(3),
    VALUE_4(4),
    VALUE_5(5);

    private final int value;

    Rating(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static Rating fromValue(Integer value) {
        for (Rating rating : Rating.values()) {
            if (Objects.equals(rating.getValue(), value)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid Rating value: " + value);
    }

}
