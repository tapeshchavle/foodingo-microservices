package com.foodingo.loyalty.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "loyalty_transactions")
public class LoyaltyTransactionEntity {
    
    @Id
    private String id;
    
    private String userId;
    
    private String orderId; // Optional - for order-related transactions
    
    private String restaurantId; // Optional
    
    private Integer points; // Positive for earned, negative for redeemed
    
    private String transactionType; // EARNED, REDEEMED, EXPIRED, ADJUSTED
    
    private String description;
    
    private Double orderAmount; // Order amount for earning calculation
    
    private Double pointsValue; // Monetary value of points
    
    private LocalDateTime expiresAt; // When points expire
    
    @Builder.Default
    private boolean isActive = true;
    
    private String referenceId; // External reference
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

