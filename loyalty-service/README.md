# 🎁 **LOYALTY SERVICE**

> Loyalty Program Management Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)

---

## 📋 **OVERVIEW**

Loyalty Service manages **Loyalty Points** and **Rewards** for the Foodingo platform. It handles points earning, redemption, and loyalty program management.

### **Key Features:**
- 🎯 **Points Earning**: Earn points on orders
- 💰 **Points Redemption**: Redeem points for rewards
- 📊 **Loyalty Tiers**: Different loyalty levels
- 📈 **Transaction History**: Complete points history
- 🎁 **Reward Management**: Manage rewards and offers
- 📊 **Analytics**: Loyalty program insights

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB

### **1. Build the Service**
```bash
cd loyalty-service
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Service**
- **Loyalty Service**: http://localhost:8087
- **Swagger UI**: http://localhost:8087/swagger-ui.html
- **Health Check**: http://localhost:8087/actuator/health

---

## 🔧 **API ENDPOINTS**

### **Loyalty Management**
```bash
# Get user loyalty balance
GET /api/loyalty/balance
Authorization: Bearer <token>

# Earn points
POST /api/loyalty/earn
Authorization: Bearer <token>
{
  "userId": "user-123",
  "orderId": "order-456",
  "points": 50,
  "reason": "Order completion"
}

# Redeem points
POST /api/loyalty/redeem
Authorization: Bearer <token>
{
  "userId": "user-123",
  "points": 100,
  "reason": "Discount redemption"
}

# Get transaction history
GET /api/loyalty/transactions
Authorization: Bearer <token>

# Get loyalty tier
GET /api/loyalty/tier
Authorization: Bearer <token>
```

---

## 🗄️ **DATA MODELS**

### **Loyalty Transaction Entity**
```java
@Data
@Document(collection = "loyalty_transactions")
public class LoyaltyTransactionEntity {
    @Id
    private String id;
    
    private String userId;
    private String orderId;
    private Integer points;
    private TransactionType type; // EARN, REDEEM
    private String reason;
    private Integer balanceAfter;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
```

---

## 🎯 **BEST PRACTICES**

### **1. Points Management**
- Implement atomic transactions
- Handle concurrent operations
- Validate point balances

### **2. Performance**
- Cache user balances
- Optimize transaction queries
- Use proper indexing

---

**Built with ❤️ using Spring Boot & MongoDB**

