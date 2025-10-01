package com.foodingo.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodingo.payment.entity.PaymentEntity;
import com.foodingo.payment.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {
    
    Optional<PaymentEntity> findByRazorpayOrderId(String razorpayOrderId);
    
    Optional<PaymentEntity> findByRazorpayPaymentId(String razorpayPaymentId);
    
    List<PaymentEntity> findByOrderId(String orderId);
    
    List<PaymentEntity> findByUserId(String userId);
    
    List<PaymentEntity> findByRestaurantId(String restaurantId);
    
    List<PaymentEntity> findByStatus(PaymentStatus status);
    
    List<PaymentEntity> findByUserIdAndStatus(String userId, PaymentStatus status);
    
    List<PaymentEntity> findByRestaurantIdAndStatus(String restaurantId, PaymentStatus status);
    
    long countByUserId(String userId);
    
    long countByRestaurantId(String restaurantId);
    
    long countByStatus(PaymentStatus status);
}

