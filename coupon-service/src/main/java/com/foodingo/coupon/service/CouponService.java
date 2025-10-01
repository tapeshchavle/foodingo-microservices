package com.foodingo.coupon.service;

import java.util.List;

import com.foodingo.coupon.dto.CouponRequest;
import com.foodingo.coupon.dto.CouponResponse;
import com.foodingo.coupon.dto.CouponValidationRequest;
import com.foodingo.coupon.dto.CouponValidationResponse;
import com.foodingo.coupon.enums.CouponStatus;
import com.foodingo.coupon.enums.CouponType;

public interface CouponService {
    
    CouponResponse createCoupon(CouponRequest request, String createdBy);
    
    CouponResponse updateCoupon(String couponId, CouponRequest request);
    
    CouponResponse getCouponById(String couponId);
    
    CouponResponse getCouponByCode(String code);
    
    List<CouponResponse> getAllCoupons();
    
    List<CouponResponse> getActiveCoupons();
    
    List<CouponResponse> getCouponsByStatus(CouponStatus status);
    
    List<CouponResponse> getCouponsByType(CouponType type);
    
    List<CouponResponse> getAvailableCoupons();
    
    List<CouponResponse> getCouponsByRestaurant(String restaurantId);
    
    List<CouponResponse> getCouponsByUser(String userId);
    
    CouponValidationResponse validateCoupon(CouponValidationRequest request);
    
    CouponResponse useCoupon(String couponCode, String userId, String orderId, String restaurantId, Double orderAmount);
    
    void expireCoupons();
    
    CouponResponse activateCoupon(String couponId);
    
    CouponResponse deactivateCoupon(String couponId);
    
    void deleteCoupon(String couponId);
    
    List<CouponResponse> searchCoupons(String searchTerm);
    
    CouponResponse updateCouponStatus(String couponId, CouponStatus status);
}

