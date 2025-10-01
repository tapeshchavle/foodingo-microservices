package com.foodingo.order.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.order.dto.OrderRequest;
import com.foodingo.order.dto.OrderResponse;
import com.foodingo.order.enums.OrderStatus;
import com.foodingo.order.service.OrderService;
import com.razorpay.RazorpayException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    @Operation(summary = "Create new order and payment")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) throws RazorpayException {
        
        // TODO: Get userId from JWT token via Feign client to User Service
        String currentUserId = userId != null ? userId : "default-user-id";
        OrderResponse response = orderService.createOrderPayment(request, currentUserId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/verify-payment")
    @Operation(summary = "Verify payment")
    public ResponseEntity<Void> verifyPayment(
            @RequestBody Map<String, String> paymentData,
            @RequestParam String status) {
        orderService.verifyPayment(paymentData, status);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get user's orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<OrderResponse> orders = orderService.getUserOrders(currentUserId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    @Operation(summary = "Get all orders (Admin)")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get restaurant's orders")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(@PathVariable String restaurantId) {
        List<OrderResponse> orders = orderService.getRestaurantOrders(restaurantId);
        return ResponseEntity.ok(orders);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestParam String reason) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        orderService.cancelOrder(id, currentUserId, reason);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        orderService.removeOrder(id);
        return ResponseEntity.noContent().build();
    }
}

