package com.foodingo.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodingo.order.dto.OrderItem;
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
@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    
    private String userId;
    
    private String restaurantId;
    
    private String userAddress;
    
    private String phoneNumber;
    
    private String email;
    
    @Builder.Default
    private List<OrderItem> orderedItems = new ArrayList<>();
    
    private double subtotal;
    
    private double deliveryCharges;
    
    private double taxAmount;
    
    private double discountAmount;
    
    private double amount; // total amount
    
    private String couponCode;
    
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private String razorpayOrderId;
    
    private String razorpaySignature;
    
    private String razorpayPaymentId;
    
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;
    
    private String deliveryPartnerId;
    
    private String deliveryNotes;
    
    private String specialInstructions;
    
    private LocalDateTime scheduledDeliveryTime;
    
    private LocalDateTime estimatedDeliveryTime;
    
    private LocalDateTime actualDeliveryTime;
    
    private Integer preparationTime; // in minutes
    
    private String cancellationReason;
    
    private LocalDateTime cancelledAt;
    
    private String cancelledBy; // userId
    
    @Builder.Default
    private Integer loyaltyPointsEarned = 0;
    
    @Builder.Default
    private Integer loyaltyPointsUsed = 0;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Order tracking history
    @Builder.Default
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusHistory {
        private OrderStatus status;
        private LocalDateTime timestamp;
        private String updatedBy;
        private String notes;
    }
}

