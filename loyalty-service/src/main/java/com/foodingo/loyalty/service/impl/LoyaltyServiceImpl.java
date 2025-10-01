package com.foodingo.loyalty.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.loyalty.dto.EarnPointsRequest;
import com.foodingo.loyalty.dto.LoyaltyBalanceResponse;
import com.foodingo.loyalty.dto.LoyaltyTransactionResponse;
import com.foodingo.loyalty.dto.RedeemPointsRequest;
import com.foodingo.loyalty.entity.LoyaltyTransactionEntity;
import com.foodingo.loyalty.repository.LoyaltyTransactionRepository;
import com.foodingo.loyalty.service.LoyaltyService;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
    
    @Autowired
    private LoyaltyTransactionRepository loyaltyTransactionRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Value("${loyalty.points.earn-rate:1.0}")
    private Double earnRate;
    
    @Value("${loyalty.points.redeem-rate:0.01}")
    private Double redeemRate;
    
    @Value("${loyalty.points.min-redeem:100}")
    private Integer minRedeem;
    
    @Value("${loyalty.points.max-redeem-percentage:50}")
    private Integer maxRedeemPercentage;
    
    @Override
    public LoyaltyBalanceResponse getUserBalance(String userId) {
        List<LoyaltyTransactionEntity> transactions = loyaltyTransactionRepository
            .findActivePointsByUserId(userId, LocalDateTime.now());
        
        int totalPoints = transactions.stream()
            .mapToInt(LoyaltyTransactionEntity::getPoints)
            .sum();
        
        int availablePoints = Math.max(0, totalPoints);
        int pendingPoints = 0; // TODO: Calculate pending points
        int expiredPoints = 0; // TODO: Calculate expired points
        
        String tier = getUserTier(userId);
        Integer nextTierPoints = getNextTierPoints(userId);
        
        List<LoyaltyTransactionResponse> recentTransactions = loyaltyTransactionRepository
            .findByUserId(userId).stream()
            .limit(5)
            .map(transaction -> modelMapper.map(transaction, LoyaltyTransactionResponse.class))
            .collect(Collectors.toList());
        
        return LoyaltyBalanceResponse.builder()
            .userId(userId)
            .totalPoints(totalPoints)
            .availablePoints(availablePoints)
            .pendingPoints(pendingPoints)
            .expiredPoints(expiredPoints)
            .pointsValue(calculatePointsValue(availablePoints))
            .tier(tier)
            .nextTierPoints(nextTierPoints)
            .recentTransactions(recentTransactions)
            .lastUpdated(LocalDateTime.now())
            .build();
    }
    
    @Override
    public LoyaltyTransactionResponse earnPoints(EarnPointsRequest request) {
        Integer points = calculateEarnablePoints(request.getOrderAmount());
        
        LoyaltyTransactionEntity transaction = LoyaltyTransactionEntity.builder()
            .userId(request.getUserId())
            .orderId(request.getOrderId())
            .restaurantId(request.getRestaurantId())
            .points(points)
            .transactionType("EARNED")
            .description(request.getDescription() != null ? request.getDescription() : 
                "Earned " + points + " points for order #" + request.getOrderId())
            .orderAmount(request.getOrderAmount())
            .pointsValue(calculatePointsValue(points))
            .expiresAt(LocalDateTime.now().plusYears(1)) // Points expire in 1 year
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        LoyaltyTransactionEntity savedTransaction = loyaltyTransactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, LoyaltyTransactionResponse.class);
    }
    
    @Override
    public LoyaltyTransactionResponse redeemPoints(RedeemPointsRequest request) {
        LoyaltyBalanceResponse balance = getUserBalance(request.getUserId());
        
        if (balance.getAvailablePoints() < request.getPointsToRedeem()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Insufficient points. Available: " + balance.getAvailablePoints());
        }
        
        if (request.getPointsToRedeem() < minRedeem) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Minimum " + minRedeem + " points required for redemption");
        }
        
        // Check if redemption amount doesn't exceed max percentage of order
        Double maxRedeemAmount = request.getOrderAmount() * maxRedeemPercentage / 100.0;
        Double redeemAmount = calculatePointsValue(request.getPointsToRedeem());
        
        if (redeemAmount > maxRedeemAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Redemption amount cannot exceed " + maxRedeemPercentage + "% of order amount");
        }
        
        LoyaltyTransactionEntity transaction = LoyaltyTransactionEntity.builder()
            .userId(request.getUserId())
            .orderId(request.getOrderId())
            .restaurantId(request.getRestaurantId())
            .points(-request.getPointsToRedeem()) // Negative for redemption
            .transactionType("REDEEMED")
            .description(request.getDescription() != null ? request.getDescription() : 
                "Redeemed " + request.getPointsToRedeem() + " points for order #" + request.getOrderId())
            .orderAmount(request.getOrderAmount())
            .pointsValue(calculatePointsValue(request.getPointsToRedeem()))
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        LoyaltyTransactionEntity savedTransaction = loyaltyTransactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, LoyaltyTransactionResponse.class);
    }
    
    @Override
    public List<LoyaltyTransactionResponse> getUserTransactions(String userId) {
        return loyaltyTransactionRepository.findByUserId(userId).stream()
            .map(transaction -> modelMapper.map(transaction, LoyaltyTransactionResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<LoyaltyTransactionResponse> getOrderTransactions(String orderId) {
        return loyaltyTransactionRepository.findByOrderId(orderId).stream()
            .map(transaction -> modelMapper.map(transaction, LoyaltyTransactionResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public LoyaltyTransactionResponse getTransactionById(String transactionId) {
        LoyaltyTransactionEntity transaction = loyaltyTransactionRepository.findById(transactionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        return modelMapper.map(transaction, LoyaltyTransactionResponse.class);
    }
    
    @Override
    public void expirePoints(String userId) {
        List<LoyaltyTransactionEntity> expiredTransactions = loyaltyTransactionRepository
            .findExpiredPointsByUserId(userId, LocalDateTime.now());
        
        for (LoyaltyTransactionEntity transaction : expiredTransactions) {
            transaction.setIsActive(false);
            transaction.setUpdatedAt(LocalDateTime.now());
        }
        
        loyaltyTransactionRepository.saveAll(expiredTransactions);
    }
    
    @Override
    public void adjustPoints(String userId, Integer points, String reason) {
        LoyaltyTransactionEntity transaction = LoyaltyTransactionEntity.builder()
            .userId(userId)
            .points(points)
            .transactionType("ADJUSTED")
            .description(reason)
            .pointsValue(calculatePointsValue(Math.abs(points)))
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        loyaltyTransactionRepository.save(transaction);
    }
    
    @Override
    public LoyaltyTransactionResponse reverseTransaction(String transactionId, String reason) {
        LoyaltyTransactionEntity originalTransaction = loyaltyTransactionRepository.findById(transactionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        
        LoyaltyTransactionEntity reversalTransaction = LoyaltyTransactionEntity.builder()
            .userId(originalTransaction.getUserId())
            .orderId(originalTransaction.getOrderId())
            .restaurantId(originalTransaction.getRestaurantId())
            .points(-originalTransaction.getPoints()) // Reverse the points
            .transactionType("REVERSED")
            .description(reason)
            .orderAmount(originalTransaction.getOrderAmount())
            .pointsValue(calculatePointsValue(Math.abs(originalTransaction.getPoints())))
            .isActive(true)
            .referenceId(transactionId)
            .createdAt(LocalDateTime.now())
            .build();
        
        LoyaltyTransactionEntity savedTransaction = loyaltyTransactionRepository.save(reversalTransaction);
        return modelMapper.map(savedTransaction, LoyaltyTransactionResponse.class);
    }
    
    @Override
    public Integer calculateEarnablePoints(Double orderAmount) {
        return (int) Math.floor(orderAmount * earnRate);
    }
    
    @Override
    public Integer calculateRedeemablePoints(String userId, Double orderAmount) {
        LoyaltyBalanceResponse balance = getUserBalance(userId);
        Double maxRedeemAmount = orderAmount * maxRedeemPercentage / 100.0;
        Integer maxRedeemPoints = (int) Math.floor(maxRedeemAmount / redeemRate);
        
        return Math.min(balance.getAvailablePoints(), maxRedeemPoints);
    }
    
    @Override
    public Double calculatePointsValue(Integer points) {
        return points * redeemRate;
    }
    
    @Override
    public String getUserTier(String userId) {
        LoyaltyBalanceResponse balance = getUserBalance(userId);
        int totalPoints = balance.getTotalPoints();
        
        if (totalPoints >= 10000) return "GOLD";
        if (totalPoints >= 5000) return "SILVER";
        if (totalPoints >= 1000) return "BRONZE";
        return "BRONZE";
    }
    
    @Override
    public Integer getNextTierPoints(String userId) {
        LoyaltyBalanceResponse balance = getUserBalance(userId);
        String currentTier = balance.getTier();
        
        switch (currentTier) {
            case "BRONZE":
                return 1000 - balance.getTotalPoints();
            case "SILVER":
                return 5000 - balance.getTotalPoints();
            case "GOLD":
                return 10000 - balance.getTotalPoints();
            default:
                return 1000;
        }
    }
    
    @Override
    public void processOrderCompletion(String userId, String orderId, Double orderAmount, String restaurantId) {
        EarnPointsRequest request = EarnPointsRequest.builder()
            .userId(userId)
            .orderId(orderId)
            .orderAmount(orderAmount)
            .restaurantId(restaurantId)
            .description("Points earned for completed order")
            .build();
        
        earnPoints(request);
    }
    
    @Override
    public void processOrderCancellation(String userId, String orderId) {
        List<LoyaltyTransactionEntity> orderTransactions = loyaltyTransactionRepository.findByOrderId(orderId);
        
        for (LoyaltyTransactionEntity transaction : orderTransactions) {
            if (transaction.getIsActive()) {
                reverseTransaction(transaction.getId(), "Order cancelled");
            }
        }
    }
}

