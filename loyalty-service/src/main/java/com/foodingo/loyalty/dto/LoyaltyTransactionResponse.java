package com.foodingo.loyalty.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTransactionResponse {
    private String id;
    private String userId;
    private String orderId;
    private String restaurantId;
    private Integer points;
    private String transactionType;
    private String description;
    private Double orderAmount;
    private Double pointsValue;
    private LocalDateTime expiresAt;
    private boolean isActive;
    private String referenceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

