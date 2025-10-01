package com.foodingo.payment.dto;

import java.time.LocalDateTime;

import com.foodingo.payment.enums.PaymentMethod;
import com.foodingo.payment.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String orderId;
    private String userId;
    private String restaurantId;
    private Double amount;
    private String currency;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String description;
    private String receipt;
    private String notes;
    private String failureReason;
    private LocalDateTime paidAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;
    private Double refundAmount;
    private String refundReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

