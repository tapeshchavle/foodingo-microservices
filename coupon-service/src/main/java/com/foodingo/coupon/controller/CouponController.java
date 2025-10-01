package com.foodingo.coupon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.coupon.dto.CouponRequest;
import com.foodingo.coupon.dto.CouponResponse;
import com.foodingo.coupon.dto.CouponValidationRequest;
import com.foodingo.coupon.dto.CouponValidationResponse;
import com.foodingo.coupon.enums.CouponStatus;
import com.foodingo.coupon.enums.CouponType;
import com.foodingo.coupon.service.CouponService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
@Tag(name = "Coupon", description = "Coupon management APIs")
public class CouponController {
    
    @Autowired
    private CouponService couponService;
    
    @PostMapping
    @Operation(summary = "Create a new coupon")
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @RequestBody CouponRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String createdBy = userId != null ? userId : "admin";
        CouponResponse response = couponService.createCoupon(request, createdBy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{couponId}")
    @Operation(summary = "Update a coupon")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable String couponId,
            @Valid @RequestBody CouponRequest request) {
        
        CouponResponse response = couponService.updateCoupon(couponId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{couponId}")
    @Operation(summary = "Get coupon by ID")
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable String couponId) {
        CouponResponse coupon = couponService.getCouponById(couponId);
        return ResponseEntity.ok(coupon);
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Get coupon by code")
    public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable String code) {
        CouponResponse coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }
    
    @GetMapping
    @Operation(summary = "Get all coupons")
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active coupons")
    public ResponseEntity<List<CouponResponse>> getActiveCoupons() {
        List<CouponResponse> coupons = couponService.getActiveCoupons();
        return ResponseEntity.ok(coupons);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available coupons")
    public ResponseEntity<List<CouponResponse>> getAvailableCoupons() {
        List<CouponResponse> coupons = couponService.getAvailableCoupons();
        return ResponseEntity.ok(coupons);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get coupons by status")
    public ResponseEntity<List<CouponResponse>> getCouponsByStatus(@PathVariable CouponStatus status) {
        List<CouponResponse> coupons = couponService.getCouponsByStatus(status);
        return ResponseEntity.ok(coupons);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Get coupons by type")
    public ResponseEntity<List<CouponResponse>> getCouponsByType(@PathVariable CouponType type) {
        List<CouponResponse> coupons = couponService.getCouponsByType(type);
        return ResponseEntity.ok(coupons);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get coupons by restaurant")
    public ResponseEntity<List<CouponResponse>> getCouponsByRestaurant(@PathVariable String restaurantId) {
        List<CouponResponse> coupons = couponService.getCouponsByRestaurant(restaurantId);
        return ResponseEntity.ok(coupons);
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get coupons by user")
    public ResponseEntity<List<CouponResponse>> getCouponsByUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<CouponResponse> coupons = couponService.getCouponsByUser(currentUserId);
        return ResponseEntity.ok(coupons);
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate a coupon")
    public ResponseEntity<CouponValidationResponse> validateCoupon(
            @Valid @RequestBody CouponValidationRequest request) {
        
        CouponValidationResponse response = couponService.validateCoupon(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/use")
    @Operation(summary = "Use a coupon")
    public ResponseEntity<CouponResponse> useCoupon(
            @RequestParam String couponCode,
            @RequestParam String orderId,
            @RequestParam(required = false) String restaurantId,
            @RequestParam Double orderAmount,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        CouponResponse response = couponService.useCoupon(couponCode, currentUserId, orderId, restaurantId, orderAmount);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/expire")
    @Operation(summary = "Expire coupons")
    public ResponseEntity<Void> expireCoupons() {
        couponService.expireCoupons();
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{couponId}/activate")
    @Operation(summary = "Activate a coupon")
    public ResponseEntity<CouponResponse> activateCoupon(@PathVariable String couponId) {
        CouponResponse response = couponService.activateCoupon(couponId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{couponId}/deactivate")
    @Operation(summary = "Deactivate a coupon")
    public ResponseEntity<CouponResponse> deactivateCoupon(@PathVariable String couponId) {
        CouponResponse response = couponService.deactivateCoupon(couponId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{couponId}/status")
    @Operation(summary = "Update coupon status")
    public ResponseEntity<CouponResponse> updateCouponStatus(
            @PathVariable String couponId,
            @RequestParam CouponStatus status) {
        
        CouponResponse response = couponService.updateCouponStatus(couponId, status);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search coupons")
    public ResponseEntity<List<CouponResponse>> searchCoupons(@RequestParam String searchTerm) {
        List<CouponResponse> coupons = couponService.searchCoupons(searchTerm);
        return ResponseEntity.ok(coupons);
    }
    
    @DeleteMapping("/{couponId}")
    @Operation(summary = "Delete a coupon")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}

