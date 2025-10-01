package com.foodingo.payment.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.payment.dto.PaymentRequest;
import com.foodingo.payment.dto.PaymentResponse;
import com.foodingo.payment.dto.PaymentVerificationRequest;
import com.foodingo.payment.dto.RefundRequest;
import com.foodingo.payment.entity.PaymentEntity;
import com.foodingo.payment.enums.PaymentStatus;
import com.foodingo.payment.repository.PaymentRepository;
import com.foodingo.payment.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;
    
    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;
    
    @Value("${razorpay.webhook.secret}")
    private String WEBHOOK_SECRET;
    
    private RazorpayClient razorpayClient;
    
    private RazorpayClient getRazorpayClient() throws RazorpayException {
        if (razorpayClient == null) {
            razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
        }
        return razorpayClient;
    }
    
    @Override
    public PaymentResponse createPayment(PaymentRequest request) throws RazorpayException {
        PaymentEntity paymentEntity = modelMapper.map(request, PaymentEntity.class);
        paymentEntity.setStatus(PaymentStatus.PENDING);
        paymentEntity.setCreatedAt(LocalDateTime.now());
        
        // Create Razorpay order
        RazorpayClient client = getRazorpayClient();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(request.getAmount() * 100)); // Convert to paise
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", request.getReceipt());
        orderRequest.put("notes", new JSONObject().put("order_id", request.getOrderId()));
        
        Order razorpayOrder = client.orders.create(orderRequest);
        paymentEntity.setRazorpayOrderId(razorpayOrder.get("id"));
        
        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
        return modelMapper.map(savedPayment, PaymentResponse.class);
    }
    
    @Override
    public PaymentResponse verifyPayment(PaymentVerificationRequest request) throws RazorpayException {
        PaymentEntity payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        
        try {
            // Verify signature
            if (!verifySignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(), request.getRazorpaySignature())) {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Invalid signature");
                payment.setFailedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment signature");
            }
            
            // Get payment details from Razorpay
            RazorpayClient client = getRazorpayClient();
            Payment razorpayPayment = client.payments.fetch(request.getRazorpayPaymentId());
            
            if ("captured".equals(razorpayPayment.get("status"))) {
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
                payment.setRazorpaySignature(request.getRazorpaySignature());
                payment.setPaidAt(LocalDateTime.now());
                payment.setGatewayTransactionId(razorpayPayment.get("id"));
                payment.setGatewayResponse(razorpayPayment.toString());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Payment not captured");
                payment.setFailedAt(LocalDateTime.now());
            }
            
            PaymentEntity updatedPayment = paymentRepository.save(payment);
            return modelMapper.map(updatedPayment, PaymentResponse.class);
            
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
            payment.setFailedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment verification failed: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentResponse getPaymentById(String paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return modelMapper.map(payment, PaymentResponse.class);
    }
    
    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId).stream()
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found for order: " + orderId));
        return modelMapper.map(payment, PaymentResponse.class);
    }
    
    @Override
    public List<PaymentResponse> getUserPayments(String userId) {
        return paymentRepository.findByUserId(userId).stream()
            .map(payment -> modelMapper.map(payment, PaymentResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentResponse> getRestaurantPayments(String restaurantId) {
        return paymentRepository.findByRestaurantId(restaurantId).stream()
            .map(payment -> modelMapper.map(payment, PaymentResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
            .map(payment -> modelMapper.map(payment, PaymentResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public PaymentResponse updatePaymentStatus(String paymentId, PaymentStatus status) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        
        payment.setStatus(status);
        if (status == PaymentStatus.COMPLETED) {
            payment.setPaidAt(LocalDateTime.now());
        } else if (status == PaymentStatus.FAILED) {
            payment.setFailedAt(LocalDateTime.now());
        }
        
        PaymentEntity updatedPayment = paymentRepository.save(payment);
        return modelMapper.map(updatedPayment, PaymentResponse.class);
    }
    
    @Override
    public PaymentResponse processRefund(RefundRequest request) throws RazorpayException {
        PaymentEntity payment = paymentRepository.findById(request.getPaymentId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only completed payments can be refunded");
        }
        
        try {
            RazorpayClient client = getRazorpayClient();
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", (int)(request.getRefundAmount() * 100));
            refundRequest.put("notes", new JSONObject().put("reason", request.getRefundReason()));
            
            Refund refund = client.payments.refund(payment.getRazorpayPaymentId(), refundRequest);
            
            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setRazorpayRefundId(refund.get("id"));
            payment.setRefundAmount(request.getRefundAmount());
            payment.setRefundReason(request.getRefundReason());
            payment.setRefundedAt(LocalDateTime.now());
            
            PaymentEntity updatedPayment = paymentRepository.save(payment);
            return modelMapper.map(updatedPayment, PaymentResponse.class);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refund failed: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentResponse getRefundStatus(String paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return modelMapper.map(payment, PaymentResponse.class);
    }
    
    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(WEBHOOK_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = bytesToHex(hash);
            
            return calculatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public void handleWebhookEvent(String eventType, String payload) {
        // Handle different webhook events
        switch (eventType) {
            case "payment.captured":
                // Handle payment captured
                break;
            case "payment.failed":
                // Handle payment failed
                break;
            case "refund.created":
                // Handle refund created
                break;
            default:
                // Handle other events
                break;
        }
    }
    
    private boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(RAZORPAY_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = bytesToHex(hash);
            
            return calculatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}

