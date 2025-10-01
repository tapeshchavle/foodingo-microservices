# 📘 COMPLETE GUIDE: Creating Remaining 5 Services from Your Monolith

## 🎯 Overview

You have **3 complete services** (User, Restaurant, Order). 
Your monolith has ALL the business logic for the remaining 5 services.

**This guide shows you how to extract and create:**
1. Review Service (Port 8085)
2. Notification Service (Port 8086)
3. Loyalty Service (Port 8087)
4. Coupon Service (Port 8088)
5. Payment Service (Port 8084)

**Time Required: 30-45 minutes for all 5 services**

---

## 🔥 FASTEST METHOD: Batch Copy Script

### Windows PowerShell Script

Save this as `copy-services.ps1` in `foodingo-microservices/`:

```powershell
# Foodingo Service Creator - Batch Copy from Monolith
$monolithBase = "C:\Tapesh\foodingo\foodingo\src\main\java\com\food"
$microservicesBase = "C:\Tapesh\foodingo\foodingo-microservices"

function Copy-ServiceFile {
    param($source, $dest, $serviceName)
    
    if (Test-Path $source) {
        $content = Get-Content $source -Raw
        $content = $content -replace 'package com\.food\.', "package com.foodingo.$serviceName."
        $content = $content -replace 'import com\.food\.', "import com.foodingo.$serviceName."
        
        New-Item -Path (Split-Path $dest) -ItemType Directory -Force | Out-Null
        Set-Content $dest $content
        Write-Host "  ✓ Copied $([System.IO.Path]::GetFileName($source))" -ForegroundColor Green
    }
}

# Review Service
Write-Host "`nCreating Review Service..." -ForegroundColor Cyan
Copy-ServiceFile "$monolithBase\entity\ReviewEntity.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\entity\ReviewEntity.java" "review"
Copy-ServiceFile "$monolithBase\repository\ReviewRepository.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\repository\ReviewRepository.java" "review"
Copy-ServiceFile "$monolithBase\service\ReviewService.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\service\ReviewService.java" "review"
Copy-ServiceFile "$monolithBase\service\impl\ReviewServiceImpl.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\service\impl\ReviewServiceImpl.java" "review"
Copy-ServiceFile "$monolithBase\controller\ReviewController.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\controller\ReviewController.java" "review"
Copy-ServiceFile "$monolithBase\io\ReviewRequest.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\dto\ReviewRequest.java" "review"
Copy-ServiceFile "$monolithBase\io\ReviewResponse.java" "$microservicesBase\review-service\src\main\java\com\foodingo\review\dto\ReviewResponse.java" "review"

# Notification Service
Write-Host "`nCreating Notification Service..." -ForegroundColor Cyan
Copy-ServiceFile "$monolithBase\entity\NotificationEntity.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\entity\NotificationEntity.java" "notification"
Copy-ServiceFile "$monolithBase\repository\NotificationRepository.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\repository\NotificationRepository.java" "notification"
Copy-ServiceFile "$monolithBase\service\NotificationService.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\service\NotificationService.java" "notification"
Copy-ServiceFile "$monolithBase\service\impl\NotificationServiceImpl.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\service\impl\NotificationServiceImpl.java" "notification"
Copy-ServiceFile "$monolithBase\controller\NotificationController.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\controller\NotificationController.java" "notification"
Copy-ServiceFile "$monolithBase\io\NotificationResponse.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\dto\NotificationResponse.java" "notification"
Copy-ServiceFile "$monolithBase\enums\NotificationType.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\enums\NotificationType.java" "notification"
Copy-ServiceFile "$monolithBase\config\WebSocketConfig.java" "$microservicesBase\notification-service\src\main\java\com\foodingo\notification\config\WebSocketConfig.java" "notification"

# Loyalty Service
Write-Host "`nCreating Loyalty Service..." -ForegroundColor Cyan
Copy-ServiceFile "$monolithBase\entity\LoyaltyTransactionEntity.java" "$microservicesBase\loyalty-service\src\main\java\com\foodingo\loyalty\entity\LoyaltyTransactionEntity.java" "loyalty"
Copy-ServiceFile "$monolithBase\repository\LoyaltyTransactionRepository.java" "$microservicesBase\loyalty-service\src\main\java\com\foodingo\loyalty\repository\LoyaltyTransactionRepository.java" "loyalty"
Copy-ServiceFile "$monolithBase\service\LoyaltyService.java" "$microservicesBase\loyalty-service\src\main\java\com\foodingo\loyalty\service\LoyaltyService.java" "loyalty"
Copy-ServiceFile "$monolithBase\service\impl\LoyaltyServiceImpl.java" "$microservicesBase\loyalty-service\src\main\java\com\foodingo\loyalty\service\impl\LoyaltyServiceImpl.java" "loyalty"
Copy-ServiceFile "$monolithBase\controller\LoyaltyController.java" "$microservicesBase\loyalty-service\src\main\java\com\foodingo\loyalty\controller\LoyaltyController.java" "loyalty"

# Coupon Service
Write-Host "`nCreating Coupon Service..." -ForegroundColor Cyan
Copy-ServiceFile "$monolithBase\entity\CouponEntity.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\entity\CouponEntity.java" "coupon"
Copy-ServiceFile "$monolithBase\repository\CouponRepository.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\repository\CouponRepository.java" "coupon"
Copy-ServiceFile "$monolithBase\service\CouponService.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\service\CouponService.java" "coupon"
Copy-ServiceFile "$monolithBase\service\impl\CouponServiceImpl.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\service\impl\CouponServiceImpl.java" "coupon"
Copy-ServiceFile "$monolithBase\controller\CouponController.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\controller\CouponController.java" "coupon"
Copy-ServiceFile "$monolithBase\io\CouponRequest.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\dto\CouponRequest.java" "coupon"
Copy-ServiceFile "$monolithBase\io\CouponResponse.java" "$microservicesBase\coupon-service\src\main\java\com\foodingo\coupon\dto\CouponResponse.java" "coupon"

Write-Host "`n✅ All services created! Now create config files..." -ForegroundColor Green
```

---

## 📝 MANUAL METHOD (if script doesn't work)

### Review Service (Port 8085)

#### Step 1: Create Directory Structure

```powershell
cd c:\Tapesh\foodingo\foodingo-microservices\review-service
mkdir -p src\main\java\com\foodingo\review\entity
mkdir -p src\main\java\com\foodingo\review\repository
mkdir -p src\main\java\com\foodingo\review\service\impl
mkdir -p src\main\java\com\foodingo\review\controller
mkdir -p src\main\java\com\foodingo\review\dto
mkdir -p src\main\resources
```

#### Step 2: Copy Files

| From Monolith | To Review Service | Action |
|---------------|-------------------|--------|
| `entity/ReviewEntity.java` | `entity/ReviewEntity.java` | Copy & Update package |
| `repository/ReviewRepository.java` | `repository/ReviewRepository.java` | Copy & Update |
| `service/ReviewService.java` | `service/ReviewService.java` | Copy & Update |
| `service/impl/ReviewServiceImpl.java` | `service/impl/ReviewServiceImpl.java` | Copy & Update |
| `controller/ReviewController.java` | `controller/ReviewController.java` | Copy & Update |
| `io/ReviewRequest.java` | `dto/ReviewRequest.java` | Copy & Update |
| `io/ReviewResponse.java` | `dto/ReviewResponse.java` | Copy & Update |

#### Step 3: Update Package Names

In each copied file, find & replace:
```
Find: package com.food.
Replace: package com.foodingo.review.

Find: import com.food.
Replace: import com.foodingo.review.
```

#### Step 4: Create pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.foodingo</groupId>
        <artifactId>foodingo-microservices</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>review-service</artifactId>
    <name>Review Service</name>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>3.2.0</version>
        </dependency>
    </dependencies>
</project>
```

#### Step 5: Create application.yml

```yaml
spring:
  application:
    name: review-service
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/foodingo_review}

server:
  port: ${SERVER_PORT:8085}

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER:http://localhost:8761/eureka/}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

#### Step 6: Create Application Class

```java
package com.foodingo.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
public class ReviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewServiceApplication.class, args);
    }
}
```

#### Step 7: Create Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ../pom.xml
COPY review-service/pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY review-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🔄 REPEAT FOR ALL SERVICES

Use the same pattern for:

### Notification Service (Port 8086)
- NotificationEntity
- NotificationRepository  
- NotificationService & impl
- NotificationController
- NotificationResponse
- NotificationType enum
- WebSocketConfig

### Loyalty Service (Port 8087)
- LoyaltyTransactionEntity
- LoyaltyTransactionRepository
- LoyaltyService & impl
- LoyaltyController

### Coupon Service (Port 8088)
- CouponEntity
- CouponRepository
- CouponService & impl
- CouponController
- CouponRequest & CouponResponse

### Payment Service (Port 8084)
- Extract payment logic from OrderServiceImpl
- Create PaymentService for Razorpay
- Add payment verification

---

## 🧪 TEST EACH SERVICE

After creating each service:

```bash
# Build
mvn clean install -DskipTests

# Run
mvn spring-boot:run

# Check Eureka
http://localhost:8761

# Test endpoint
curl http://localhost:8085/actuator/health
```

---

## 📋 CHECKLIST

Use this checklist for each service:

- [ ] Directory structure created
- [ ] Entity files copied & updated
- [ ] Repository files copied & updated
- [ ] Service interface copied & updated
- [ ] Service implementation copied & updated
- [ ] Controller copied & updated
- [ ] DTOs copied & updated
- [ ] Enum files copied (if applicable)
- [ ] Config files copied (if applicable)
- [ ] pom.xml created
- [ ] application.yml created
- [ ] Application class created
- [ ] Dockerfile created
- [ ] ModelMapperConfig added (if needed)
- [ ] Service builds successfully
- [ ] Service registers with Eureka
- [ ] Endpoints work

---

## ⚡ QUICK REFERENCE

### Package Name Conversions

| Monolith | Review Service |
|----------|----------------|
| `com.food.entity` | `com.foodingo.review.entity` |
| `com.food.repository` | `com.foodingo.review.repository` |
| `com.food.service` | `com.foodingo.review.service` |
| `com.food.controller` | `com.foodingo.review.controller` |
| `com.food.io` | `com.foodingo.review.dto` |
| `com.food.enums` | `com.foodingo.review.enums` |

### Service Ports

- Review: 8085
- Notification: 8086
- Loyalty: 8087  
- Coupon: 8088
- Payment: 8084

---

## 🎯 ESTIMATED TIME

- Review Service: 5-7 minutes
- Notification Service: 8-10 minutes (has WebSocket)
- Loyalty Service: 4-5 minutes
- Coupon Service: 5-7 minutes
- Payment Service: 6-8 minutes

**Total: 30-45 minutes for all 5 services**

---

## ❓ NEED HELP?

If anything is unclear or you encounter issues:

**Reply with: "Help with [service-name]"**

I'll create that specific service for you immediately!

---

## 🎉 WHEN YOU'RE DONE

You'll have **ALL 8 business microservices** running:

1. ✅ User Service
2. ✅ Restaurant Service
3. ✅ Order Service
4. ✅ Review Service (NEW)
5. ✅ Notification Service (NEW)
6. ✅ Loyalty Service (NEW)
7. ✅ Coupon Service (NEW)
8. ✅ Payment Service (NEW)

**Plus all infrastructure services = 11 total microservices!**

Your foodingo platform will be **fully operational**! 🚀

