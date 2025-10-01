package com.foodingo.loyalty.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.loyalty.dto.EarnPointsRequest;
import com.foodingo.loyalty.dto.LoyaltyBalanceResponse;
import com.foodingo.loyalty.dto.LoyaltyTransactionResponse;
import com.foodingo.loyalty.dto.RedeemPointsRequest;
import com.foodingo.loyalty.service.LoyaltyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/loyalty")
@Tag(name = "Loyalty", description = "Loyalty points management APIs")
public class LoyaltyController {
    
    @Autowired
    private LoyaltyService loyaltyService;
    
    @GetMapping("/balance")
    @Operation(summary = "Get user loyalty balance")
    public ResponseEntity<LoyaltyBalanceResponse> getUserBalance(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        LoyaltyBalanceResponse balance = loyaltyService.getUserBalance(currentUserId);
        return ResponseEntity.ok(balance);
    }
    
    @PostMapping("/earn")
    @Operation(summary = "Earn loyalty points")
    public ResponseEntity<LoyaltyTransactionResponse> earnPoints(
            @Valid @RequestBody EarnPointsRequest request) {
        
        LoyaltyTransactionResponse response = loyaltyService.earnPoints(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/redeem")
    @Operation(summary = "Redeem loyalty points")
    public ResponseEntity<LoyaltyTransactionResponse> redeemPoints(
            @Valid @RequestBody RedeemPointsRequest request) {
        
        LoyaltyTransactionResponse response = loyaltyService.redeemPoints(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/transactions")
    @Operation(summary = "Get user loyalty transactions")
    public ResponseEntity<List<LoyaltyTransactionResponse>> getUserTransactions(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<LoyaltyTransactionResponse> transactions = loyaltyService.getUserTransactions(currentUserId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/order/{orderId}")
    @Operation(summary = "Get loyalty transactions for order")
    public ResponseEntity<List<LoyaltyTransactionResponse>> getOrderTransactions(@PathVariable String orderId) {
        List<LoyaltyTransactionResponse> transactions = loyaltyService.getOrderTransactions(orderId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/transactions/{transactionId}")
    @Operation(summary = "Get loyalty transaction by ID")
    public ResponseEntity<LoyaltyTransactionResponse> getTransactionById(@PathVariable String transactionId) {
        LoyaltyTransactionResponse transaction = loyaltyService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }
    
    @PostMapping("/expire")
    @Operation(summary = "Expire user points")
    public ResponseEntity<Void> expirePoints(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        loyaltyService.expirePoints(currentUserId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/adjust")
    @Operation(summary = "Adjust user points")
    public ResponseEntity<Void> adjustPoints(
            @RequestParam Integer points,
            @RequestParam String reason,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        loyaltyService.adjustPoints(currentUserId, points, reason);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reverse/{transactionId}")
    @Operation(summary = "Reverse loyalty transaction")
    public ResponseEntity<LoyaltyTransactionResponse> reverseTransaction(
            @PathVariable String transactionId,
            @RequestParam String reason) {
        
        LoyaltyTransactionResponse response = loyaltyService.reverseTransaction(transactionId, reason);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/calculate/earn")
    @Operation(summary = "Calculate earnable points for order amount")
    public ResponseEntity<Integer> calculateEarnablePoints(@RequestParam Double orderAmount) {
        Integer points = loyaltyService.calculateEarnablePoints(orderAmount);
        return ResponseEntity.ok(points);
    }
    
    @GetMapping("/calculate/redeem")
    @Operation(summary = "Calculate redeemable points for user and order")
    public ResponseEntity<Integer> calculateRedeemablePoints(
            @RequestParam Double orderAmount,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        Integer points = loyaltyService.calculateRedeemablePoints(currentUserId, orderAmount);
        return ResponseEntity.ok(points);
    }
    
    @GetMapping("/calculate/value")
    @Operation(summary = "Calculate monetary value of points")
    public ResponseEntity<Double> calculatePointsValue(@RequestParam Integer points) {
        Double value = loyaltyService.calculatePointsValue(points);
        return ResponseEntity.ok(value);
    }
    
    @GetMapping("/tier")
    @Operation(summary = "Get user loyalty tier")
    public ResponseEntity<String> getUserTier(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        String tier = loyaltyService.getUserTier(currentUserId);
        return ResponseEntity.ok(tier);
    }
    
    @GetMapping("/next-tier")
    @Operation(summary = "Get points needed for next tier")
    public ResponseEntity<Integer> getNextTierPoints(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        Integer points = loyaltyService.getNextTierPoints(currentUserId);
        return ResponseEntity.ok(points);
    }
    
    @PostMapping("/order/complete")
    @Operation(summary = "Process order completion for loyalty points")
    public ResponseEntity<Void> processOrderCompletion(
            @RequestParam String orderId,
            @RequestParam Double orderAmount,
            @RequestParam(required = false) String restaurantId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        loyaltyService.processOrderCompletion(currentUserId, orderId, orderAmount, restaurantId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/order/cancel")
    @Operation(summary = "Process order cancellation for loyalty points")
    public ResponseEntity<Void> processOrderCancellation(
            @RequestParam String orderId,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        loyaltyService.processOrderCancellation(currentUserId, orderId);
        return ResponseEntity.ok().build();
    }
}

