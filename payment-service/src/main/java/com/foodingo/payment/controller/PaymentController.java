package com.foodingo.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.payment.dto.PaymentRequest;
import com.foodingo.payment.dto.PaymentResponse;
import com.foodingo.payment.dto.PaymentVerificationRequest;
import com.foodingo.payment.dto.RefundRequest;
import com.foodingo.payment.enums.PaymentStatus;
import com.foodingo.payment.service.PaymentService;
import com.razorpay.RazorpayException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "Payment management APIs")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping
    @Operation(summary = "Create payment")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) throws RazorpayException {
        
        // TODO: Get userId from JWT token via Feign client to User Service
        if (userId != null) {
            request.setUserId(userId);
        }
        
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/verify")
    @Operation(summary = "Verify payment")
    public ResponseEntity<PaymentResponse> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequest request) throws RazorpayException {
        
        PaymentResponse response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get user payments")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<PaymentResponse> payments = paymentService.getUserPayments(currentUserId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get restaurant payments")
    public ResponseEntity<List<PaymentResponse>> getRestaurantPayments(@PathVariable String restaurantId) {
        List<PaymentResponse> payments = paymentService.getRestaurantPayments(restaurantId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam PaymentStatus status) {
        
        PaymentResponse payment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(payment);
    }
    
    @PostMapping("/refund")
    @Operation(summary = "Process refund")
    public ResponseEntity<PaymentResponse> processRefund(
            @Valid @RequestBody RefundRequest request) throws RazorpayException {
        
        PaymentResponse response = paymentService.processRefund(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/refund-status")
    @Operation(summary = "Get refund status")
    public ResponseEntity<PaymentResponse> getRefundStatus(@PathVariable String id) {
        PaymentResponse payment = paymentService.getRefundStatus(id);
        return ResponseEntity.ok(payment);
    }
    
    @PostMapping("/webhook")
    @Operation(summary = "Handle Razorpay webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        
        if (paymentService.verifyWebhookSignature(payload, signature)) {
            // Parse event type from payload
            // paymentService.handleWebhookEvent(eventType, payload);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

