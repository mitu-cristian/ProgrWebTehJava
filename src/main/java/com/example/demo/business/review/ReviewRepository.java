package com.example.demo.business.review;

import com.example.demo.user.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByUser(UserEntity user);
    @Modifying
    @Transactional
    @Query("DELETE from Review r where r.id = :reviewId")
    void deleteByReviewId(@Param("reviewId") Integer reviewId);
//    @Query("SELECT review from Review review where review.user.id = :userId")
//    Optional<Review> findByUserId(@Param("userId") Integer id);
}
