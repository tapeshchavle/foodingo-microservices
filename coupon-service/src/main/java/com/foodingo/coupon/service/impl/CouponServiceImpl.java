package com.foodingo.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.coupon.dto.CouponRequest;
import com.foodingo.coupon.dto.CouponResponse;
import com.foodingo.coupon.dto.CouponValidationRequest;
import com.foodingo.coupon.dto.CouponValidationResponse;
import com.foodingo.coupon.entity.CouponEntity;
import com.foodingo.coupon.entity.CouponUsageEntity;
import com.foodingo.coupon.enums.CouponStatus;
import com.foodingo.coupon.enums.CouponType;
import com.foodingo.coupon.repository.CouponRepository;
import com.foodingo.coupon.repository.CouponUsageRepository;
import com.foodingo.coupon.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
    
    @Autowired
    private CouponRepository couponRepository;
    
    @Autowired
    private CouponUsageRepository couponUsageRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public CouponResponse createCoupon(CouponRequest request, String createdBy) {
        // Check if coupon code already exists
        if (couponRepository.findByCode(request.getCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon code already exists");
        }
        
        CouponEntity coupon = modelMapper.map(request, CouponEntity.class);
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setUsageCount(0);
        coupon.setCreatedBy(createdBy);
        coupon.setIsActive(true);
        coupon.setCreatedAt(LocalDateTime.now());
        
        CouponEntity savedCoupon = couponRepository.save(coupon);
        return modelMapper.map(savedCoupon, CouponResponse.class);
    }
    
    @Override
    public CouponResponse updateCoupon(String couponId, CouponRequest request) {
        CouponEntity coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        
        // Check if new code conflicts with existing coupons
        if (!coupon.getCode().equals(request.getCode()) && 
            couponRepository.findByCode(request.getCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon code already exists");
        }
        
        modelMapper.map(request, coupon);
        coupon.setUpdatedAt(LocalDateTime.now());
        
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        return modelMapper.map(updatedCoupon, CouponResponse.class);
    }
    
    @Override
    public CouponResponse getCouponById(String couponId) {
        CouponEntity coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        return modelMapper.map(coupon, CouponResponse.class);
    }
    
    @Override
    public CouponResponse getCouponByCode(String code) {
        CouponEntity coupon = couponRepository.findByCode(code)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        return modelMapper.map(coupon, CouponResponse.class);
    }
    
    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CouponResponse> getActiveCoupons() {
        return couponRepository.findByIsActiveTrue().stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CouponResponse> getCouponsByStatus(CouponStatus status) {
        return couponRepository.findByStatus(status).stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CouponResponse> getCouponsByType(CouponType type) {
        return couponRepository.findByType(type).stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CouponResponse> getAvailableCoupons() {
        return couponRepository.findAvailableCoupons(LocalDateTime.now()).stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CouponResponse> getCouponsByRestaurant(String restaurantId) {
        return couponRepository.findCouponsByRestaurant(LocalDateTime.now(), restaurantId).stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CouponResponse> getCouponsByUser(String userId) {
        return couponRepository.findCouponsByUser(LocalDateTime.now(), userId).stream()
            .map(coupon -> modelMapper.map(coupon, CouponResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public CouponValidationResponse validateCoupon(CouponValidationRequest request) {
        CouponEntity coupon = couponRepository.findActiveCouponByCode(request.getCouponCode(), LocalDateTime.now())
            .orElse(null);
        
        if (coupon == null) {
            return CouponValidationResponse.builder()
                .isValid(false)
                .message("Invalid or expired coupon")
                .build();
        }
        
        // Check minimum order amount
        if (coupon.getMinimumOrderAmount() != null && 
            request.getOrderAmount() < coupon.getMinimumOrderAmount()) {
            return CouponValidationResponse.builder()
                .isValid(false)
                .message("Minimum order amount not met")
                .build();
        }
        
        // Check usage limit
        if (coupon.getUsageLimit() != null && 
            coupon.getUsageCount() >= coupon.getUsageLimit()) {
            return CouponValidationResponse.builder()
                .isValid(false)
                .message("Coupon usage limit exceeded")
                .build();
        }
        
        // Check per user usage limit
        if (coupon.getUsageLimitPerUser() != null) {
            long userUsageCount = couponUsageRepository.countByCouponIdAndUserId(coupon.getId(), request.getUserId());
            if (userUsageCount >= coupon.getUsageLimitPerUser()) {
                return CouponValidationResponse.builder()
                    .isValid(false)
                    .message("You have already used this coupon maximum times")
                    .build();
            }
        }
        
        // Check restaurant applicability
        if (coupon.getApplicableRestaurantIds() != null && 
            !coupon.getApplicableRestaurantIds().isEmpty() &&
            !coupon.getApplicableRestaurantIds().contains(request.getRestaurantId())) {
            return CouponValidationResponse.builder()
                .isValid(false)
                .message("Coupon not applicable for this restaurant")
                .build();
        }
        
        // Check user applicability
        if (coupon.getApplicableUserIds() != null && 
            !coupon.getApplicableUserIds().isEmpty() &&
            !coupon.getApplicableUserIds().contains(request.getUserId())) {
            return CouponValidationResponse.builder()
                .isValid(false)
                .message("Coupon not applicable for this user")
                .build();
        }
        
        // Calculate discount amount
        Double discountAmount = calculateDiscountAmount(coupon, request.getOrderAmount());
        Double finalAmount = request.getOrderAmount() - discountAmount;
        
        return CouponValidationResponse.builder()
            .isValid(true)
            .message("Coupon is valid")
            .discountAmount(discountAmount)
            .finalAmount(finalAmount)
            .coupon(modelMapper.map(coupon, CouponResponse.class))
            .build();
    }
    
    @Override
    public CouponResponse useCoupon(String couponCode, String userId, String orderId, String restaurantId, Double orderAmount) {
        CouponEntity coupon = couponRepository.findActiveCouponByCode(couponCode, LocalDateTime.now())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        
        // Validate coupon usage
        CouponValidationRequest validationRequest = CouponValidationRequest.builder()
            .couponCode(couponCode)
            .userId(userId)
            .orderAmount(orderAmount)
            .restaurantId(restaurantId)
            .build();
        
        CouponValidationResponse validation = validateCoupon(validationRequest);
        if (!validation.getIsValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validation.getMessage());
        }
        
        // Record coupon usage
        CouponUsageEntity usage = CouponUsageEntity.builder()
            .couponId(coupon.getId())
            .userId(userId)
            .orderId(orderId)
            .restaurantId(restaurantId)
            .orderAmount(orderAmount)
            .discountAmount(validation.getDiscountAmount())
            .usedAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();
        
        couponUsageRepository.save(usage);
        
        // Update coupon usage count
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        if (coupon.getUsageLimit() != null && coupon.getUsageCount() >= coupon.getUsageLimit()) {
            coupon.setStatus(CouponStatus.USED_UP);
        }
        
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        return modelMapper.map(updatedCoupon, CouponResponse.class);
    }
    
    @Override
    public void expireCoupons() {
        List<CouponEntity> expiredCoupons = couponRepository
            .findByValidFromLessThanEqualAndValidUntilGreaterThanEqualAndStatus(
                LocalDateTime.now(), LocalDateTime.now(), CouponStatus.ACTIVE);
        
        for (CouponEntity coupon : expiredCoupons) {
            coupon.setStatus(CouponStatus.EXPIRED);
            coupon.setUpdatedAt(LocalDateTime.now());
        }
        
        couponRepository.saveAll(expiredCoupons);
    }
    
    @Override
    public CouponResponse activateCoupon(String couponId) {
        CouponEntity coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setIsActive(true);
        coupon.setUpdatedAt(LocalDateTime.now());
        
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        return modelMapper.map(updatedCoupon, CouponResponse.class);
    }
    
    @Override
    public CouponResponse deactivateCoupon(String couponId) {
        CouponEntity coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        
        coupon.setStatus(CouponStatus.INACTIVE);
        coupon.setIsActive(false);
        coupon.setUpdatedAt(LocalDateTime.now());
        
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        return modelMapper.map(updatedCoupon, CouponResponse.class);
    }
    
    @Override
    public void deleteCoupon(String couponId) {
        couponRepository.deleteById(couponId);
    }
    
    @Override
    public List<CouponResponse> searchCoupons(String searchTerm) {
        // TODO: Implement search functionality
        return getAllCoupons();
    }
    
    @Override
    public CouponResponse updateCouponStatus(String couponId, CouponStatus status) {
        CouponEntity coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        
        coupon.setStatus(status);
        coupon.setUpdatedAt(LocalDateTime.now());
        
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        return modelMapper.map(updatedCoupon, CouponResponse.class);
    }
    
    private Double calculateDiscountAmount(CouponEntity coupon, Double orderAmount) {
        switch (coupon.getType()) {
            case PERCENTAGE:
                Double discount = orderAmount * (coupon.getDiscountValue() / 100.0);
                if (coupon.getMaximumDiscountAmount() != null) {
                    discount = Math.min(discount, coupon.getMaximumDiscountAmount());
                }
                return discount;
            case FIXED_AMOUNT:
                return Math.min(coupon.getDiscountValue(), orderAmount);
            case FREE_DELIVERY:
                return 0.0; // Free delivery is handled separately
            case BUY_ONE_GET_ONE:
                return orderAmount / 2.0; // 50% discount
            case MINIMUM_ORDER:
                return orderAmount >= coupon.getMinimumOrderAmount() ? coupon.getDiscountValue() : 0.0;
            default:
                return 0.0;
        }
    }
}

