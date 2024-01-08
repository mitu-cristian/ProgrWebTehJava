package com.example.demo.business.review;

import com.example.demo.MessageResponse;
import com.example.demo.business.reservation.ReservationUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

//  @METHOD     GET
//  @DESC       user gets the review
//  @ACCESS     ADMIN
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/user/review")
    public ResponseEntity<ReviewResponse> getReview(HttpServletRequest request) {
       return ResponseEntity.ok(reviewService.getReview(request));
    }

//  @METHOD     GET
//  @DESC       get all the reviews
//  @ACCESS     PUBLIC
    @RequestMapping(value = "/public/review")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

//  @METHOD     POST
//  @DESC       user creates a review
//  @ACCESS     USER
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/user/review", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> createReview(HttpServletRequest httpRequest,
                                                        @RequestBody ReviewRequest reviewRequest) {
            MessageResponse messageResponse = reviewService.createReview(httpRequest, reviewRequest);
            if(messageResponse.isSuccess() == true)
                return ResponseEntity.status(201).body(messageResponse);
            else
                return ResponseEntity.status(400).body(messageResponse);
    }

//  @METHOD     PUT
//  @DESC       user updates the review
//  ACCESS      USER
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/user/review", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> updateReview(HttpServletRequest httpRequest,
                                                        @RequestBody ReviewRequest reviewRequest) {
        MessageResponse messageResponse = reviewService.updateReview(httpRequest, reviewRequest);
        if(messageResponse.isSuccess() == true)
            return ResponseEntity.status(201).body(messageResponse);
        else
            return ResponseEntity.status(400).body(messageResponse);
    }

//  @METHOD     DELETE
//  @DESC       user deletes the review
//  @ACCESS     USER
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/user/review", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteReview(HttpServletRequest request){
        MessageResponse messageResponse = reviewService.deleteReview(request);
        if(messageResponse.isSuccess())
            return ResponseEntity.ok(messageResponse);
        else
            return ResponseEntity.status(400).body(messageResponse);
    }

}
