package com.foodingo.coupon.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class CouponResponse {
    private String id;
    private String code;
    private String name;
    private String description;
    private CouponType type;
    private CouponStatus status;
    private Double discountValue;
    private Double minimumOrderAmount;
    private Double maximumDiscountAmount;
    private Integer usageLimit;
    private Integer usageCount;
    private Integer usageLimitPerUser;
    private List<String> applicableRestaurantIds;
    private List<String> applicableFoodIds;
    private List<String> applicableUserIds;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String termsAndConditions;
    private String imageUrl;
    private String createdBy;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

