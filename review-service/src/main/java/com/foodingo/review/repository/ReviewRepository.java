package com.foodingo.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodingo.review.entity.ReviewEntity;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
    
    List<ReviewEntity> findByRestaurantId(String restaurantId);
    
    List<ReviewEntity> findByFoodId(String foodId);
    
    List<ReviewEntity> findByUserId(String userId);
    
    List<ReviewEntity> findByOrderId(String orderId);
    
    List<ReviewEntity> findByRestaurantIdAndIsVisibleTrue(String restaurantId);
    
    List<ReviewEntity> findByFoodIdAndIsVisibleTrue(String foodId);
    
    List<ReviewEntity> findByUserIdAndIsVisibleTrue(String userId);
    
    List<ReviewEntity> findByRestaurantIdAndRating(String restaurantId, Integer rating);
    
    List<ReviewEntity> findByRestaurantIdAndIsVerifiedTrue(String restaurantId);
    
    List<ReviewEntity> findByRestaurantIdAndIsHelpfulTrue(String restaurantId);
    
    Page<ReviewEntity> findByRestaurantIdAndIsVisibleTrue(String restaurantId, Pageable pageable);
    
    Page<ReviewEntity> findByFoodIdAndIsVisibleTrue(String foodId, Pageable pageable);
    
    @Query("{'restaurantId': ?0, 'rating': {$gte: ?1}, 'isVisible': true}")
    List<ReviewEntity> findByRestaurantIdAndRatingGreaterThanEqual(String restaurantId, Integer minRating);
    
    @Query("{'restaurantId': ?0, 'rating': {$lte: ?1}, 'isVisible': true}")
    List<ReviewEntity> findByRestaurantIdAndRatingLessThanEqual(String restaurantId, Integer maxRating);
    
    @Query("{'restaurantId': ?0, 'rating': {$gte: ?1, $lte: ?2}, 'isVisible': true}")
    List<ReviewEntity> findByRestaurantIdAndRatingBetween(String restaurantId, Integer minRating, Integer maxRating);
    
    @Query("{'restaurantId': ?0, 'tags': {$in: ?1}, 'isVisible': true}")
    List<ReviewEntity> findByRestaurantIdAndTagsIn(String restaurantId, List<String> tags);
    
    @Query("{'restaurantId': ?0, 'comment': {$regex: ?1, $options: 'i'}, 'isVisible': true}")
    List<ReviewEntity> findByRestaurantIdAndCommentContainingIgnoreCase(String restaurantId, String searchText);
    
    long countByRestaurantId(String restaurantId);
    
    long countByFoodId(String foodId);
    
    long countByUserId(String userId);
    
    long countByRestaurantIdAndIsVisibleTrue(String restaurantId);
    
    long countByRestaurantIdAndIsVerifiedTrue(String restaurantId);
    
    @Query(value = "{'restaurantId': ?0, 'isVisible': true}", fields = "{'rating': 1}")
    List<ReviewEntity> findRatingsByRestaurantId(String restaurantId);
}

