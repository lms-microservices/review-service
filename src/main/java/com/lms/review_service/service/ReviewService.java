package com.lms.review_service.service;

import com.lms.review_service.client.EnrollmentClient;
import com.lms.review_service.model.Review;
import com.lms.review_service.repo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EnrollmentClient enrollmentClient;

    public Review addReview(Long courseId, Long userId, String role, Review review) {
        if (!"STUDENT".equals(role)) {
            throw new RuntimeException("Only students can add reviews");
        }
        // Temporarily hardcoded for testing
        boolean enrolled = true; // enrollmentClient.isEnrolled(userId, courseId);
        if (!enrolled) {
            throw new RuntimeException("Student is not enrolled in this course");
        }

        review.setStudentId(userId);
        review.setCourseId(courseId);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getCourseReviews(Long courseId) {
        return reviewRepository.findByCourseId(courseId);
    }

    public Review updateReview(Long reviewId, Long userId, String role, Review updated) {
        if (!"STUDENT".equals(role)) {
            throw new RuntimeException("Only students can update reviews");
        }

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getStudentId().equals(userId)) {
            throw new RuntimeException("Students can only update their own reviews");
        }

        // Temporarily hardcoding enrollment check to true for testing
        boolean enrolled = true; // enrollmentClient.isEnrolled(userId, review.getCourseId());
        if (!enrolled) {
            throw new RuntimeException("Student is not enrolled in this course");
        }

        review.setRating(updated.getRating());
        review.setComment(updated.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public Review replyToReview(Long reviewId, String role, String reply) {
        if (!"INSTRUCTOR".equals(role)) {
            throw new RuntimeException("Only admins can reply to reviews");
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setInstructorReply(reply);
        review.setRepliedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId, String role) {
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Only admins can delete reviews");
        }
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }

        reviewRepository.deleteById(reviewId);
    }
}