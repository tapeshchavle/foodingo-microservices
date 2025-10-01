package com.foodingo.notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodingo.notification.entity.NotificationEntity;
import com.foodingo.notification.enums.NotificationType;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    
    List<NotificationEntity> findByUserId(String userId);
    
    List<NotificationEntity> findByUserIdAndIsReadFalse(String userId);
    
    List<NotificationEntity> findByUserIdAndType(String userId, NotificationType type);
    
    List<NotificationEntity> findByOrderId(String orderId);
    
    List<NotificationEntity> findByRestaurantId(String restaurantId);
    
    List<NotificationEntity> findByScheduledAtLessThanEqualAndSentAtIsNull(LocalDateTime now);
    
    Page<NotificationEntity> findByUserId(String userId, Pageable pageable);
    
    Page<NotificationEntity> findByUserIdAndIsReadFalse(String userId, Pageable pageable);
    
    long countByUserIdAndIsReadFalse(String userId);
    
    long countByUserId(String userId);
    
    long countByType(NotificationType type);
    
    @Query("{'userId': ?0, 'type': ?1, 'isRead': false}")
    long countUnreadByUserIdAndType(String userId, NotificationType type);
    
    @Query("{'userId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<NotificationEntity> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
}

