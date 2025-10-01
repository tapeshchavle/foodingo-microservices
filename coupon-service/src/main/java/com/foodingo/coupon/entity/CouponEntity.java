package com.foodingo.coupon.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodingo.coupon.enums.CouponStatus;
import com.foodingo.coupon.enums.CouponType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "coupons")
public class CouponEntity {
    
    @Id
    private String id;
    
    private String code;
    
    private String name;
    
    private String description;
    
    private CouponType type;
    
    private CouponStatus status;
    
    private Double discountValue; // Percentage or fixed amount
    
    private Double minimumOrderAmount;
    
    private Double maximumDiscountAmount; // For percentage coupons
    
    private Integer usageLimit; // Total usage limit
    
    private Integer usageCount; // Current usage count
    
    private Integer usageLimitPerUser; // Per user usage limit
    
    private List<String> applicableRestaurantIds; // Empty means all restaurants
    
    private List<String> applicableFoodIds; // Empty means all foods
    
    private List<String> applicableUserIds; // Empty means all users
    
    private LocalDateTime validFrom;
    
    private LocalDateTime validUntil;
    
    private String termsAndConditions;
    
    private String imageUrl;
    
    private String createdBy; // Admin user ID
    
    @Builder.Default
    private boolean isActive = true;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

