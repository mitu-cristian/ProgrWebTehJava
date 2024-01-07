package com.example.demo.business.review;


import com.example.demo.MessageResponse;
import com.example.demo.business.reservation.ReservationRepository;
import com.example.demo.config.JwtService;
import com.example.demo.user.UserEntity;
import com.example.demo.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(UserRepository userRepository, ReservationRepository reservationRepository,
                         ReviewRepository reviewRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.jwtService = jwtService;
    }

    public ReviewResponse getReview(HttpServletRequest request) {
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UserEntity user = userRepository.findByEmail(jwtService.extractEmail(jwtToken)).get();
        Optional<Review> optionalReview = reviewRepository.findByUser(user);
        if(optionalReview.isPresent()) {
            Review existingReview = optionalReview.get();
            return new ReviewResponse.Builder()
                    .firstname(existingReview.getUser().getFirstname())
                    .lastname(existingReview.getUser().getLastname())
                    .rating(existingReview.getRating().getValue())
                    .description(existingReview.getDescription())
                    .build();
        }
        return new ReviewResponse();
    }

    public MessageResponse createReview(HttpServletRequest httpRequest, ReviewRequest reviewRequest) {
            final String jwtToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            UserEntity user = userRepository.findByEmail(jwtService.extractEmail(jwtToken)).get();
            Optional<Review> optionalReview = reviewRepository.findByUser(user);
//      If user has a review
            if (optionalReview.isPresent())
                return new MessageResponse.Builder()
                        .success(false)
                        .message("User already have posted a review.")
                        .build();

//      If user doesn't have a review
            if (optionalReview.isEmpty()) {
                if (reservationRepository.getCompletedReservation(user) == 0)
                    return new MessageResponse.Builder()
                            .success(false)
                            .message("User must have at least one completed reservation")
                            .build();
                else {
                    Review review = new Review.Builder()
                            .user(user)
                            .rating(Rating.fromValue(reviewRequest.getRating()))
                            .description(reviewRequest.getDescription())
                            .build();
                reviewRepository.save(review);
                    return new MessageResponse.Builder()
                            .success(true)
                            .message("Review successfully created.")
                            .build();
                }
            }
            return new MessageResponse.Builder()
                    .success(false)
                    .message("Error while creating the review.")
                    .build();
    }

    public MessageResponse updateReview(HttpServletRequest httpRequest, ReviewRequest reviewRequest){
        final String jwtToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UserEntity user = userRepository.findByEmail(jwtService.extractEmail(jwtToken)).get();
        Optional<Review> optionalReview = reviewRepository.findByUser(user);
        if(optionalReview.isEmpty())
            return new MessageResponse.Builder()
                    .success(false)
                    .message("User doesn't have any review.")
                    .build();
        if(optionalReview.isPresent()) {
            Review existingReview = optionalReview.get();
            if(reviewRequest.getRating() == null)
                existingReview.setRating(Rating.fromValue(reviewRequest.getRating()));
            if(reviewRequest.getDescription() != null)
                existingReview.setDescription(reviewRequest.getDescription());
            reviewRepository.save(existingReview);
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Review successfully updated.")
                    .build();
        }
        return new MessageResponse.Builder()
                .success(false)
                .message("Error while updating the review.")
                .build();
    }

    public MessageResponse deleteReview(HttpServletRequest request) {
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UserEntity user = userRepository.findByEmail(jwtService.extractEmail(jwtToken)).get();
        Optional<Review> optionalReview = reviewRepository.findByUser(user);
        if(optionalReview.isEmpty())
            return new MessageResponse.Builder()
                    .success(false)
                    .message("User doesn't have any review.")
                    .build();
        else {
            reviewRepository.delete(optionalReview.get());
            return new MessageResponse.Builder()
                    .success(true)
                    .message("Review successfully deleted")
                    .build();
        }
    }
}