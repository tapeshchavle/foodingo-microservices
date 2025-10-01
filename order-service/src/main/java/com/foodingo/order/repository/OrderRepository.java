package com.foodingo.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodingo.order.entity.OrderEntity;
import com.foodingo.order.enums.OrderStatus;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    
    Optional<OrderEntity> findByRazorpayOrderId(String razorpayOrderId);
    
    List<OrderEntity> findByUserId(String userId);
    
    List<OrderEntity> findByRestaurantId(String restaurantId);
    
    List<OrderEntity> findByOrderStatus(OrderStatus status);
    
    List<OrderEntity> findByUserIdAndOrderStatus(String userId, OrderStatus status);
    
    List<OrderEntity> findByRestaurantIdAndOrderStatus(String restaurantId, OrderStatus status);
    
    long countByRestaurantId(String restaurantId);
    
    long countByUserId(String userId);
}

