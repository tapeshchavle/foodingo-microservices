package com.foodingo.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.foodingo.order.enums.OrderStatus;
import com.foodingo.order.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String userId;
    private String restaurantId;
    private String userAddress;
    private String phoneNumber;
    private String email;
    private List<OrderItem> orderedItems;
    private double subtotal;
    private double deliveryCharges;
    private double taxAmount;
    private double discountAmount;
    private double amount;
    private String couponCode;
    private PaymentStatus paymentStatus;
    private String razorpayOrderId;
    private OrderStatus orderStatus;
    private String deliveryPartnerId;
    private String specialInstructions;
    private String deliveryNotes;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private Integer preparationTime;
    private Integer loyaltyPointsEarned;
    private Integer loyaltyPointsUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

