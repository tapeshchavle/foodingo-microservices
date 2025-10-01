package com.foodingo.payment.service;

import java.util.List;

import com.foodingo.payment.dto.PaymentRequest;
import com.foodingo.payment.dto.PaymentResponse;
import com.foodingo.payment.dto.PaymentVerificationRequest;
import com.foodingo.payment.dto.RefundRequest;
import com.foodingo.payment.enums.PaymentStatus;
import com.razorpay.RazorpayException;

public interface PaymentService {
    
    PaymentResponse createPayment(PaymentRequest request) throws RazorpayException;
    
    PaymentResponse verifyPayment(PaymentVerificationRequest request) throws RazorpayException;
    
    PaymentResponse getPaymentById(String paymentId);
    
    PaymentResponse getPaymentByOrderId(String orderId);
    
    List<PaymentResponse> getUserPayments(String userId);
    
    List<PaymentResponse> getRestaurantPayments(String restaurantId);
    
    List<PaymentResponse> getPaymentsByStatus(PaymentStatus status);
    
    PaymentResponse updatePaymentStatus(String paymentId, PaymentStatus status);
    
    PaymentResponse processRefund(RefundRequest request) throws RazorpayException;
    
    PaymentResponse getRefundStatus(String paymentId);
    
    boolean verifyWebhookSignature(String payload, String signature);
    
    void handleWebhookEvent(String eventType, String payload);
}

