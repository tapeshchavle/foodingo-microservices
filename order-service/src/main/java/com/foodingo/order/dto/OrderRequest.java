package com.foodingo.order.dto;

import java.util.List;

import com.foodingo.order.enums.OrderStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class OrderRequest {
    
    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItem> orderedItems;
    
    @NotBlank(message = "Delivery address is required")
    private String userAddress;
    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    private Double subtotal;
    private Double deliveryCharges;
    private Double taxAmount;
    private Double discountAmount;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    private String couponCode;
    
    private String specialInstructions;
    
    private String deliveryNotes;
    
    private Integer loyaltyPointsUsed;
    
    private OrderStatus orderStatus;
}

