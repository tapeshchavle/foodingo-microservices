# 👤 **USER SERVICE**

> User Management and Authentication Service for Foodingo Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue.svg)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-Token-orange.svg)](https://jwt.io/)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)

---

## 📋 **OVERVIEW**

User Service is responsible for **User Management** and **Authentication** in the Foodingo platform. It handles user registration, login, profile management, and JWT token generation.

### **Key Features:**
- 🔐 **JWT Authentication**: Secure token-based authentication
- 👥 **User Registration**: New user account creation
- 🔑 **Login/Logout**: User authentication and session management
- 👤 **Profile Management**: User profile CRUD operations
- 🛡️ **Role-Based Access Control**: RBAC with different user roles
- 🔒 **Password Security**: BCrypt password hashing
- 📧 **Email Verification**: Email validation and verification
- 🔄 **Event Publishing**: User events for other services

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────────────┐
│                    USER SERVICE                             │
│                     Port: 8081                             │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   JWT Token     │  │   User          │                  │
│  │   Generation    │  │   Repository    │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Password      │  │   Event         │                  │
│  │   Encryption    │  │   Publisher     │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
   ┌────▼────┐         ┌─────▼─────┐         ┌─────▼─────┐
   │MongoDB  │         │ RabbitMQ  │         │   Redis   │
   │Database │         │  Events   │         │  Cache    │
   └─────────┘         └───────────┘         └───────────┘
```

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- MongoDB
- RabbitMQ
- Redis (optional)

### **1. Build the Service**
```bash
cd user-service
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Service**
- **User Service**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **Health Check**: http://localhost:8081/actuator/health

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8081

spring:
  application:
    name: user-service
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/foodingo_user
      auto-index-creation: true
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123
  security:
    user:
      name: admin
      password: admin123

# JWT Configuration
jwt:
  secret: your-jwt-secret-key-here-make-it-long-and-secure
  expiration: 86400000  # 24 hours in milliseconds

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
    com.foodingo.user: DEBUG
    org.springframework.security: DEBUG
```

### **Environment Variables**
```bash
# Server Configuration
SERVER_PORT=8081

# Database Configuration
MONGODB_URI=mongodb://admin:admin123@localhost:27017/foodingo_user

# JWT Configuration
JWT_SECRET=your-jwt-secret-key-here-make-it-long-and-secure
JWT_EXPIRATION=86400000

# RabbitMQ Configuration
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=admin
RABBITMQ_PASSWORD=admin123

# Eureka Configuration
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
```

---

## 🔧 **API ENDPOINTS**

### **Authentication Endpoints**
```bash
# User Registration
POST /api/user/register
Content-Type: application/json
{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "name": "John Doe",
  "phoneNumber": "1234567890",
  "address": {
    "street": "123 Main St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "zipCode": "400001"
  }
}

# User Login
POST /api/user/login
Content-Type: application/json
{
  "email": "user@example.com",
  "password": "SecurePass123!"
}

# Response
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "user-id",
    "email": "user@example.com",
    "name": "John Doe",
    "roles": ["CUSTOMER"]
  }
}
```

### **Profile Management Endpoints**
```bash
# Get User Profile (requires JWT)
GET /api/user/profile
Authorization: Bearer <jwt-token>

# Update User Profile (requires JWT)
PUT /api/user/profile
Authorization: Bearer <jwt-token>
Content-Type: application/json
{
  "name": "John Smith",
  "phoneNumber": "9876543210",
  "address": {
    "street": "456 Oak Ave",
    "city": "Delhi",
    "state": "Delhi",
    "zipCode": "110001"
  }
}

# Delete User Account (requires JWT)
DELETE /api/user/profile
Authorization: Bearer <jwt-token>
```

### **Admin Endpoints**
```bash
# Get All Users (Admin only)
GET /api/user/admin/users
Authorization: Bearer <admin-jwt-token>

# Get User by ID (Admin only)
GET /api/user/admin/users/{userId}
Authorization: Bearer <admin-jwt-token>

# Update User Role (Admin only)
PUT /api/user/admin/users/{userId}/role
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json
{
  "role": "RESTAURANT_OWNER"
}

# Deactivate User (Admin only)
PUT /api/user/admin/users/{userId}/deactivate
Authorization: Bearer <admin-jwt-token>
```

---

## 🗄️ **DATA MODELS**

### **User Entity**
```java
@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String name;
    
    private String phoneNumber;
    
    private Address address;
    
    @Builder.Default
    private Set<UserRole> roles = Set.of(UserRole.CUSTOMER);
    
    @Builder.Default
    private boolean isActive = true;
    
    @Builder.Default
    private boolean isEmailVerified = false;
    
    private String emailVerificationToken;
    
    private LocalDateTime lastLoginAt;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### **Address Entity**
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country = "India";
}
```

### **User Roles**
```java
public enum UserRole {
    CUSTOMER,
    RESTAURANT_OWNER,
    ADMIN,
    DELIVERY_PARTNER
}
```

---

## 🔐 **SECURITY CONFIGURATION**

### **JWT Configuration**
```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private int expiration;
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

### **Security Configuration**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/register", "/api/user/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

---

## 📊 **MONITORING**

### **Health Checks**
```bash
# Check User Service health
curl http://localhost:8081/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP"
    },
    "rabbit": {
      "status": "UP"
    }
  }
}
```

### **Metrics**
```bash
# Get User Service metrics
curl http://localhost:8081/actuator/metrics

# Key metrics:
# - http.server.requests
# - jvm.memory.used
# - process.cpu.usage
# - spring.data.mongodb.connections
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
EXPOSE 8081
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Docker Compose**
```yaml
user-service:
  build: ./user-service
  container_name: user-service
  ports:
    - "8081:8081"
  environment:
    - SERVER_PORT=8081
    - MONGODB_URI=mongodb://admin:admin123@mongodb:27017/foodingo_user
    - RABBITMQ_HOST=rabbitmq
    - RABBITMQ_PORT=5672
    - RABBITMQ_USERNAME=admin
    - RABBITMQ_PASSWORD=admin123
    - JWT_SECRET=your-jwt-secret-key-here-make-it-long-and-secure
    - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka/
  depends_on:
    - mongodb
    - rabbitmq
    - eureka-server
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 5
```

---

## 🔄 **EVENT PUBLISHING**

### **User Events**
```java
@Component
public class UserEventPublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void publishUserRegistered(UserEntity user) {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .timestamp(LocalDateTime.now())
            .build();
        
        rabbitTemplate.convertAndSend("user.exchange", "user.registered", event);
    }
    
    public void publishUserUpdated(UserEntity user) {
        UserUpdatedEvent event = UserUpdatedEvent.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .timestamp(LocalDateTime.now())
            .build();
        
        rabbitTemplate.convertAndSend("user.exchange", "user.updated", event);
    }
}
```

### **Event Models**
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private String userId;
    private String email;
    private String name;
    private LocalDateTime timestamp;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedEvent {
    private String userId;
    private String email;
    private String name;
    private LocalDateTime timestamp;
}
```

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues**

#### **1. Authentication Failures**
```bash
# Check JWT token validity
curl -H "Authorization: Bearer <token>" http://localhost:8081/api/user/profile

# Verify JWT secret configuration
echo $JWT_SECRET
```

#### **2. Database Connection Issues**
```bash
# Check MongoDB connection
mongo mongodb://admin:admin123@localhost:27017/foodingo_user

# Verify database configuration
curl http://localhost:8081/actuator/health
```

#### **3. RabbitMQ Connection Issues**
```bash
# Check RabbitMQ connection
rabbitmqctl status

# Verify RabbitMQ configuration
curl http://localhost:8081/actuator/health
```

### **Logs**
```bash
# View logs
tail -f logs/user-service.log

# Key log patterns:
# - "User registered successfully"
# - "JWT token generated"
# - "Authentication successful"
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
      # Connection pool settings
      options:
        max-connections-per-host: 100
        threads-allowed-to-block-for-connection-multiplier: 5
        server-selection-timeout: 30000
        max-wait-time: 120000
        max-connection-idle-time: 0
        max-connection-life-time: 0
```

---

## 🎯 **BEST PRACTICES**

### **1. Security**
- Use strong JWT secrets
- Implement proper password policies
- Use HTTPS in production
- Implement rate limiting

### **2. Data Management**
- Use proper indexing on MongoDB
- Implement data validation
- Handle sensitive data carefully
- Implement audit logging

### **3. Performance**
- Use connection pooling
- Implement caching where appropriate
- Monitor database performance
- Optimize queries

### **4. Error Handling**
- Implement proper error responses
- Log security events
- Handle edge cases gracefully
- Provide meaningful error messages

---

## 📚 **INTEGRATION EXAMPLES**

### **Frontend Integration**
```javascript
// User registration
const registerUser = async (userData) => {
  const response = await fetch('http://localhost:8081/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData)
  });
  return response.json();
};

// User login
const loginUser = async (credentials) => {
  const response = await fetch('http://localhost:8081/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(credentials)
  });
  return response.json();
};

// Get user profile
const getUserProfile = async (token) => {
  const response = await fetch('http://localhost:8081/api/user/profile', {
    headers: {
      'Authorization': `Bearer ${token}`,
    }
  });
  return response.json();
};
```

### **Service Integration**
```java
@Service
public class OrderService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public UserEntity getUserById(String userId) {
        String url = "http://user-service/api/user/admin/users/" + userId;
        return restTemplate.getForObject(url, UserEntity.class);
    }
}
```

---

## 📞 **SUPPORT**

For issues and questions:
- Check the [Spring Security documentation](https://spring.io/projects/spring-security)
- Review [JWT documentation](https://jwt.io/)
- Open an issue in the project repository

---

## 📝 **CHANGELOG**

### **Version 1.0.0**
- Initial User Service setup
- JWT authentication implementation
- User registration and login
- Profile management
- Role-based access control
- Event publishing
- Docker support

---

**Built with ❤️ using Spring Boot, Spring Security & JWT**

