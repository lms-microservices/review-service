package com.lms.review_service.controller;

import com.lms.review_service.model.Review;
import com.lms.review_service.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/course/{courseId}")
    public Review addReview(@PathVariable Long courseId,
                            @RequestHeader("X-User-Id") Long userId,
                            @RequestHeader("X-User-Role") String role,
                            @Valid @RequestBody Review review) {
        return reviewService.addReview(courseId, userId, role, review);
    }

    @GetMapping("/course/{courseId}")
    public List<Review> getCourseReviews(@PathVariable Long courseId) {
        return reviewService.getCourseReviews(courseId);
    }

    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable Long reviewId,
                               @RequestHeader("X-User-Id") Long userId,
                               @RequestHeader("X-User-Role") String role,
                               @Valid @RequestBody Review review) {
        return reviewService.updateReview(reviewId, userId, role, review);
    }

    @PostMapping("/{reviewId}/reply")
    public Review replyToReview(@PathVariable Long reviewId,
                                @RequestHeader("X-User-Role") String role,
                                @RequestBody String reply) {
        return reviewService.replyToReview(reviewId, role, reply);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId,
                             @RequestHeader("X-User-Role") String role) {
        reviewService.deleteReview(reviewId, role);
    }

}