package com.foodingo.notification.dto;

import java.time.LocalDateTime;

import com.foodingo.notification.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String userId;
    private String title;
    private String message;
    private NotificationType type;
    private String orderId;
    private String restaurantId;
    private boolean isRead;
    private boolean isEmailSent;
    private boolean isPushSent;
    private boolean isSmsSent;
    private String imageUrl;
    private String actionUrl;
    private String metadata;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

