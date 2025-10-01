package com.foodingo.payment.dto;

import com.foodingo.payment.enums.PaymentMethod;

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
public class PaymentRequest {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    private String currency = "INR";
    
    private PaymentMethod paymentMethod = PaymentMethod.RAZORPAY;
    
    private String description;
    
    private String receipt;
    
    private String notes;
}

