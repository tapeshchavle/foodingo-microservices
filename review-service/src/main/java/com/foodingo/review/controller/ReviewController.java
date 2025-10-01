package com.foodingo.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.review.dto.ReviewRequest;
import com.foodingo.review.dto.ReviewResponse;
import com.foodingo.review.dto.ReviewStatsResponse;
import com.foodingo.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "Review management APIs")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    @Operation(summary = "Create a new review")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        ReviewResponse response = reviewService.createReview(request, currentUserId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{reviewId}")
    @Operation(summary = "Update a review")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        ReviewResponse response = reviewService.updateReview(reviewId, request, currentUserId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{reviewId}")
    @Operation(summary = "Get review by ID")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable String reviewId) {
        ReviewResponse review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get reviews by restaurant")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurant(@PathVariable String restaurantId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurant(restaurantId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/paginated")
    @Operation(summary = "Get reviews by restaurant with pagination")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByRestaurantPaginated(
            @PathVariable String restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ReviewResponse> reviews = reviewService.getReviewsByRestaurant(restaurantId, pageable);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/food/{foodId}")
    @Operation(summary = "Get reviews by food item")
    public ResponseEntity<List<ReviewResponse>> getReviewsByFood(@PathVariable String foodId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByFood(foodId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get user's reviews")
    public ResponseEntity<List<ReviewResponse>> getUserReviews(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<ReviewResponse> reviews = reviewService.getReviewsByUser(currentUserId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/rating/{rating}")
    @Operation(summary = "Get reviews by restaurant and rating")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurantAndRating(
            @PathVariable String restaurantId,
            @PathVariable Integer rating) {
        
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurantAndRating(restaurantId, rating);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/min-rating/{minRating}")
    @Operation(summary = "Get reviews by restaurant with minimum rating")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurantAndMinRating(
            @PathVariable String restaurantId,
            @PathVariable Integer minRating) {
        
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurantAndMinRating(restaurantId, minRating);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/max-rating/{maxRating}")
    @Operation(summary = "Get reviews by restaurant with maximum rating")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurantAndMaxRating(
            @PathVariable String restaurantId,
            @PathVariable Integer maxRating) {
        
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurantAndMaxRating(restaurantId, maxRating);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/rating-range")
    @Operation(summary = "Get reviews by restaurant with rating range")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurantAndRatingRange(
            @PathVariable String restaurantId,
            @RequestParam Integer minRating,
            @RequestParam Integer maxRating) {
        
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurantAndRatingRange(restaurantId, minRating, maxRating);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/verified")
    @Operation(summary = "Get verified reviews by restaurant")
    public ResponseEntity<List<ReviewResponse>> getVerifiedReviewsByRestaurant(@PathVariable String restaurantId) {
        List<ReviewResponse> reviews = reviewService.getVerifiedReviewsByRestaurant(restaurantId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/helpful")
    @Operation(summary = "Get helpful reviews by restaurant")
    public ResponseEntity<List<ReviewResponse>> getHelpfulReviewsByRestaurant(@PathVariable String restaurantId) {
        List<ReviewResponse> reviews = reviewService.getHelpfulReviewsByRestaurant(restaurantId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/search")
    @Operation(summary = "Search reviews by restaurant")
    public ResponseEntity<List<ReviewResponse>> searchReviewsByRestaurant(
            @PathVariable String restaurantId,
            @RequestParam String searchText) {
        
        List<ReviewResponse> reviews = reviewService.searchReviewsByRestaurant(restaurantId, searchText);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/tags")
    @Operation(summary = "Get reviews by restaurant and tags")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurantAndTags(
            @PathVariable String restaurantId,
            @RequestParam List<String> tags) {
        
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurantAndTags(restaurantId, tags);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/restaurant/{restaurantId}/stats")
    @Operation(summary = "Get review statistics by restaurant")
    public ResponseEntity<ReviewStatsResponse> getReviewStatsByRestaurant(@PathVariable String restaurantId) {
        ReviewStatsResponse stats = reviewService.getReviewStatsByRestaurant(restaurantId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/food/{foodId}/stats")
    @Operation(summary = "Get review statistics by food item")
    public ResponseEntity<ReviewStatsResponse> getReviewStatsByFood(@PathVariable String foodId) {
        ReviewStatsResponse stats = reviewService.getReviewStatsByFood(foodId);
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/{reviewId}/helpful")
    @Operation(summary = "Mark review as helpful")
    public ResponseEntity<ReviewResponse> markReviewAsHelpful(
            @PathVariable String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        ReviewResponse review = reviewService.markReviewAsHelpful(reviewId, currentUserId);
        return ResponseEntity.ok(review);
    }
    
    @DeleteMapping("/{reviewId}/helpful")
    @Operation(summary = "Remove helpful mark from review")
    public ResponseEntity<ReviewResponse> removeHelpfulMark(
            @PathVariable String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        ReviewResponse review = reviewService.removeHelpfulMark(reviewId, currentUserId);
        return ResponseEntity.ok(review);
    }
    
    @PostMapping("/{reviewId}/response")
    @Operation(summary = "Add restaurant response to review")
    public ResponseEntity<ReviewResponse> addRestaurantResponse(
            @PathVariable String reviewId,
            @RequestParam String response,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        ReviewResponse review = reviewService.addRestaurantResponse(reviewId, response, currentUserId);
        return ResponseEntity.ok(review);
    }
    
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        reviewService.deleteReview(reviewId, currentUserId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{reviewId}/hide")
    @Operation(summary = "Hide a review")
    public ResponseEntity<Void> hideReview(
            @PathVariable String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        reviewService.hideReview(reviewId, currentUserId);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{reviewId}/show")
    @Operation(summary = "Show a review")
    public ResponseEntity<Void> showReview(
            @PathVariable String reviewId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        reviewService.showReview(reviewId, currentUserId);
        return ResponseEntity.ok().build();
    }
}

