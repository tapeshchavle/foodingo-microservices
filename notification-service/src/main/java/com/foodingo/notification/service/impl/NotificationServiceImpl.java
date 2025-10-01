package com.foodingo.notification.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.notification.dto.NotificationRequest;
import com.foodingo.notification.dto.NotificationResponse;
import com.foodingo.notification.entity.NotificationEntity;
import com.foodingo.notification.enums.NotificationType;
import com.foodingo.notification.repository.NotificationRepository;
import com.foodingo.notification.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Override
    @Async
    public void sendNotification(String userId, NotificationType type, String title, String message, String orderId, String restaurantId) {
        NotificationEntity notification = NotificationEntity.builder()
            .userId(userId)
            .title(title)
            .message(message)
            .type(type)
            .orderId(orderId)
            .restaurantId(restaurantId)
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();
        
        notification = notificationRepository.save(notification);
        
        // Send via different channels
        sendEmailNotification(userId, title, message);
        sendPushNotification(userId, title, message);
    }
    
    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        NotificationEntity notification = modelMapper.map(request, NotificationEntity.class);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        
        NotificationEntity savedNotification = notificationRepository.save(notification);
        return modelMapper.map(savedNotification, NotificationResponse.class);
    }
    
    @Override
    public List<NotificationResponse> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId).stream()
            .map(notification -> modelMapper.map(notification, NotificationResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public Page<NotificationResponse> getUserNotifications(String userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
            .map(notification -> modelMapper.map(notification, NotificationResponse.class));
    }
    
    @Override
    public List<NotificationResponse> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId).stream()
            .map(notification -> modelMapper.map(notification, NotificationResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public long getUnreadCount(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    
    @Override
    public void markAsRead(String notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
    
    @Override
    public void markAllAsRead(String userId) {
        List<NotificationEntity> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(notifications);
    }
    
    @Override
    public void deleteNotification(String notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    @Override
    @Async
    public void sendOrderNotification(String userId, String orderId, NotificationType type) {
        String title = getOrderNotificationTitle(type);
        String message = getOrderNotificationMessage(type, orderId);
        
        sendNotification(userId, type, title, message, orderId, null);
    }
    
    @Override
    @Async
    public void sendEmailNotification(String userId, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userId); // Assuming userId is email
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            
            mailSender.send(mailMessage);
        } catch (Exception e) {
            // Log error but don't throw exception
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    @Override
    @Async
    public void sendPushNotification(String userId, String title, String message) {
        // TODO: Implement push notification service (FCM, APNS)
        System.out.println("Push notification sent to " + userId + ": " + title + " - " + message);
    }
    
    @Override
    @Async
    public void sendSmsNotification(String userId, String message) {
        // TODO: Implement SMS service (Twilio, AWS SNS)
        System.out.println("SMS sent to " + userId + ": " + message);
    }
    
    @Override
    public void processScheduledNotifications() {
        List<NotificationEntity> scheduledNotifications = notificationRepository
            .findByScheduledAtLessThanEqualAndSentAtIsNull(LocalDateTime.now());
        
        for (NotificationEntity notification : scheduledNotifications) {
            // Send the notification
            sendEmailNotification(notification.getUserId(), notification.getTitle(), notification.getMessage());
            sendPushNotification(notification.getUserId(), notification.getTitle(), notification.getMessage());
            
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }
    
    @Override
    public List<NotificationResponse> getNotificationsByType(String userId, NotificationType type) {
        return notificationRepository.findByUserIdAndType(userId, type).stream()
            .map(notification -> modelMapper.map(notification, NotificationResponse.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public void markAsReadByType(String userId, NotificationType type) {
        List<NotificationEntity> notifications = notificationRepository.findByUserIdAndType(userId, type);
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(notifications);
    }
    
    private String getOrderNotificationTitle(NotificationType type) {
        switch (type) {
            case ORDER_CONFIRMED:
                return "Order Confirmed!";
            case ORDER_PREPARING:
                return "Your order is being prepared";
            case ORDER_READY:
                return "Your order is ready!";
            case ORDER_OUT_FOR_DELIVERY:
                return "Your order is out for delivery";
            case ORDER_DELIVERED:
                return "Order delivered successfully!";
            case ORDER_CANCELLED:
                return "Order cancelled";
            default:
                return "Order Update";
        }
    }
    
    private String getOrderNotificationMessage(NotificationType type, String orderId) {
        switch (type) {
            case ORDER_CONFIRMED:
                return "Your order #" + orderId + " has been confirmed and is being prepared.";
            case ORDER_PREPARING:
                return "Your order #" + orderId + " is being prepared by the restaurant.";
            case ORDER_READY:
                return "Your order #" + orderId + " is ready for pickup/delivery.";
            case ORDER_OUT_FOR_DELIVERY:
                return "Your order #" + orderId + " is out for delivery. Track your order in real-time.";
            case ORDER_DELIVERED:
                return "Your order #" + orderId + " has been delivered successfully. Enjoy your meal!";
            case ORDER_CANCELLED:
                return "Your order #" + orderId + " has been cancelled.";
            default:
                return "Update for your order #" + orderId;
        }
    }
}

