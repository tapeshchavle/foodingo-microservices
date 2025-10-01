package com.foodingo.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.foodingo.notification.dto.NotificationRequest;
import com.foodingo.notification.dto.NotificationResponse;
import com.foodingo.notification.enums.NotificationType;
import com.foodingo.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "Notification management APIs")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    @Operation(summary = "Create a notification")
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        
        NotificationResponse response = notificationService.createNotification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/send")
    @Operation(summary = "Send notification to user")
    public ResponseEntity<Void> sendNotification(
            @RequestParam String userId,
            @RequestParam NotificationType type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String restaurantId) {
        
        notificationService.sendNotification(userId, type, title, message, orderId, restaurantId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get user notifications")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<NotificationResponse> notifications = notificationService.getUserNotifications(currentUserId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/paginated")
    @Operation(summary = "Get user notifications with pagination")
    public ResponseEntity<Page<NotificationResponse>> getUserNotificationsPaginated(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NotificationResponse> notifications = notificationService.getUserNotifications(currentUserId, pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/unread")
    @Operation(summary = "Get unread notifications")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(currentUserId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/unread-count")
    @Operation(summary = "Get unread notification count")
    public ResponseEntity<Long> getUnreadCount(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        long count = notificationService.getUnreadCount(currentUserId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/user/type/{type}")
    @Operation(summary = "Get notifications by type")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByType(
            @PathVariable NotificationType type,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        List<NotificationResponse> notifications = notificationService.getNotificationsByType(currentUserId, type);
        return ResponseEntity.ok(notifications);
    }
    
    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/user/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        notificationService.markAllAsRead(currentUserId);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/user/read-by-type/{type}")
    @Operation(summary = "Mark notifications as read by type")
    public ResponseEntity<Void> markAsReadByType(
            @PathVariable NotificationType type,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        notificationService.markAsReadByType(currentUserId, type);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete a notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/order/{orderId}")
    @Operation(summary = "Send order notification")
    public ResponseEntity<Void> sendOrderNotification(
            @PathVariable String orderId,
            @RequestParam NotificationType type,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        String currentUserId = userId != null ? userId : "default-user-id";
        notificationService.sendOrderNotification(currentUserId, orderId, type);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/email")
    @Operation(summary = "Send email notification")
    public ResponseEntity<Void> sendEmailNotification(
            @RequestParam String userId,
            @RequestParam String subject,
            @RequestParam String message) {
        
        notificationService.sendEmailNotification(userId, subject, message);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/push")
    @Operation(summary = "Send push notification")
    public ResponseEntity<Void> sendPushNotification(
            @RequestParam String userId,
            @RequestParam String title,
            @RequestParam String message) {
        
        notificationService.sendPushNotification(userId, title, message);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/sms")
    @Operation(summary = "Send SMS notification")
    public ResponseEntity<Void> sendSmsNotification(
            @RequestParam String userId,
            @RequestParam String message) {
        
        notificationService.sendSmsNotification(userId, message);
        return ResponseEntity.ok().build();
    }
}

