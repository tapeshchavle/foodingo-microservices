# 🍽️ **RESTAURANT SERVICE**

> Restaurant and Food Management Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![AWS S3](https://img.shields.io/badge/AWS-S3-orange.svg)](https://aws.amazon.com/s3/)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)

---

## 📋 **OVERVIEW**

Restaurant Service manages **Restaurants** and **Food Items** for the Foodingo platform. It handles restaurant registration, food menu management, image uploads, and provides search capabilities.

### **Key Features:**
- 🏪 **Restaurant Management**: CRUD operations for restaurants
- 🍕 **Food Management**: Menu items and food details
- 📸 **Image Upload**: AWS S3 integration for food/restaurant images
- 🔍 **Search & Filter**: Advanced search capabilities
- 📊 **Analytics**: Restaurant performance metrics
- 🏷️ **Categorization**: Food categories and tags
- ⭐ **Rating Integration**: Integration with review service

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB
- AWS S3 Bucket
- Redis (for caching)

### **1. Build the Service**
```bash
cd restaurant-service
mvn clean install
```

### **2. Configure AWS S3**
```bash
export AWS_ACCESS_KEY=your_access_key
export AWS_SECRET_KEY=your_secret_key
export AWS_S3_BUCKET=your_bucket_name
```

### **3. Run the Service**
```bash
mvn spring-boot:run
```

### **4. Access Service**
- **Restaurant Service**: http://localhost:8082
- **Swagger UI**: http://localhost:8082/swagger-ui.html
- **Health Check**: http://localhost:8082/actuator/health

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8082

spring:
  application:
    name: restaurant-service
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/foodingo_restaurant
  redis:
    host: localhost
    port: 6379

# AWS S3 Configuration
aws:
  access-key: ${AWS_ACCESS_KEY}
  secret-key: ${AWS_SECRET_KEY}
  s3-bucket: ${AWS_S3_BUCKET}
  region: ap-south-1

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

---

## 🔧 **API ENDPOINTS**

### **Restaurant Management**
```bash
# Get all restaurants
GET /api/restaurants

# Get restaurant by ID
GET /api/restaurants/{id}

# Create restaurant (Admin only)
POST /api/restaurants
Authorization: Bearer <admin-token>
{
  "name": "Pizza Palace",
  "description": "Best pizza in town",
  "cuisineType": "Italian",
  "address": {
    "street": "123 Main St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "zipCode": "400001"
  },
  "contactInfo": {
    "phone": "1234567890",
    "email": "info@pizzapalace.com"
  },
  "operatingHours": {
    "monday": "10:00-22:00",
    "tuesday": "10:00-22:00"
  }
}

# Update restaurant
PUT /api/restaurants/{id}
Authorization: Bearer <token>

# Delete restaurant
DELETE /api/restaurants/{id}
Authorization: Bearer <admin-token>
```

### **Food Management**
```bash
# Get all foods
GET /api/foods

# Get food by ID
GET /api/foods/{id}

# Get foods by restaurant
GET /api/foods/restaurant/{restaurantId}

# Create food item
POST /api/foods
Authorization: Bearer <restaurant-owner-token>
{
  "name": "Margherita Pizza",
  "description": "Classic tomato and mozzarella pizza",
  "price": 299.00,
  "category": "Pizza",
  "restaurantId": "restaurant-123",
  "ingredients": ["Tomato", "Mozzarella", "Basil"],
  "nutritionInfo": {
    "calories": 250,
    "protein": 12,
    "carbs": 30,
    "fat": 8
  },
  "isVegetarian": true,
  "isAvailable": true
}

# Update food item
PUT /api/foods/{id}
Authorization: Bearer <token>

# Delete food item
DELETE /api/foods/{id}
Authorization: Bearer <token>
```

### **Image Upload**
```bash
# Upload restaurant image
POST /api/restaurants/{id}/image
Authorization: Bearer <token>
Content-Type: multipart/form-data
file: <image-file>

# Upload food image
POST /api/foods/{id}/image
Authorization: Bearer <token>
Content-Type: multipart/form-data
file: <image-file>
```

### **Search & Filter**
```bash
# Search restaurants
GET /api/restaurants/search?query=pizza&city=mumbai

# Filter by cuisine
GET /api/restaurants/cuisine/{cuisineType}

# Search foods
GET /api/foods/search?query=margherita&restaurantId=123

# Filter by category
GET /api/foods/category/{category}
```

---

## 🗄️ **DATA MODELS**

### **Restaurant Entity**
```java
@Data
@Document(collection = "restaurants")
public class RestaurantEntity {
    @Id
    private String id;
    
    @NotBlank
    private String name;
    
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInfo contactInfo;
    private OperatingHours operatingHours;
    private String imageUrl;
    
    @Builder.Default
    private boolean isActive = true;
    
    @Builder.Default
    private Double rating = 0.0;
    
    @Builder.Default
    private Integer reviewCount = 0;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### **Food Entity**
```java
@Data
@Document(collection = "foods")
public class FoodEntity {
    @Id
    private String id;
    
    @NotBlank
    private String name;
    
    private String description;
    private Double price;
    private String category;
    private String restaurantId;
    private List<String> ingredients;
    private NutritionInfo nutritionInfo;
    private String imageUrl;
    
    @Builder.Default
    private boolean isVegetarian = false;
    
    @Builder.Default
    private boolean isAvailable = true;
    
    @Builder.Default
    private Double rating = 0.0;
    
    @Builder.Default
    private Integer reviewCount = 0;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
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
EXPOSE 8082
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8082/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 📊 **MONITORING**

### **Health Checks**
```bash
curl http://localhost:8082/actuator/health
```

### **Metrics**
```bash
curl http://localhost:8082/actuator/metrics
```

---

## 🎯 **BEST PRACTICES**

### **1. Performance**
- Use Redis caching for frequently accessed data
- Implement proper database indexing
- Optimize image uploads

### **2. Security**
- Validate file uploads
- Implement proper authorization
- Sanitize user inputs

### **3. Data Management**
- Use proper data validation
- Implement soft deletes
- Handle image cleanup

---

**Built with ❤️ using Spring Boot, AWS S3 & MongoDB**

