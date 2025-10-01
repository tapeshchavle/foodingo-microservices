package com.foodingo.loyalty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarnPointsRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotNull(message = "Order amount is required")
    @Positive(message = "Order amount must be positive")
    private Double orderAmount;
    
    private String restaurantId;
    
    private String description;
}

