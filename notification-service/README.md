# 🔔 **NOTIFICATION SERVICE**

> Notification Management Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![WebSocket](https://img.shields.io/badge/WebSocket-Real%20Time-blue.svg)](https://spring.io/guides/gs/messaging-stomp-websocket/)
[![Email](https://img.shields.io/badge/Email-SMTP-green.svg)](https://spring.io/guides/gs/sending-email/)

---

## 📋 **OVERVIEW**

Notification Service handles **Real-time Notifications** and **Email Communications** for the Foodingo platform. It provides WebSocket support for instant notifications and email services.

### **Key Features:**
- 📧 **Email Notifications**: SMTP email sending
- 🔔 **Real-time Notifications**: WebSocket support
- 📱 **Push Notifications**: Mobile push support
- 🎯 **Targeted Messaging**: User-specific notifications
- 📊 **Notification Analytics**: Delivery tracking
- 🎨 **Template Management**: Notification templates

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB
- SMTP Server (Gmail, SendGrid, etc.)

### **1. Build the Service**
```bash
cd notification-service
mvn clean install
```

### **2. Configure Email**
```bash
export SMTP_HOST=smtp.gmail.com
export SMTP_PORT=587
export SMTP_USERNAME=your_email@gmail.com
export SMTP_PASSWORD=your_app_password
```

### **3. Run the Service**
```bash
mvn spring-boot:run
```

### **4. Access Service**
- **Notification Service**: http://localhost:8086
- **WebSocket**: ws://localhost:8086/ws
- **Swagger UI**: http://localhost:8086/swagger-ui.html

---

## 🔧 **API ENDPOINTS**

### **Notification Management**
```bash
# Send email notification
POST /api/notifications/email
Authorization: Bearer <token>
{
  "to": "user@example.com",
  "subject": "Order Confirmation",
  "template": "order-confirmation",
  "data": {
    "orderId": "order-123",
    "totalAmount": 299.00
  }
}

# Send real-time notification
POST /api/notifications/realtime
Authorization: Bearer <token>
{
  "userId": "user-123",
  "title": "Order Update",
  "message": "Your order is being prepared",
  "type": "ORDER_UPDATE"
}

# Get user notifications
GET /api/notifications/user
Authorization: Bearer <token>

# Mark notification as read
PUT /api/notifications/{id}/read
Authorization: Bearer <token>
```

### **WebSocket Connection**
```javascript
// Connect to WebSocket
const socket = new WebSocket('ws://localhost:8086/ws');

socket.onopen = function(event) {
    console.log('Connected to notification service');
};

socket.onmessage = function(event) {
    const notification = JSON.parse(event.data);
    console.log('Received notification:', notification);
};
```

---

## 🗄️ **DATA MODELS**

### **Notification Entity**
```java
@Data
@Document(collection = "notifications")
public class NotificationEntity {
    @Id
    private String id;
    
    private String userId;
    private String title;
    private String message;
    private NotificationType type;
    private String channel; // EMAIL, REALTIME, PUSH
    
    @Builder.Default
    private boolean isRead = false;
    
    @Builder.Default
    private boolean isDelivered = false;
    
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
```

---

## 🎯 **BEST PRACTICES**

### **1. Email Delivery**
- Use proper SMTP configuration
- Implement retry mechanisms
- Handle bounce emails

### **2. Real-time Notifications**
- Manage WebSocket connections
- Handle connection failures
- Implement message queuing

### **3. Performance**
- Use async processing
- Implement rate limiting
- Cache notification templates

---

**Built with ❤️ using Spring Boot, WebSocket & Email**

