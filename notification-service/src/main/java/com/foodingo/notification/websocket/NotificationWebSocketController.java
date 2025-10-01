package com.foodingo.notification.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.foodingo.notification.dto.NotificationResponse;
import com.foodingo.notification.service.NotificationService;

@Controller
public class NotificationWebSocketController {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public NotificationResponse handleNotification(NotificationResponse notification) {
        return notification;
    }
    
    public void sendNotificationToUser(String userId, NotificationResponse notification) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }
    
    public void sendNotificationToAll(NotificationResponse notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}

