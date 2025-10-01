package com.foodingo.order.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.order.dto.OrderRequest;
import com.foodingo.order.dto.OrderResponse;
import com.foodingo.order.entity.OrderEntity;
import com.foodingo.order.entity.OrderEntity.OrderStatusHistory;
import com.foodingo.order.enums.OrderStatus;
import com.foodingo.order.enums.PaymentStatus;
import com.foodingo.order.repository.OrderRepository;
import com.foodingo.order.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;
    
    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;
    
    @Override
    public OrderResponse createOrderPayment(OrderRequest request, String userId) throws RazorpayException {
        OrderEntity orderEntity = modelMapper.map(request, OrderEntity.class);
        orderEntity.setUserId(userId);
        orderEntity.setPaymentStatus(PaymentStatus.PENDING);
        orderEntity.setOrderStatus(OrderStatus.PENDING);
        
        // Initialize status history
        List<OrderStatusHistory> history = new ArrayList<>();
        history.add(OrderStatusHistory.builder()
            .status(OrderStatus.PENDING)
            .timestamp(LocalDateTime.now())
            .updatedBy(userId)
            .notes("Order created")
            .build());
        orderEntity.setStatusHistory(history);
        
        orderEntity = orderRepository.save(orderEntity);
        
        // Create Razorpay order
        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(orderEntity.getAmount() * 100)); // Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);
        
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        orderEntity.setRazorpayOrderId(razorpayOrder.get("id"));
        orderEntity = orderRepository.save(orderEntity);
        
        return modelMapper.map(orderEntity, OrderResponse.class);
    }
    
    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        if ("PAID".equalsIgnoreCase(status)) {
            existingOrder.setPaymentStatus(PaymentStatus.PAID);
            existingOrder.setOrderStatus(OrderStatus.CONFIRMED);
            
            // Add status history
            OrderStatusHistory history = OrderStatusHistory.builder()
                .status(OrderStatus.CONFIRMED)
                .timestamp(LocalDateTime.now())
                .notes("Payment confirmed")
                .build();
            existingOrder.getStatusHistory().add(history);
        } else {
            existingOrder.setPaymentStatus(PaymentStatus.FAILED);
        }
        
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
    }
    
    @Override
    public List<OrderResponse> getUserOrders(String userId) {
        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
        return orderEntities.stream()
            .map(entity -> modelMapper.map(entity, OrderResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public OrderResponse getOrderById(String id) {
        OrderEntity order = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return modelMapper.map(order, OrderResponse.class);
    }
    
    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderEntities.stream()
            .map(entity -> modelMapper.map(entity, OrderResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderResponse> getRestaurantOrders(String restaurantId) {
        List<OrderEntity> orders = orderRepository.findByRestaurantId(restaurantId);
        return orders.stream()
            .map(entity -> modelMapper.map(entity, OrderResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public void updateOrderStatus(String orderId, OrderStatus status) {
        OrderEntity entity = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        entity.setOrderStatus(status);
        
        // Add status history
        OrderStatusHistory history = OrderStatusHistory.builder()
            .status(status)
            .timestamp(LocalDateTime.now())
            .notes("Status updated to " + status)
            .build();
        entity.getStatusHistory().add(history);
        
        if (status == OrderStatus.DELIVERED) {
            entity.setActualDeliveryTime(LocalDateTime.now());
        }
        
        orderRepository.save(entity);
    }
    
    @Override
    public void cancelOrder(String orderId, String userId, String reason) {
        OrderEntity entity = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        entity.setOrderStatus(OrderStatus.CANCELLED);
        entity.setCancellationReason(reason);
        entity.setCancelledAt(LocalDateTime.now());
        entity.setCancelledBy(userId);
        
        // Add status history
        OrderStatusHistory history = OrderStatusHistory.builder()
            .status(OrderStatus.CANCELLED)
            .timestamp(LocalDateTime.now())
            .updatedBy(userId)
            .notes("Order cancelled: " + reason)
            .build();
        entity.getStatusHistory().add(history);
        
        orderRepository.save(entity);
    }
    
    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }
}

