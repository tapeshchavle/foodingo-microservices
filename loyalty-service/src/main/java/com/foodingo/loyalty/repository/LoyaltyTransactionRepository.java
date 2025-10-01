package com.foodingo.loyalty.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodingo.loyalty.entity.LoyaltyTransactionEntity;

@Repository
public interface LoyaltyTransactionRepository extends MongoRepository<LoyaltyTransactionEntity, String> {
    
    List<LoyaltyTransactionEntity> findByUserId(String userId);
    
    List<LoyaltyTransactionEntity> findByUserIdAndIsActiveTrue(String userId);
    
    List<LoyaltyTransactionEntity> findByOrderId(String orderId);
    
    List<LoyaltyTransactionEntity> findByRestaurantId(String restaurantId);
    
    List<LoyaltyTransactionEntity> findByUserIdAndTransactionType(String userId, String transactionType);
    
    @Query("{'userId': ?0, 'points': {$gt: 0}, 'isActive': true}")
    List<LoyaltyTransactionEntity> findEarnedPointsByUserId(String userId);
    
    @Query("{'userId': ?0, 'points': {$lt: 0}, 'isActive': true}")
    List<LoyaltyTransactionEntity> findRedeemedPointsByUserId(String userId);
    
    @Query("{'userId': ?0, 'expiresAt': {$lt: ?1}, 'isActive': true}")
    List<LoyaltyTransactionEntity> findExpiredPointsByUserId(String userId, LocalDateTime now);
    
    @Query("{'userId': ?0, 'expiresAt': {$gt: ?1}, 'isActive': true}")
    List<LoyaltyTransactionEntity> findActivePointsByUserId(String userId, LocalDateTime now);
    
    @Query(value = "{'userId': ?0, 'isActive': true}", fields = "{'points': 1}")
    List<LoyaltyTransactionEntity> findPointsByUserId(String userId);
    
    long countByUserId(String userId);
    
    long countByUserIdAndTransactionType(String userId, String transactionType);
}

