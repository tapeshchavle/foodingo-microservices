package com.foodingo.payment.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodingo.payment.enums.PaymentStatus;
import com.foodingo.payment.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class PaymentEntity {
    
    @Id
    private String id;
    
    private String orderId;
    
    private String userId;
    
    private String restaurantId;
    
    private Double amount;
    
    private String currency;
    
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    private PaymentMethod paymentMethod;
    
    // Razorpay fields
    private String razorpayOrderId;
    
    private String razorpayPaymentId;
    
    private String razorpaySignature;
    
    private String razorpayRefundId;
    
    // Additional fields
    private String description;
    
    private String receipt;
    
    private String notes;
    
    private String failureReason;
    
    private LocalDateTime paidAt;
    
    private LocalDateTime failedAt;
    
    private LocalDateTime refundedAt;
    
    private Double refundAmount;
    
    private String refundReason;
    
    private String gatewayTransactionId;
    
    private String gatewayResponse;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

