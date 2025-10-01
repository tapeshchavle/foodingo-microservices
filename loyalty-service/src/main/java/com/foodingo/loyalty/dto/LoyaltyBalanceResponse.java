package com.foodingo.loyalty.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyBalanceResponse {
    private String userId;
    private Integer totalPoints;
    private Integer availablePoints;
    private Integer pendingPoints;
    private Integer expiredPoints;
    private Double pointsValue;
    private String tier;
    private Integer nextTierPoints;
    private List<LoyaltyTransactionResponse> recentTransactions;
    private LocalDateTime lastUpdated;
}

