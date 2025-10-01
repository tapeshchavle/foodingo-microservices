# 🔍 **EUREKA SERVER**

> Service Discovery Server for Foodingo Microservices Architecture

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Eureka](https://img.shields.io/badge/Eureka-Server-orange.svg)](https://github.com/Netflix/eureka)

---

## 📋 **OVERVIEW**

Eureka Server is the **Service Discovery** component of the Foodingo microservices architecture. It acts as a registry where all microservices register themselves and discover other services dynamically.

### **Key Features:**
- 🔍 **Service Registration**: Services automatically register themselves
- 🔎 **Service Discovery**: Services can find other services by name
- 💓 **Health Monitoring**: Continuous health checks of registered services
- 🔄 **Load Balancing**: Client-side load balancing support
- 📊 **Dashboard**: Web UI for monitoring registered services

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────────────┐
│                    EUREKA SERVER                            │
│                     Port: 8761                             │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Registry      │  │   Dashboard     │                  │
│  │   Service       │  │   Web UI         │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Health        │  │   Replication   │                  │
│  │   Monitoring    │  │   Service       │                  │
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

### **1. Build the Service**
```bash
cd eureka-server
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Dashboard**
- **Eureka Dashboard**: http://localhost:8761
- **Health Check**: http://localhost:8761/actuator/health

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    wait-time-in-ms-when-sync-empty: 0
    enable-self-preservation: false

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### **Environment Variables**
```bash
# Server Configuration
SERVER_PORT=8761
EUREKA_INSTANCE_HOSTNAME=localhost

# Management
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics
```

---

## 🔧 **API ENDPOINTS**

### **Service Registry Endpoints**
```bash
# Get all registered services
GET /eureka/apps

# Get specific service instances
GET /eureka/apps/{service-name}

# Register a service instance
POST /eureka/apps/{service-name}

# Renew service registration
PUT /eureka/apps/{service-name}/{instance-id}

# Cancel service registration
DELETE /eureka/apps/{service-name}/{instance-id}
```

### **Management Endpoints**
```bash
# Health check
GET /actuator/health

# Service info
GET /actuator/info

# Metrics
GET /actuator/metrics
```

---

## 📊 **MONITORING**

### **Eureka Dashboard**
Access the web dashboard at `http://localhost:8761` to:
- View all registered services
- Monitor service health status
- Check service instances
- View service metadata

### **Health Checks**
```bash
# Check Eureka server health
curl http://localhost:8761/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "eurekaServer": {
      "status": "UP"
    }
  }
}
```

### **Metrics**
```bash
# Get Eureka metrics
curl http://localhost:8761/actuator/metrics

# Key metrics:
# - eureka.registry.size
# - eureka.registry.renewals
# - eureka.registry.cancellations
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
EXPOSE 8761
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8761/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Docker Compose**
```yaml
eureka-server:
  build: ./eureka-server
  container_name: eureka-server
  ports:
    - "8761:8761"
  environment:
    - SERVER_PORT=8761
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 5
```

---

## 🔒 **SECURITY**

### **Basic Authentication** (Optional)
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://admin:password@localhost:8761/eureka/
```

### **HTTPS Configuration** (Production)
```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
```

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues**

#### **1. Service Not Registering**
```bash
# Check Eureka server logs
docker logs eureka-server

# Verify service configuration
curl http://localhost:8761/eureka/apps
```

#### **2. Service Discovery Failing**
```bash
# Check service registration
curl http://localhost:8761/eureka/apps/{service-name}

# Verify client configuration
# Ensure eureka.client.service-url.defaultZone is correct
```

#### **3. High Memory Usage**
```yaml
# Configure JVM settings
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
```

### **Logs**
```bash
# View logs
tail -f logs/eureka-server.log

# Key log patterns:
# - "Started EurekaServerApplication"
# - "Registered instance"
# - "Renewed lease"
```

---

## 📈 **PERFORMANCE TUNING**

### **JVM Settings**
```bash
# Production JVM settings
JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### **Eureka Configuration**
```yaml
eureka:
  server:
    # Reduce wait time for empty registry
    wait-time-in-ms-when-sync-empty: 0
    # Disable self-preservation in development
    enable-self-preservation: false
    # Eviction interval
    eviction-interval-timer-in-ms: 60000
```

---

## 🔄 **CLUSTERING**

### **Multi-Instance Setup**
```yaml
# Instance 1
eureka:
  instance:
    hostname: eureka1.example.com
  client:
    service-url:
      defaultZone: http://eureka2.example.com:8761/eureka/

# Instance 2
eureka:
  instance:
    hostname: eureka2.example.com
  client:
    service-url:
      defaultZone: http://eureka1.example.com:8761/eureka/
```

---

## 📚 **INTEGRATION**

### **Service Registration**
```java
@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

### **Service Discovery**
```java
@RestController
public class UserController {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    @GetMapping("/services")
    public List<ServiceInstance> getServices() {
        return discoveryClient.getInstances("restaurant-service");
    }
}
```

---

## 🎯 **BEST PRACTICES**

### **1. Service Naming**
- Use consistent naming convention
- Avoid special characters
- Use lowercase with hyphens

### **2. Health Checks**
- Implement proper health check endpoints
- Return meaningful health status
- Handle dependencies gracefully

### **3. Configuration**
- Use environment-specific configurations
- Externalize sensitive data
- Use configuration management

### **4. Monitoring**
- Set up proper monitoring
- Configure alerts for service failures
- Track service metrics

---

## 📞 **SUPPORT**

For issues and questions:
- Check the [Spring Cloud Netflix documentation](https://spring.io/projects/spring-cloud-netflix)
- Review [Eureka GitHub repository](https://github.com/Netflix/eureka)
- Open an issue in the project repository

---

## 📝 **CHANGELOG**

### **Version 1.0.0**
- Initial Eureka Server setup
- Basic service registration and discovery
- Web dashboard integration
- Health monitoring
- Docker support

---

**Built with ❤️ using Spring Boot & Netflix Eureka**

