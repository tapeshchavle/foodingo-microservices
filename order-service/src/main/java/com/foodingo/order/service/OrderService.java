package com.foodingo.order.service;

import java.util.List;
import java.util.Map;

import com.foodingo.order.dto.OrderRequest;
import com.foodingo.order.dto.OrderResponse;
import com.foodingo.order.enums.OrderStatus;
import com.razorpay.RazorpayException;

public interface OrderService {
    OrderResponse createOrderPayment(OrderRequest request, String userId) throws RazorpayException;
    
    void verifyPayment(Map<String, String> paymentData, String status);
    
    List<OrderResponse> getUserOrders(String userId);
    
    OrderResponse getOrderById(String id);
    
    List<OrderResponse> getAllOrders();
    
    List<OrderResponse> getRestaurantOrders(String restaurantId);
    
    void updateOrderStatus(String orderId, OrderStatus status);
    
    void cancelOrder(String orderId, String userId, String reason);
    
    void removeOrder(String orderId);
}

