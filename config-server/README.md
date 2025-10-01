# ⚙️ **CONFIG SERVER**

> Centralized Configuration Management for Foodingo Microservices

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Config Server](https://img.shields.io/badge/Config-Server-green.svg)](https://spring.io/projects/spring-cloud-config)

---

## 📋 **OVERVIEW**

Config Server is the **Centralized Configuration Management** component of the Foodingo microservices architecture. It provides a centralized place to manage external properties for applications across all environments.

### **Key Features:**
- 🔧 **Centralized Configuration**: Single source of truth for all configurations
- 🌍 **Environment Management**: Support for multiple environments (dev, staging, prod)
- 🔄 **Dynamic Refresh**: Hot reload of configuration without restart
- 🔐 **Security**: Encrypt/decrypt sensitive configuration values
- 📁 **Multiple Backends**: Git, SVN, File System, Database support
- 🔍 **Version Control**: Track configuration changes over time

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────────────┐
│                    CONFIG SERVER                           │
│                     Port: 8888                             │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Git Backend   │  │   File System   │                  │
│  │   Repository    │  │   Backend       │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Encryption    │  │   Environment   │                  │
│  │   Service       │  │   Profiles      │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
   ┌────▼────┐         ┌─────▼─────┐         ┌─────▼─────┐
   │  User   │         │Restaurant │         │   Order   │
   │Service  │         │ Service   │         │ Service   │
   └─────────┘         └───────────┘         └───────────┘
```

---

## 🚀 **QUICK START**

### **Prerequisites**
- Java 17+
- Maven 3.9+
- Git repository (optional)

### **1. Build the Service**
```bash
cd config-server
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Configuration**
- **Config Server**: http://localhost:8888
- **Health Check**: http://localhost:8888/actuator/health

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8888

spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/your-org/foodingo-config
          clone-on-start: true
          default-label: main
        # Alternative: File system backend
        # native:
        #   search-locations: classpath:/config
  profiles:
    active: native

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

management:
  endpoints:
    web:
      exposure:
        include: health,info,refresh,env
```

### **Environment Variables**
```bash
# Server Configuration
SERVER_PORT=8888

# Git Configuration
SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/your-org/foodingo-config
SPRING_CLOUD_CONFIG_SERVER_GIT_DEFAULT_LABEL=main

# Eureka Configuration
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
```

---

## 📁 **CONFIGURATION FILES STRUCTURE**

### **Git Repository Structure**
```
foodingo-config/
├── application.yml                 # Common configuration
├── application-dev.yml            # Development environment
├── application-staging.yml         # Staging environment
├── application-prod.yml           # Production environment
├── user-service.yml               # User service specific
├── restaurant-service.yml         # Restaurant service specific
├── order-service.yml              # Order service specific
├── payment-service.yml            # Payment service specific
├── review-service.yml             # Review service specific
├── notification-service.yml       # Notification service specific
├── loyalty-service.yml            # Loyalty service specific
└── coupon-service.yml             # Coupon service specific
```

### **Example Configuration Files**

#### **application.yml** (Common)
```yaml
# Common configuration for all services
spring:
  data:
    mongodb:
      auto-index-creation: true
  
  redis:
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  tracing:
    sampling:
      probability: 1.0

logging:
  level:
    com.foodingo: INFO
```

#### **user-service.yml**
```yaml
# User Service specific configuration
server:
  port: 8081

spring:
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/foodingo_user
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123

jwt:
  secret: your-jwt-secret-key-here
  expiration: 86400000  # 24 hours

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### **application-prod.yml** (Production)
```yaml
# Production environment configuration
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}
  redis:
    host: ${REDIS_HOST}
    port: ${REDIS_PORT}
  rabbitmq:
    host: ${RABBITMQ_HOST}
    port: ${RABBITMQ_PORT}
    username: ${RABBITMQ_USERNAME}
    password: ${RABBITMQ_PASSWORD}

jwt:
  secret: ${JWT_SECRET}

razorpay:
  key: ${RAZORPAY_KEY}
  secret: ${RAZORPAY_SECRET}

aws:
  access-key: ${AWS_ACCESS_KEY}
  secret-key: ${AWS_SECRET_KEY}
  s3-bucket: ${AWS_S3_BUCKET}

logging:
  level:
    root: WARN
    com.foodingo: INFO
```

---

## 🔧 **API ENDPOINTS**

### **Configuration Endpoints**
```bash
# Get configuration for application
GET /{application}/{profile}[/{label}]

# Get configuration for application with label
GET /{application}/{profile}/{label}

# Get configuration for multiple applications
GET /{application}/{profile1},{profile2}

# Get configuration with default label
GET /{application}/{profile}
```

### **Examples**
```bash
# Get user-service configuration for dev environment
GET /user-service/dev

# Get restaurant-service configuration for prod environment
GET /restaurant-service/prod

# Get common configuration for all services
GET /application/dev

# Get configuration with specific label
GET /user-service/dev/main
```

### **Management Endpoints**
```bash
# Health check
GET /actuator/health

# Service info
GET /actuator/info

# Refresh configuration (for clients)
POST /actuator/refresh

# Environment info
GET /actuator/env
```

---

## 🔄 **DYNAMIC REFRESH**

### **Client-Side Configuration**
```yaml
# In client service application.yml
management:
  endpoints:
    web:
      exposure:
        include: refresh

spring:
  cloud:
    config:
      uri: http://localhost:8888
      name: user-service
      profile: dev
      label: main
```

### **Refresh Endpoint**
```bash
# Refresh configuration for a specific service
POST http://localhost:8081/actuator/refresh

# Response
[
  "config.client.version",
  "spring.datasource.url"
]
```

### **RefreshScope Annotation**
```java
@RestController
@RefreshScope
public class UserController {
    
    @Value("${custom.property}")
    private String customProperty;
    
    @GetMapping("/config")
    public String getConfig() {
        return customProperty;
    }
}
```

---

## 🔐 **ENCRYPTION & SECURITY**

### **Encryption Setup**
```bash
# Generate encryption key
keytool -genkeypair -alias config-server -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore config-server.p12 -validity 3650

# Set encryption key
export ENCRYPT_KEY=your-encryption-key-here
```

### **Encrypting Values**
```bash
# Encrypt a value
curl -X POST http://localhost:8888/encrypt -d "sensitive-value"

# Response: encrypted-value

# Decrypt a value
curl -X POST http://localhost:8888/decrypt -d "encrypted-value"

# Response: sensitive-value
```

### **Using Encrypted Values**
```yaml
# In configuration files
database:
  password: '{cipher}encrypted-password-here'
  
jwt:
  secret: '{cipher}encrypted-jwt-secret-here'
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
EXPOSE 8888
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8888/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Docker Compose**
```yaml
config-server:
  build: ./config-server
  container_name: config-server
  ports:
    - "8888:8888"
  environment:
    - SERVER_PORT=8888
    - SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/your-org/foodingo-config
    - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka/
  depends_on:
    - eureka-server
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8888/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 5
```

---

## 🔗 **CLIENT INTEGRATION**

### **Spring Boot Client Configuration**
```yaml
# bootstrap.yml (in client service)
spring:
  application:
    name: user-service
  cloud:
    config:
      uri: http://localhost:8888
      profile: dev
      label: main
      fail-fast: true
      retry:
        initial-interval: 1000
        max-attempts: 6
        max-interval: 2000
        multiplier: 1.1
```

### **Client Dependencies**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### **Client Application**
```java
@SpringBootApplication
@EnableConfigServer
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

---

## 📊 **MONITORING**

### **Health Checks**
```bash
# Check Config Server health
curl http://localhost:8888/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "configServer": {
      "status": "UP"
    }
  }
}
```

### **Configuration Info**
```bash
# Get configuration for a service
curl http://localhost:8888/user-service/dev

# Response
{
  "name": "user-service",
  "profiles": ["dev"],
  "label": "main",
  "version": "abc123",
  "state": null,
  "propertySources": [
    {
      "name": "https://github.com/your-org/foodingo-config/user-service.yml",
      "source": {
        "server.port": 8081,
        "spring.data.mongodb.uri": "mongodb://admin:admin123@localhost:27017/foodingo_user"
      }
    }
  ]
}
```

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues**

#### **1. Configuration Not Loading**
```bash
# Check Config Server logs
docker logs config-server

# Verify Git repository access
curl http://localhost:8888/actuator/env
```

#### **2. Client Cannot Connect**
```bash
# Check client configuration
# Verify bootstrap.yml or application.yml
# Ensure Config Server is running
curl http://localhost:8888/actuator/health
```

#### **3. Encryption Issues**
```bash
# Check encryption key
curl -X POST http://localhost:8888/encrypt -d "test"

# Verify keystore configuration
# Check ENCRYPT_KEY environment variable
```

### **Logs**
```bash
# View logs
tail -f logs/config-server.log

# Key log patterns:
# - "Started ConfigServerApplication"
# - "Located property source"
# - "Configuration loaded"
```

---

## 📈 **PERFORMANCE TUNING**

### **JVM Settings**
```bash
# Production JVM settings
JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC"
```

### **Git Configuration**
```yaml
spring:
  cloud:
    config:
      server:
        git:
          # Clone repository on startup
          clone-on-start: true
          # Force pull on refresh
          force-pull: true
          # Timeout for git operations
          timeout: 10
          # Default label
          default-label: main
```

---

## 🔄 **BACKUP & RECOVERY**

### **Configuration Backup**
```bash
# Backup configuration repository
git clone https://github.com/your-org/foodingo-config.git
tar -czf config-backup-$(date +%Y%m%d).tar.gz foodingo-config/
```

### **Disaster Recovery**
```bash
# Restore from backup
tar -xzf config-backup-20240101.tar.gz
cd foodingo-config
git remote add origin https://github.com/your-org/foodingo-config.git
git push -u origin main
```

---

## 🎯 **BEST PRACTICES**

### **1. Configuration Organization**
- Use environment-specific profiles
- Separate common and service-specific configs
- Use meaningful property names

### **2. Security**
- Encrypt sensitive values
- Use environment variables for secrets
- Implement proper access controls

### **3. Version Control**
- Track all configuration changes
- Use meaningful commit messages
- Implement configuration review process

### **4. Monitoring**
- Monitor configuration changes
- Set up alerts for failures
- Track configuration usage

---

## 📚 **INTEGRATION EXAMPLES**

### **Service Configuration**
```java
@Configuration
@ConfigurationProperties(prefix = "database")
public class DatabaseConfig {
    private String url;
    private String username;
    private String password;
    
    // Getters and setters
}

@RestController
public class ConfigController {
    
    @Value("${custom.property:default-value}")
    private String customProperty;
    
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("customProperty", customProperty);
        return config;
    }
}
```

---

## 📞 **SUPPORT**

For issues and questions:
- Check the [Spring Cloud Config documentation](https://spring.io/projects/spring-cloud-config)
- Review [Config Server GitHub repository](https://github.com/spring-cloud/spring-cloud-config)
- Open an issue in the project repository

---

## 📝 **CHANGELOG**

### **Version 1.0.0**
- Initial Config Server setup
- Git backend integration
- Environment profile support
- Encryption/decryption service
- Client integration support
- Docker support

---

**Built with ❤️ using Spring Boot & Spring Cloud Config**

