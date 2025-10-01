package com.foodingo.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foodingo.review.dto.ReviewRequest;
import com.foodingo.review.dto.ReviewResponse;
import com.foodingo.review.dto.ReviewStatsResponse;

public interface ReviewService {
    
    ReviewResponse createReview(ReviewRequest request, String userId);
    
    ReviewResponse updateReview(String reviewId, ReviewRequest request, String userId);
    
    ReviewResponse getReviewById(String reviewId);
    
    List<ReviewResponse> getReviewsByRestaurant(String restaurantId);
    
    List<ReviewResponse> getReviewsByFood(String foodId);
    
    List<ReviewResponse> getReviewsByUser(String userId);
    
    Page<ReviewResponse> getReviewsByRestaurant(String restaurantId, Pageable pageable);
    
    Page<ReviewResponse> getReviewsByFood(String foodId, Pageable pageable);
    
    List<ReviewResponse> getReviewsByRestaurantAndRating(String restaurantId, Integer rating);
    
    List<ReviewResponse> getReviewsByRestaurantAndMinRating(String restaurantId, Integer minRating);
    
    List<ReviewResponse> getReviewsByRestaurantAndMaxRating(String restaurantId, Integer maxRating);
    
    List<ReviewResponse> getReviewsByRestaurantAndRatingRange(String restaurantId, Integer minRating, Integer maxRating);
    
    List<ReviewResponse> getVerifiedReviewsByRestaurant(String restaurantId);
    
    List<ReviewResponse> getHelpfulReviewsByRestaurant(String restaurantId);
    
    List<ReviewResponse> searchReviewsByRestaurant(String restaurantId, String searchText);
    
    List<ReviewResponse> getReviewsByRestaurantAndTags(String restaurantId, List<String> tags);
    
    ReviewStatsResponse getReviewStatsByRestaurant(String restaurantId);
    
    ReviewStatsResponse getReviewStatsByFood(String foodId);
    
    ReviewResponse markReviewAsHelpful(String reviewId, String userId);
    
    ReviewResponse removeHelpfulMark(String reviewId, String userId);
    
    ReviewResponse addRestaurantResponse(String reviewId, String response, String restaurantOwnerId);
    
    void deleteReview(String reviewId, String userId);
    
    void hideReview(String reviewId, String userId);
    
    void showReview(String reviewId, String userId);
}

