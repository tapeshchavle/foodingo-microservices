# 📦 **ORDER SERVICE**

> Order Management and Processing Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.12-blue.svg)](https://www.rabbitmq.com/)

---

## 📋 **OVERVIEW**

Order Service handles **Order Management** and **Processing** for the Foodingo platform. It manages order creation, status tracking, delivery management, and integrates with other services.

### **Key Features:**
- 📝 **Order Creation**: Create and manage food orders
- 📊 **Status Tracking**: Real-time order status updates
- 🚚 **Delivery Management**: Track delivery progress
- 🔄 **Event Publishing**: Order events for other services
- 📈 **Analytics**: Order metrics and reporting
- 💰 **Price Calculation**: Order total calculation
- 🎯 **Order History**: Complete order tracking

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB
- RabbitMQ
- Redis (for caching)

### **1. Build the Service**
```bash
cd order-service
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Service**
- **Order Service**: http://localhost:8083
- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **Health Check**: http://localhost:8083/actuator/health

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8083

spring:
  application:
    name: order-service
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/foodingo_order
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123
  redis:
    host: localhost
    port: 6379

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

---

## 🔧 **API ENDPOINTS**

### **Order Management**
```bash
# Create order
POST /api/orders
Authorization: Bearer <token>
{
  "userId": "user-123",
  "restaurantId": "restaurant-456",
  "items": [
    {
      "foodId": "food-789",
      "quantity": 2,
      "price": 299.00,
      "customizations": ["Extra cheese", "No onions"]
    }
  ],
  "deliveryAddress": {
    "street": "123 Main St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "zipCode": "400001"
  },
  "paymentMethod": "RAZORPAY",
  "couponCode": "SAVE10"
}

# Get order by ID
GET /api/orders/{id}
Authorization: Bearer <token>

# Get user orders
GET /api/orders/user
Authorization: Bearer <token>

# Get restaurant orders
GET /api/orders/restaurant/{restaurantId}
Authorization: Bearer <token>

# Update order status
PUT /api/orders/{id}/status
Authorization: Bearer <token>
{
  "status": "PREPARING",
  "notes": "Order is being prepared"
}
```

### **Order Status Management**
```bash
# Get order status
GET /api/orders/{id}/status

# Update delivery status
PUT /api/orders/{id}/delivery-status
Authorization: Bearer <delivery-token>
{
  "deliveryStatus": "OUT_FOR_DELIVERY",
  "estimatedDeliveryTime": "2024-01-01T12:00:00Z",
  "deliveryPartnerId": "partner-123"
}

# Complete order
PUT /api/orders/{id}/complete
Authorization: Bearer <token>
{
  "deliveryNotes": "Delivered successfully"
}
```

---

## 🗄️ **DATA MODELS**

### **Order Entity**
```java
@Data
@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    
    private String userId;
    private String restaurantId;
    private List<OrderItem> items;
    private Address deliveryAddress;
    private Double totalAmount;
    private Double discountAmount;
    private String couponCode;
    private String paymentMethod;
    private String paymentId;
    
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
    
    @Builder.Default
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;
    
    private String deliveryPartnerId;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime deliveredAt;
    private String deliveryNotes;
    
    private List<OrderStatusHistory> statusHistory;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### **Order Status**
```java
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    REFUNDED
}
```

### **Delivery Status**
```java
public enum DeliveryStatus {
    PENDING,
    ASSIGNED,
    PICKED_UP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    FAILED,
    RETURNED
}
```

---

## 🔄 **EVENT PUBLISHING**

### **Order Events**
```java
@Component
public class OrderEventPublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void publishOrderCreated(OrderEntity order) {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
            .orderId(order.getId())
            .userId(order.getUserId())
            .restaurantId(order.getRestaurantId())
            .totalAmount(order.getTotalAmount())
            .timestamp(LocalDateTime.now())
            .build();
        
        rabbitTemplate.convertAndSend("order.exchange", "order.created", event);
    }
    
    public void publishOrderStatusUpdated(OrderEntity order) {
        OrderStatusUpdatedEvent event = OrderStatusUpdatedEvent.builder()
            .orderId(order.getId())
            .status(order.getStatus())
            .timestamp(LocalDateTime.now())
            .build();
        
        rabbitTemplate.convertAndSend("order.exchange", "order.status.updated", event);
    }
}
```

---

## 🐳 **DOCKER DEPLOYMENT**

### **Dockerfile**
```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8083/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 📊 **MONITORING**

### **Health Checks**
```bash
curl http://localhost:8083/actuator/health
```

### **Metrics**
```bash
curl http://localhost:8083/actuator/metrics
```

---

## 🎯 **BEST PRACTICES**

### **1. Order Processing**
- Implement proper validation
- Handle concurrent order updates
- Use optimistic locking

### **2. Event Management**
- Ensure event ordering
- Handle event failures
- Implement retry mechanisms

### **3. Performance**
- Cache frequently accessed data
- Optimize database queries
- Use proper indexing

---

**Built with ❤️ using Spring Boot, MongoDB & RabbitMQ**

