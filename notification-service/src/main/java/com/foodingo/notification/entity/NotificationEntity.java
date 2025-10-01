package com.foodingo.notification.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodingo.notification.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class NotificationEntity {
    
    @Id
    private String id;
    
    private String userId;
    
    private String title;
    
    private String message;
    
    private NotificationType type;
    
    private String orderId; // Optional
    
    private String restaurantId; // Optional
    
    @Builder.Default
    private boolean isRead = false;
    
    @Builder.Default
    private boolean isEmailSent = false;
    
    @Builder.Default
    private boolean isPushSent = false;
    
    @Builder.Default
    private boolean isSmsSent = false;
    
    private String imageUrl; // Optional
    
    private String actionUrl; // Optional - deep link
    
    private String metadata; // JSON string for additional data
    
    private LocalDateTime scheduledAt; // For scheduled notifications
    
    private LocalDateTime sentAt;
    
    private LocalDateTime readAt;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

