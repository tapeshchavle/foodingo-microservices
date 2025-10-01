package com.foodingo.coupon.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "coupon_usage")
public class CouponUsageEntity {
    
    @Id
    private String id;
    
    private String couponId;
    
    private String userId;
    
    private String orderId;
    
    private String restaurantId;
    
    private Double orderAmount;
    
    private Double discountAmount;
    
    private LocalDateTime usedAt;
    
    @CreatedDate
    private LocalDateTime createdAt;
}

