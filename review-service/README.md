# ⭐ **REVIEW SERVICE**

> Review and Rating Management Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)

---

## 📋 **OVERVIEW**

Review Service manages **Reviews** and **Ratings** for restaurants and food items in the Foodingo platform. It handles review creation, moderation, and provides analytics.

### **Key Features:**
- ⭐ **Rating System**: 1-5 star rating system
- 📝 **Review Management**: Create, update, delete reviews
- 🔍 **Review Analytics**: Statistics and insights
- 🛡️ **Moderation**: Review moderation features
- 📊 **Aggregation**: Average ratings calculation
- 🏷️ **Categorization**: Review categorization

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB

### **1. Build the Service**
```bash
cd review-service
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Service**
- **Review Service**: http://localhost:8085
- **Swagger UI**: http://localhost:8085/swagger-ui.html
- **Health Check**: http://localhost:8085/actuator/health

---

## 🔧 **API ENDPOINTS**

### **Review Management**
```bash
# Create review
POST /api/reviews
Authorization: Bearer <token>
{
  "userId": "user-123",
  "restaurantId": "restaurant-456",
  "foodId": "food-789",
  "rating": 4,
  "comment": "Great food and service!",
  "reviewType": "RESTAURANT"
}

# Get reviews by restaurant
GET /api/reviews/restaurant/{restaurantId}

# Get reviews by food
GET /api/reviews/food/{foodId}

# Get user reviews
GET /api/reviews/user
Authorization: Bearer <token>

# Update review
PUT /api/reviews/{id}
Authorization: Bearer <token>

# Delete review
DELETE /api/reviews/{id}
Authorization: Bearer <token>
```

### **Analytics**
```bash
# Get restaurant rating stats
GET /api/reviews/restaurant/{restaurantId}/stats

# Get food rating stats
GET /api/reviews/food/{foodId}/stats

# Get overall rating
GET /api/reviews/rating/{entityId}
```

---

## 🗄️ **DATA MODELS**

### **Review Entity**
```java
@Data
@Document(collection = "reviews")
public class ReviewEntity {
    @Id
    private String id;
    
    private String userId;
    private String restaurantId;
    private String foodId;
    private Integer rating;
    private String comment;
    private ReviewType reviewType;
    
    @Builder.Default
    private boolean isApproved = true;
    
    @Builder.Default
    private boolean isModerated = false;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

---

## 🎯 **BEST PRACTICES**

### **1. Review Quality**
- Implement content moderation
- Validate rating ranges
- Handle spam detection

### **2. Performance**
- Cache rating calculations
- Optimize aggregation queries
- Use proper indexing

---

**Built with ❤️ using Spring Boot & MongoDB**

