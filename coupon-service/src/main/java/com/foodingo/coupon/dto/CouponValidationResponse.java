package com.foodingo.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponValidationResponse {
    private boolean isValid;
    private String message;
    private Double discountAmount;
    private Double finalAmount;
    private CouponResponse coupon;
}

