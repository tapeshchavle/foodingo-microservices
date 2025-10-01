package com.foodingo.loyalty.service;

import java.util.List;

import com.foodingo.loyalty.dto.EarnPointsRequest;
import com.foodingo.loyalty.dto.LoyaltyBalanceResponse;
import com.foodingo.loyalty.dto.LoyaltyTransactionResponse;
import com.foodingo.loyalty.dto.RedeemPointsRequest;

public interface LoyaltyService {
    
    LoyaltyBalanceResponse getUserBalance(String userId);
    
    LoyaltyTransactionResponse earnPoints(EarnPointsRequest request);
    
    LoyaltyTransactionResponse redeemPoints(RedeemPointsRequest request);
    
    List<LoyaltyTransactionResponse> getUserTransactions(String userId);
    
    List<LoyaltyTransactionResponse> getOrderTransactions(String orderId);
    
    LoyaltyTransactionResponse getTransactionById(String transactionId);
    
    void expirePoints(String userId);
    
    void adjustPoints(String userId, Integer points, String reason);
    
    LoyaltyTransactionResponse reverseTransaction(String transactionId, String reason);
    
    Integer calculateEarnablePoints(Double orderAmount);
    
    Integer calculateRedeemablePoints(String userId, Double orderAmount);
    
    Double calculatePointsValue(Integer points);
    
    String getUserTier(String userId);
    
    Integer getNextTierPoints(String userId);
    
    void processOrderCompletion(String userId, String orderId, Double orderAmount, String restaurantId);
    
    void processOrderCancellation(String userId, String orderId);
}

