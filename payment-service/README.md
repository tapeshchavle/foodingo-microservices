# 💳 **PAYMENT SERVICE**

> Payment Processing and Management Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Razorpay](https://img.shields.io/badge/Razorpay-Integration-blue.svg)](https://razorpay.com/)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)

---

## 📋 **OVERVIEW**

Payment Service handles all **Payment Processing** and **Transaction Management** for the Foodingo platform. It integrates with Razorpay payment gateway to process payments, handle refunds, and manage payment statuses.

### **Key Features:**
- 💳 **Razorpay Integration**: Complete payment gateway integration
- 🔄 **Payment Processing**: Order creation, payment verification, and status tracking
- 💰 **Refund Management**: Process refunds and track refund status
- 🔐 **Signature Verification**: Secure payment verification using HMAC
- 📊 **Transaction History**: Complete payment transaction records
- 🔔 **Webhook Handling**: Real-time payment status updates
- 📈 **Analytics**: Payment metrics and reporting
- 🛡️ **Security**: PCI DSS compliant payment handling

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────────────┐
│                    PAYMENT SERVICE                          │
│                     Port: 8084                             │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Razorpay      │  │   Payment       │                  │
│  │   Integration   │  │   Repository    │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Signature     │  │   Webhook       │                  │
│  │   Verification  │  │   Handler       │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
   ┌────▼────┐         ┌─────▼─────┐         ┌─────▼─────┐
   │MongoDB  │         │   Redis   │         │ Razorpay  │
   │Database │         │  Cache    │         │ Gateway   │
   └─────────┘         └───────────┘         └───────────┘
```

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB
- Redis
- Razorpay Account & API Keys

### **1. Build the Service**
```bash
cd payment-service
mvn clean install
```

### **2. Configure Razorpay**
```bash
# Set Razorpay credentials
export RAZORPAY_KEY=rzp_test_your_key_id
export RAZORPAY_SECRET=your_secret_key
export RAZORPAY_WEBHOOK_SECRET=your_webhook_secret
```

### **3. Run the Service**
```bash
mvn spring-boot:run
```

### **4. Access Service**
- **Payment Service**: http://localhost:8084
- **Swagger UI**: http://localhost:8084/swagger-ui.html
- **Health Check**: http://localhost:8084/actuator/health

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8084

spring:
  application:
    name: payment-service
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/foodingo_payment
      auto-index-creation: true
  redis:
    host: localhost
    port: 6379

# Razorpay Configuration
razorpay:
  key: ${RAZORPAY_KEY:rzp_test_fIHdXKFZG9UZ0w}
  secret: ${RAZORPAY_SECRET:OFLTu1nDmfyyOjSjlOH3b4u2}
  webhook:
    secret: ${RAZORPAY_WEBHOOK_SECRET:your_webhook_secret}

# Eureka Configuration
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

# Management Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  tracing:
    sampling:
      probability: 1.0
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans

# Logging Configuration
logging:
  level:
    com.foodingo.payment: DEBUG
```

### **Environment Variables**
```bash
# Server Configuration
SERVER_PORT=8084

# Database Configuration
MONGODB_URI=mongodb://admin:admin123@localhost:27017/foodingo_payment

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# Razorpay Configuration
RAZORPAY_KEY=rzp_test_your_key_id
RAZORPAY_SECRET=your_secret_key
RAZORPAY_WEBHOOK_SECRET=your_webhook_secret

# Eureka Configuration
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
```

---

## 🔧 **API ENDPOINTS**

### **Payment Processing**
```bash
# Create Payment
POST /api/payments
Content-Type: application/json
{
  "orderId": "order-123",
  "userId": "user-456",
  "restaurantId": "restaurant-789",
  "amount": 500.00,
  "currency": "INR",
  "paymentMethod": "RAZORPAY",
  "description": "Food order payment",
  "receipt": "receipt-123"
}

# Response
{
  "id": "payment-123",
  "orderId": "order-123",
  "userId": "user-456",
  "restaurantId": "restaurant-789",
  "amount": 500.00,
  "currency": "INR",
  "status": "PENDING",
  "paymentMethod": "RAZORPAY",
  "razorpayOrderId": "order_ABC123",
  "description": "Food order payment",
  "receipt": "receipt-123",
  "createdAt": "2024-01-01T10:00:00Z"
}
```

### **Payment Verification**
```bash
# Verify Payment
POST /api/payments/verify
Content-Type: application/json
{
  "razorpayOrderId": "order_ABC123",
  "razorpayPaymentId": "pay_DEF456",
  "razorpaySignature": "signature_GHI789"
}

# Response
{
  "id": "payment-123",
  "orderId": "order-123",
  "status": "COMPLETED",
  "razorpayPaymentId": "pay_DEF456",
  "paidAt": "2024-01-01T10:05:00Z",
  "gatewayTransactionId": "pay_DEF456"
}
```

### **Payment Management**
```bash
# Get Payment by ID
GET /api/payments/{paymentId}

# Get Payment by Order ID
GET /api/payments/order/{orderId}

# Get User Payments
GET /api/payments/user
Authorization: Bearer <jwt-token>

# Get Restaurant Payments
GET /api/payments/restaurant/{restaurantId}

# Get Payments by Status
GET /api/payments/status/{status}
```

### **Refund Management**
```bash
# Process Refund
POST /api/payments/refund
Content-Type: application/json
{
  "paymentId": "payment-123",
  "refundAmount": 250.00,
  "refundReason": "Order cancelled",
  "notes": "Partial refund for cancelled order"
}

# Response
{
  "id": "payment-123",
  "status": "REFUNDED",
  "refundAmount": 250.00,
  "refundReason": "Order cancelled",
  "razorpayRefundId": "rfnd_JKL012",
  "refundedAt": "2024-01-01T11:00:00Z"
}

# Get Refund Status
GET /api/payments/{paymentId}/refund-status
```

### **Webhook Handling**
```bash
# Razorpay Webhook
POST /api/payments/webhook
Content-Type: application/json
X-Razorpay-Signature: webhook_signature
{
  "event": "payment.captured",
  "account_id": "acc_ABC123",
  "created_at": 1640995200,
  "contains": ["payment"],
  "payload": {
    "payment": {
      "entity": {
        "id": "pay_DEF456",
        "amount": 50000,
        "currency": "INR",
        "status": "captured",
        "order_id": "order_ABC123",
        "method": "card"
      }
    }
  }
}
```

---

## 🗄️ **DATA MODELS**

### **Payment Entity**
```java
@Data
@Document(collection = "payments")
public class PaymentEntity {
    @Id
    private String id;
    
    private String orderId;
    private String userId;
    private String restaurantId;
    
    private Double amount;
    private String currency;
    
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    private PaymentMethod paymentMethod;
    
    // Razorpay fields
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String razorpayRefundId;
    
    // Additional fields
    private String description;
    private String receipt;
    private String notes;
    private String failureReason;
    
    private LocalDateTime paidAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;
    
    private Double refundAmount;
    private String refundReason;
    
    private String gatewayTransactionId;
    private String gatewayResponse;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### **Payment Status**
```java
public enum PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED,
    EXPIRED
}
```

### **Payment Method**
```java
public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    NET_BANKING,
    UPI,
    WALLET,
    CASH_ON_DELIVERY,
    RAZORPAY
}
```

---

## 🔐 **SECURITY & VERIFICATION**

### **Signature Verification**
```java
@Service
public class PaymentServiceImpl implements PaymentService {
    
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                RAZORPAY_SECRET.getBytes(StandardCharsets.UTF_8), 
                "HmacSHA256"
            );
            mac.init(secretKeySpec);
            
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = bytesToHex(hash);
            
            return calculatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
```

### **Webhook Verification**
```java
@PostMapping("/webhook")
public ResponseEntity<Void> handleWebhook(
        @RequestBody String payload,
        @RequestHeader("X-Razorpay-Signature") String signature) {
    
    if (paymentService.verifyWebhookSignature(payload, signature)) {
        // Parse event type from payload
        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");
        
        paymentService.handleWebhookEvent(eventType, payload);
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.badRequest().build();
    }
}
```

---

## 📊 **MONITORING**

### **Health Checks**
```bash
# Check Payment Service health
curl http://localhost:8084/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP"
    },
    "redis": {
      "status": "UP"
    }
  }
}
```

### **Payment Metrics**
```bash
# Get Payment Service metrics
curl http://localhost:8084/actuator/metrics

# Key metrics:
# - http.server.requests
# - payment.total.count
# - payment.success.rate
# - payment.refund.count
# - razorpay.api.response.time
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
EXPOSE 8084
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8084/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Docker Compose**
```yaml
payment-service:
  build: ./payment-service
  container_name: payment-service
  ports:
    - "8084:8084"
  environment:
    - SERVER_PORT=8084
    - MONGODB_URI=mongodb://admin:admin123@mongodb:27017/foodingo_payment
    - REDIS_HOST=redis
    - REDIS_PORT=6379
    - RAZORPAY_KEY=${RAZORPAY_KEY}
    - RAZORPAY_SECRET=${RAZORPAY_SECRET}
    - RAZORPAY_WEBHOOK_SECRET=${RAZORPAY_WEBHOOK_SECRET}
    - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka/
  depends_on:
    - mongodb
    - redis
    - eureka-server
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8084/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 5
```

---

## 🔄 **RAZORPAY INTEGRATION**

### **Order Creation**
```java
@Override
public PaymentResponse createPayment(PaymentRequest request) throws RazorpayException {
    PaymentEntity paymentEntity = modelMapper.map(request, PaymentEntity.class);
    paymentEntity.setStatus(PaymentStatus.PENDING);
    paymentEntity.setCreatedAt(LocalDateTime.now());
    
    // Create Razorpay order
    RazorpayClient client = getRazorpayClient();
    JSONObject orderRequest = new JSONObject();
    orderRequest.put("amount", (int)(request.getAmount() * 100)); // Convert to paise
    orderRequest.put("currency", request.getCurrency());
    orderRequest.put("receipt", request.getReceipt());
    orderRequest.put("notes", new JSONObject().put("order_id", request.getOrderId()));
    
    Order razorpayOrder = client.orders.create(orderRequest);
    paymentEntity.setRazorpayOrderId(razorpayOrder.get("id"));
    
    PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
    return modelMapper.map(savedPayment, PaymentResponse.class);
}
```

### **Payment Verification**
```java
@Override
public PaymentResponse verifyPayment(PaymentVerificationRequest request) throws RazorpayException {
    PaymentEntity payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    
    // Verify signature
    if (!verifySignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(), request.getRazorpaySignature())) {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason("Invalid signature");
        payment.setFailedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment signature");
    }
    
    // Get payment details from Razorpay
    RazorpayClient client = getRazorpayClient();
    Payment razorpayPayment = client.payments.fetch(request.getRazorpayPaymentId());
    
    if ("captured".equals(razorpayPayment.get("status"))) {
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setPaidAt(LocalDateTime.now());
        payment.setGatewayTransactionId(razorpayPayment.get("id"));
        payment.setGatewayResponse(razorpayPayment.toString());
    } else {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason("Payment not captured");
        payment.setFailedAt(LocalDateTime.now());
    }
    
    PaymentEntity updatedPayment = paymentRepository.save(payment);
    return modelMapper.map(updatedPayment, PaymentResponse.class);
}
```

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues**

#### **1. Razorpay API Errors**
```bash
# Check Razorpay credentials
curl -u "rzp_test_key:secret" https://api.razorpay.com/v1/orders

# Verify webhook configuration
# Check Razorpay dashboard for webhook URL
```

#### **2. Signature Verification Failures**
```bash
# Check signature verification logs
tail -f logs/payment-service.log | grep "signature"

# Verify Razorpay secret key
echo $RAZORPAY_SECRET
```

#### **3. Payment Status Issues**
```bash
# Check payment status in database
mongo mongodb://admin:admin123@localhost:27017/foodingo_payment
db.payments.find({status: "PENDING"})

# Check Razorpay dashboard for payment status
```

### **Logs**
```bash
# View logs
tail -f logs/payment-service.log

# Key log patterns:
# - "Payment created successfully"
# - "Payment verification successful"
# - "Refund processed"
# - "Webhook received"
```

---

## 📈 **PERFORMANCE TUNING**

### **JVM Settings**
```bash
# Production JVM settings
JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC"
```

### **Database Optimization**
```yaml
spring:
  data:
    mongodb:
      options:
        max-connections-per-host: 100
        threads-allowed-to-block-for-connection-multiplier: 5
        server-selection-timeout: 30000
        max-wait-time: 120000
```

---

## 🎯 **BEST PRACTICES**

### **1. Security**
- Always verify payment signatures
- Use HTTPS for all API calls
- Store sensitive data encrypted
- Implement proper webhook verification

### **2. Error Handling**
- Handle Razorpay API failures gracefully
- Implement retry mechanisms
- Log all payment events
- Provide meaningful error messages

### **3. Performance**
- Use connection pooling
- Implement caching for frequent queries
- Monitor API response times
- Optimize database queries

### **4. Compliance**
- Follow PCI DSS guidelines
- Implement audit logging
- Handle data retention policies
- Ensure data privacy compliance

---

## 📚 **INTEGRATION EXAMPLES**

### **Frontend Integration**
```javascript
// Create payment
const createPayment = async (paymentData) => {
  const response = await fetch('http://localhost:8084/api/payments', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(paymentData)
  });
  return response.json();
};

// Verify payment
const verifyPayment = async (verificationData) => {
  const response = await fetch('http://localhost:8084/api/payments/verify', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(verificationData)
  });
  return response.json();
};

// Process refund
const processRefund = async (refundData) => {
  const response = await fetch('http://localhost:8084/api/payments/refund', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(refundData)
  });
  return response.json();
};
```

### **Service Integration**
```java
@Service
public class OrderService {
    
    @Autowired
    private PaymentService paymentService;
    
    public void processOrderPayment(String orderId, Double amount) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
            .orderId(orderId)
            .amount(amount)
            .currency("INR")
            .description("Order payment")
            .build();
        
        try {
            PaymentResponse payment = paymentService.createPayment(paymentRequest);
            // Handle payment creation
        } catch (RazorpayException e) {
            // Handle payment creation failure
        }
    }
}
```

---

## 📞 **SUPPORT**

For issues and questions:
- Check the [Razorpay documentation](https://razorpay.com/docs/)
- Review [Razorpay API reference](https://razorpay.com/docs/api/)
- Open an issue in the project repository

---

## 📝 **CHANGELOG**

### **Version 1.0.0**
- Initial Payment Service setup
- Razorpay integration
- Payment processing and verification
- Refund management
- Webhook handling
- Signature verification
- Docker support

---

**Built with ❤️ using Spring Boot & Razorpay Integration**

