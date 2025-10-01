package com.foodingo.notification.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foodingo.notification.dto.NotificationRequest;
import com.foodingo.notification.dto.NotificationResponse;
import com.foodingo.notification.enums.NotificationType;

public interface NotificationService {
    
    void sendNotification(String userId, NotificationType type, String title, String message, String orderId, String restaurantId);
    
    NotificationResponse createNotification(NotificationRequest request);
    
    List<NotificationResponse> getUserNotifications(String userId);
    
    Page<NotificationResponse> getUserNotifications(String userId, Pageable pageable);
    
    List<NotificationResponse> getUnreadNotifications(String userId);
    
    long getUnreadCount(String userId);
    
    void markAsRead(String notificationId);
    
    void markAllAsRead(String userId);
    
    void deleteNotification(String notificationId);
    
    void sendOrderNotification(String userId, String orderId, NotificationType type);
    
    void sendEmailNotification(String userId, String subject, String message);
    
    void sendPushNotification(String userId, String title, String message);
    
    void sendSmsNotification(String userId, String message);
    
    void processScheduledNotifications();
    
    List<NotificationResponse> getNotificationsByType(String userId, NotificationType type);
    
    void markAsReadByType(String userId, NotificationType type);
}

