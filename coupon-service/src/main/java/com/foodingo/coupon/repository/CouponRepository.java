package com.foodingo.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodingo.coupon.entity.CouponEntity;
import com.foodingo.coupon.enums.CouponStatus;
import com.foodingo.coupon.enums.CouponType;

@Repository
public interface CouponRepository extends MongoRepository<CouponEntity, String> {
    
    Optional<CouponEntity> findByCode(String code);
    
    List<CouponEntity> findByStatus(CouponStatus status);
    
    List<CouponEntity> findByType(CouponType type);
    
    List<CouponEntity> findByIsActiveTrue();
    
    List<CouponEntity> findByValidFromLessThanEqualAndValidUntilGreaterThanEqualAndStatus(
        LocalDateTime now, LocalDateTime now2, CouponStatus status);
    
    List<CouponEntity> findByApplicableRestaurantIdsContaining(String restaurantId);
    
    List<CouponEntity> findByApplicableUserIdsContaining(String userId);
    
    @Query("{'code': ?0, 'status': 'ACTIVE', 'isActive': true, 'validFrom': {$lte: ?1}, 'validUntil': {$gte: ?1}}")
    Optional<CouponEntity> findActiveCouponByCode(String code, LocalDateTime now);
    
    @Query("{'status': 'ACTIVE', 'isActive': true, 'validFrom': {$lte: ?1}, 'validUntil': {$gte: ?1}, 'usageCount': {$lt: '$usageLimit'}}")
    List<CouponEntity> findAvailableCoupons(LocalDateTime now);
    
    @Query("{'status': 'ACTIVE', 'isActive': true, 'validFrom': {$lte: ?1}, 'validUntil': {$gte: ?1}, 'applicableRestaurantIds': {$in: [?2]}}")
    List<CouponEntity> findCouponsByRestaurant(LocalDateTime now, String restaurantId);
    
    @Query("{'status': 'ACTIVE', 'isActive': true, 'validFrom': {$lte: ?1}, 'validUntil': {$gte: ?1}, 'applicableUserIds': {$in: [?2]}}")
    List<CouponEntity> findCouponsByUser(LocalDateTime now, String userId);
    
    long countByStatus(CouponStatus status);
    
    long countByType(CouponType type);
}

