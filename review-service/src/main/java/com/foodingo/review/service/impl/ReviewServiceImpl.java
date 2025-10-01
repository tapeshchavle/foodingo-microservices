package com.foodingo.review.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.review.dto.ReviewRequest;
import com.foodingo.review.dto.ReviewResponse;
import com.foodingo.review.dto.ReviewStatsResponse;
import com.foodingo.review.entity.ReviewEntity;
import com.foodingo.review.repository.ReviewRepository;
import com.foodingo.review.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    @CacheEvict(value = {"reviews", "reviewStats"}, allEntries = true)
    public ReviewResponse createReview(ReviewRequest request, String userId) {
        // Check if user already reviewed this restaurant/food
        if (request.getFoodId() != null) {
            if (reviewRepository.findByUserIdAndFoodId(userId, request.getFoodId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already reviewed this food item");
            }
        } else {
            if (reviewRepository.findByUserIdAndRestaurantId(userId, request.getRestaurantId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already reviewed this restaurant");
            }
        }
        
        ReviewEntity review = modelMapper.map(request, ReviewEntity.class);
        review.setUserId(userId);
        review.setCreatedAt(LocalDateTime.now());
        review.setIsVisible(true);
        review.setIsVerified(false); // TODO: Verify based on order completion
        review.setIsHelpful(false);
        review.setHelpfulCount(0);
        
        ReviewEntity savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewResponse.class);
    }
    
    @Override
    @CacheEvict(value = {"reviews", "reviewStats"}, allEntries = true)
    public ReviewResponse updateReview(String reviewId, ReviewRequest request, String userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own reviews");
        }
        
        modelMapper.map(request, review);
        review.setUpdatedAt(LocalDateTime.now());
        
        ReviewEntity updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewResponse.class);
    }
    
    @Override
    @Cacheable(value = "reviews", key = "#reviewId")
    public ReviewResponse getReviewById(String reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        return modelMapper.map(review, ReviewResponse.class);
    }
    
    @Override
    @Cacheable(value = "reviews", key = "'restaurant_' + #restaurantId")
    public List<ReviewResponse> getReviewsByRestaurant(String restaurantId) {
        return reviewRepository.findByRestaurantIdAndIsVisibleTrue(restaurantId).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    @Cacheable(value = "reviews", key = "'food_' + #foodId")
    public List<ReviewResponse> getReviewsByFood(String foodId) {
        return reviewRepository.findByFoodIdAndIsVisibleTrue(foodId).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getReviewsByUser(String userId) {
        return reviewRepository.findByUserIdAndIsVisibleTrue(userId).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public Page<ReviewResponse> getReviewsByRestaurant(String restaurantId, Pageable pageable) {
        return reviewRepository.findByRestaurantIdAndIsVisibleTrue(restaurantId, pageable)
            .map(review -> modelMapper.map(review, ReviewResponse.class));
    }
    
    @Override
    public Page<ReviewResponse> getReviewsByFood(String foodId, Pageable pageable) {
        return reviewRepository.findByFoodIdAndIsVisibleTrue(foodId, pageable)
            .map(review -> modelMapper.map(review, ReviewResponse.class));
    }
    
    @Override
    public List<ReviewResponse> getReviewsByRestaurantAndRating(String restaurantId, Integer rating) {
        return reviewRepository.findByRestaurantIdAndRating(restaurantId, rating).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getReviewsByRestaurantAndMinRating(String restaurantId, Integer minRating) {
        return reviewRepository.findByRestaurantIdAndRatingGreaterThanEqual(restaurantId, minRating).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getReviewsByRestaurantAndMaxRating(String restaurantId, Integer maxRating) {
        return reviewRepository.findByRestaurantIdAndRatingLessThanEqual(restaurantId, maxRating).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getReviewsByRestaurantAndRatingRange(String restaurantId, Integer minRating, Integer maxRating) {
        return reviewRepository.findByRestaurantIdAndRatingBetween(restaurantId, minRating, maxRating).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getVerifiedReviewsByRestaurant(String restaurantId) {
        return reviewRepository.findByRestaurantIdAndIsVerifiedTrue(restaurantId).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getHelpfulReviewsByRestaurant(String restaurantId) {
        return reviewRepository.findByRestaurantIdAndIsHelpfulTrue(restaurantId).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> searchReviewsByRestaurant(String restaurantId, String searchText) {
        return reviewRepository.findByRestaurantIdAndCommentContainingIgnoreCase(restaurantId, searchText).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ReviewResponse> getReviewsByRestaurantAndTags(String restaurantId, List<String> tags) {
        return reviewRepository.findByRestaurantIdAndTagsIn(restaurantId, tags).stream()
            .map(review -> modelMapper.map(review, ReviewResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    @Cacheable(value = "reviewStats", key = "'restaurant_' + #restaurantId")
    public ReviewStatsResponse getReviewStatsByRestaurant(String restaurantId) {
        List<ReviewEntity> reviews = reviewRepository.findByRestaurantIdAndIsVisibleTrue(restaurantId);
        
        if (reviews.isEmpty()) {
            return ReviewStatsResponse.builder()
                .restaurantId(restaurantId)
                .averageRating(0.0)
                .totalReviews(0)
                .build();
        }
        
        double averageRating = reviews.stream()
            .mapToInt(ReviewEntity::getRating)
            .average()
            .orElse(0.0);
        
        Map<Integer, Integer> ratingDistribution = reviews.stream()
            .collect(Collectors.groupingBy(
                ReviewEntity::getRating,
                Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
            ));
        
        long verifiedCount = reviews.stream()
            .mapToLong(review -> review.getIsVerified() ? 1 : 0)
            .sum();
        
        long helpfulCount = reviews.stream()
            .mapToLong(review -> review.getIsHelpful() ? 1 : 0)
            .sum();
        
        double helpfulPercentage = reviews.size() > 0 ? (double) helpfulCount / reviews.size() * 100 : 0.0;
        
        return ReviewStatsResponse.builder()
            .restaurantId(restaurantId)
            .averageRating(Math.round(averageRating * 10.0) / 10.0)
            .totalReviews(reviews.size())
            .ratingDistribution(ratingDistribution)
            .verifiedReviews((int) verifiedCount)
            .helpfulReviews((int) helpfulCount)
            .helpfulPercentage(Math.round(helpfulPercentage * 10.0) / 10.0)
            .build();
    }
    
    @Override
    @Cacheable(value = "reviewStats", key = "'food_' + #foodId")
    public ReviewStatsResponse getReviewStatsByFood(String foodId) {
        List<ReviewEntity> reviews = reviewRepository.findByFoodIdAndIsVisibleTrue(foodId);
        
        if (reviews.isEmpty()) {
            return ReviewStatsResponse.builder()
                .restaurantId(foodId)
                .averageRating(0.0)
                .totalReviews(0)
                .build();
        }
        
        double averageRating = reviews.stream()
            .mapToInt(ReviewEntity::getRating)
            .average()
            .orElse(0.0);
        
        Map<Integer, Integer> ratingDistribution = reviews.stream()
            .collect(Collectors.groupingBy(
                ReviewEntity::getRating,
                Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
            ));
        
        return ReviewStatsResponse.builder()
            .restaurantId(foodId)
            .averageRating(Math.round(averageRating * 10.0) / 10.0)
            .totalReviews(reviews.size())
            .ratingDistribution(ratingDistribution)
            .build();
    }
    
    @Override
    @CacheEvict(value = "reviews", allEntries = true)
    public ReviewResponse markReviewAsHelpful(String reviewId, String userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        // TODO: Check if user already marked as helpful
        review.setIsHelpful(true);
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        
        ReviewEntity updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewResponse.class);
    }
    
    @Override
    @CacheEvict(value = "reviews", allEntries = true)
    public ReviewResponse removeHelpfulMark(String reviewId, String userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        review.setIsHelpful(false);
        review.setHelpfulCount(Math.max(0, review.getHelpfulCount() - 1));
        
        ReviewEntity updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewResponse.class);
    }
    
    @Override
    @CacheEvict(value = "reviews", allEntries = true)
    public ReviewResponse addRestaurantResponse(String reviewId, String response, String restaurantOwnerId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        review.setResponse(response);
        review.setResponseBy(restaurantOwnerId);
        review.setResponseDate(LocalDateTime.now());
        
        ReviewEntity updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewResponse.class);
    }
    
    @Override
    @CacheEvict(value = {"reviews", "reviewStats"}, allEntries = true)
    public void deleteReview(String reviewId, String userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own reviews");
        }
        
        reviewRepository.deleteById(reviewId);
    }
    
    @Override
    @CacheEvict(value = "reviews", allEntries = true)
    public void hideReview(String reviewId, String userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only hide your own reviews");
        }
        
        review.setIsVisible(false);
        reviewRepository.save(review);
    }
    
    @Override
    @CacheEvict(value = "reviews", allEntries = true)
    public void showReview(String reviewId, String userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        
        if (!review.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only show your own reviews");
        }
        
        review.setIsVisible(true);
        reviewRepository.save(review);
    }
}

