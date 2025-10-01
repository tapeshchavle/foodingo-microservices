package com.foodingo.coupon.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.foodingo.coupon.enums.CouponType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest {
    
    @NotBlank(message = "Coupon code is required")
    private String code;
    
    @NotBlank(message = "Coupon name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Coupon type is required")
    private CouponType type;
    
    @NotNull(message = "Discount value is required")
    @Positive(message = "Discount value must be positive")
    private Double discountValue;
    
    private Double minimumOrderAmount;
    
    private Double maximumDiscountAmount;
    
    private Integer usageLimit;
    
    private Integer usageLimitPerUser;
    
    private List<String> applicableRestaurantIds;
    
    private List<String> applicableFoodIds;
    
    private List<String> applicableUserIds;
    
    private LocalDateTime validFrom;
    
    private LocalDateTime validUntil;
    
    private String termsAndConditions;
    
    private String imageUrl;
}

