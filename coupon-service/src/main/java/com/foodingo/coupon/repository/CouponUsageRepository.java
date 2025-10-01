package com.foodingo.coupon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodingo.coupon.entity.CouponUsageEntity;

@Repository
public interface CouponUsageRepository extends MongoRepository<CouponUsageEntity, String> {
    
    List<CouponUsageEntity> findByCouponId(String couponId);
    
    List<CouponUsageEntity> findByUserId(String userId);
    
    List<CouponUsageEntity> findByOrderId(String orderId);
    
    List<CouponUsageEntity> findByRestaurantId(String restaurantId);
    
    List<CouponUsageEntity> findByCouponIdAndUserId(String couponId, String userId);
    
    long countByCouponId(String couponId);
    
    long countByUserId(String userId);
    
    long countByCouponIdAndUserId(String couponId, String userId);
}

