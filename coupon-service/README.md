# 🎫 **COUPON SERVICE**

> Coupon and Discount Management Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)

---

## 📋 **OVERVIEW**

Coupon Service manages **Coupons** and **Discounts** for the Foodingo platform. It handles coupon creation, validation, usage tracking, and expiration management.

### **Key Features:**
- 🎫 **Coupon Management**: Create and manage coupons
- ✅ **Validation**: Coupon validation and verification
- 📊 **Usage Tracking**: Track coupon usage
- ⏰ **Expiration Management**: Handle coupon expiration
- 🎯 **Targeted Offers**: User-specific coupons
- 📈 **Analytics**: Coupon performance metrics

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB

### **1. Build the Service**
```bash
cd coupon-service
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Service**
- **Coupon Service**: http://localhost:8088
- **Swagger UI**: http://localhost:8088/swagger-ui.html
- **Health Check**: http://localhost:8088/actuator/health

---

## 🔧 **API ENDPOINTS**

### **Coupon Management**
```bash
# Create coupon (Admin only)
POST /api/coupons
Authorization: Bearer <admin-token>
{
  "code": "SAVE10",
  "description": "10% off on orders above ₹500",
  "discountType": "PERCENTAGE",
  "discountValue": 10.0,
  "minOrderAmount": 500.0,
  "maxDiscountAmount": 100.0,
  "validFrom": "2024-01-01T00:00:00Z",
  "validUntil": "2024-12-31T23:59:59Z",
  "usageLimit": 1000,
  "isActive": true
}

# Validate coupon
POST /api/coupons/validate
Authorization: Bearer <token>
{
  "code": "SAVE10",
  "orderAmount": 600.0,
  "userId": "user-123"
}

# Use coupon
POST /api/coupons/use
Authorization: Bearer <token>
{
  "code": "SAVE10",
  "orderId": "order-456",
  "userId": "user-123"
}

# Get available coupons
GET /api/coupons/available
Authorization: Bearer <token>

# Get coupon by code
GET /api/coupons/code/{code}
```

---

## 🗄️ **DATA MODELS**

### **Coupon Entity**
```java
@Data
@Document(collection = "coupons")
public class CouponEntity {
    @Id
    private String id;
    
    @NotBlank
    private String code;
    
    private String description;
    private CouponType discountType;
    private Double discountValue;
    private Double minOrderAmount;
    private Double maxDiscountAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Integer usageLimit;
    private Integer usedCount;
    
    @Builder.Default
    private boolean isActive = true;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

---

## 🎯 **BEST PRACTICES**

### **1. Coupon Validation**
- Implement proper validation rules
- Handle concurrent usage
- Check expiration dates

### **2. Performance**
- Cache active coupons
- Optimize validation queries
- Use proper indexing

---

**Built with ❤️ using Spring Boot & MongoDB**

