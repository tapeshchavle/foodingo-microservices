package com.foodingo.coupon.dto;

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
public class CouponValidationRequest {
    
    @NotBlank(message = "Coupon code is required")
    private String couponCode;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Order amount is required")
    @Positive(message = "Order amount must be positive")
    private Double orderAmount;
    
    private String restaurantId;
    
    private List<String> foodIds;
}

