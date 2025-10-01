# 🚪 **API GATEWAY**

> Single Entry Point for Foodingo Microservices Architecture

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Gateway](https://img.shields.io/badge/Gateway-Spring%20Cloud-green.svg)](https://spring.io/projects/spring-cloud-gateway)

---

## 📋 **OVERVIEW**

API Gateway is the **Single Entry Point** for all client requests to the Foodingo microservices architecture. It provides routing, load balancing, security, and cross-cutting concerns for all microservices.

### **Key Features:**
- 🛣️ **Request Routing**: Route requests to appropriate microservices
- ⚖️ **Load Balancing**: Distribute load across service instances
- 🔒 **Security**: Authentication, authorization, and rate limiting
- 🔄 **Circuit Breaker**: Fault tolerance and resilience
- 📊 **Monitoring**: Request/response logging and metrics
- 🚦 **Rate Limiting**: Prevent API abuse and ensure fair usage
- 🔍 **Request/Response Transformation**: Modify requests and responses

---

## 🏗️ **ARCHITECTURE**

```
┌─────────────────────────────────────────────────────────────┐
│                    API GATEWAY                             │
│                     Port: 8080                             │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Route         │  │   Load          │                  │
│  │   Handler       │  │   Balancer      │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Circuit        │  │   Rate          │                  │
│  │   Breaker        │  │   Limiter       │                  │
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
- Eureka Server running
- Redis (for rate limiting)

### **1. Build the Service**
```bash
cd api-gateway
mvn clean install
```

### **2. Run the Service**
```bash
mvn spring-boot:run
```

### **3. Access Gateway**
- **API Gateway**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## ⚙️ **CONFIGURATION**

### **Application Properties**
```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # User Service Routes
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: user-service-circuit
                fallbackUri: forward:/fallback/user-service
        
        # Restaurant Service Routes
        - id: restaurant-service
          uri: lb://restaurant-service
          predicates:
            - Path=/api/restaurants/**,/api/foods/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: restaurant-service-circuit
                fallbackUri: forward:/fallback/restaurant-service
        
        # Order Service Routes
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: order-service-circuit
                fallbackUri: forward:/fallback/order-service
        
        # Payment Service Routes
        - id: payment-service
          uri: lb://payment-service
          predicates:
            - Path=/api/payments/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: payment-service-circuit
                fallbackUri: forward:/fallback/payment-service
        
        # Review Service Routes
        - id: review-service
          uri: lb://review-service
          predicates:
            - Path=/api/reviews/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: review-service-circuit
                fallbackUri: forward:/fallback/review-service
        
        # Notification Service Routes
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: notification-service-circuit
                fallbackUri: forward:/fallback/notification-service
        
        # Loyalty Service Routes
        - id: loyalty-service
          uri: lb://loyalty-service
          predicates:
            - Path=/api/loyalty/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: loyalty-service-circuit
                fallbackUri: forward:/fallback/loyalty-service
        
        # Coupon Service Routes
        - id: coupon-service
          uri: lb://coupon-service
          predicates:
            - Path=/api/coupons/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: coupon-service-circuit
                fallbackUri: forward:/fallback/coupon-service
      
      # Global Filters
      default-filters:
        - name: RequestRateLimiter
          args:
            redis-rate-limiter.replenishRate: 10
            redis-rate-limiter.burstCapacity: 20
            redis-rate-limiter.requestedTokens: 1
        - name: Retry
          args:
            retries: 3
            methods: GET,POST
            backoff:
              firstBackoff: 50ms
              maxBackoff: 500ms
              factor: 2

  # Redis Configuration
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2000ms

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
        include: health,info,metrics,prometheus,gateway
  tracing:
    sampling:
      probability: 1.0
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans

# Logging Configuration
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.web.reactive: DEBUG
```

---

## 🛣️ **ROUTING CONFIGURATION**

### **Route Definitions**
```yaml
spring:
  cloud:
    gateway:
      routes:
        # User Service
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1
            - AddRequestHeader=X-Gateway-Source, api-gateway
        
        # Restaurant Service
        - id: restaurant-service
          uri: lb://restaurant-service
          predicates:
            - Path=/api/restaurants/**,/api/foods/**
          filters:
            - StripPrefix=1
            - AddRequestHeader=X-Gateway-Source, api-gateway
        
        # Order Service
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
          filters:
            - StripPrefix=1
            - AddRequestHeader=X-Gateway-Source, api-gateway
```

### **Predicate Examples**
```yaml
# Path-based routing
predicates:
  - Path=/api/user/**

# Method-based routing
predicates:
  - Method=GET,POST

# Header-based routing
predicates:
  - Header=X-Request-Type, mobile

# Query parameter routing
predicates:
  - Query=version, v2

# Host-based routing
predicates:
  - Host=api.foodingo.com

# Weight-based routing
predicates:
  - Weight=group1, 80
```

### **Filter Examples**
```yaml
# Request/Response modification
filters:
  - AddRequestHeader=X-Gateway-Source, api-gateway
  - AddResponseHeader=X-Response-Time, ${T(java.time.Instant).now()}
  - RemoveRequestHeader=X-Secret-Header
  - RewritePath=/api/(?<segment>.*), /$\{segment}

# Circuit breaker
filters:
  - name: CircuitBreaker
    args:
      name: user-service-circuit
      fallbackUri: forward:/fallback/user-service

# Rate limiting
filters:
  - name: RequestRateLimiter
    args:
      redis-rate-limiter.replenishRate: 10
      redis-rate-limiter.burstCapacity: 20
```

---

## 🔒 **SECURITY CONFIGURATION**

### **JWT Authentication Filter**
```java
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // Skip authentication for public endpoints
        if (isPublicEndpoint(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        
        // Extract JWT token
        String token = extractToken(request);
        
        if (token == null || !isValidToken(token)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        
        // Add user info to request headers
        ServerHttpRequest modifiedRequest = request.mutate()
            .header("X-User-Id", getUserIdFromToken(token))
            .build();
        
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
    
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/user/register") ||
               path.startsWith("/api/user/login") ||
               path.startsWith("/api/restaurants") ||
               path.startsWith("/api/foods") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/actuator/health");
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}
```

### **CORS Configuration**
```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}
```

---

## 🚦 **RATE LIMITING**

### **Redis-based Rate Limiting**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
```

### **Key Resolver**
```java
@Bean
public KeyResolver userKeyResolver() {
    return exchange -> Mono.just(
        exchange.getRequest().getHeaders().getFirst("X-User-Id") != null ?
        exchange.getRequest().getHeaders().getFirst("X-User-Id") :
        exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
    );
}
```

---

## 🔄 **CIRCUIT BREAKER**

### **Circuit Breaker Configuration**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      user-service-circuit:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        automatic-transition-from-open-to-half-open-enabled: true
```

### **Fallback Handler**
```java
@RestController
public class FallbackController {
    
    @GetMapping("/fallback/user-service")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User service is temporarily unavailable");
        response.put("status", "SERVICE_DOWN");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/fallback/restaurant-service")
    public ResponseEntity<Map<String, String>> restaurantServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Restaurant service is temporarily unavailable");
        response.put("status", "SERVICE_DOWN");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
```

---

## 📊 **MONITORING & LOGGING**

### **Request Logging**
```java
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        logger.info("Request: {} {}", request.getMethod(), request.getURI());
        logger.info("Headers: {}", request.getHeaders());
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            logger.info("Response: {}", response.getStatusCode());
        }));
    }
    
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
```

### **Metrics Configuration**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,gateway
  metrics:
    export:
      prometheus:
        enabled: true
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
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Docker Compose**
```yaml
api-gateway:
  build: ./api-gateway
  container_name: api-gateway
  ports:
    - "8080:8080"
  environment:
    - SERVER_PORT=8080
    - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka/
    - SPRING_DATA_REDIS_HOST=redis
    - SPRING_DATA_REDIS_PORT=6379
  depends_on:
    - eureka-server
    - redis
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 5
```

---

## 🔧 **API ENDPOINTS**

### **Gateway Management**
```bash
# Get all routes
GET /actuator/gateway/routes

# Get specific route
GET /actuator/gateway/routes/{route-id}

# Refresh routes
POST /actuator/gateway/refresh

# Get route filters
GET /actuator/gateway/routefilters

# Get global filters
GET /actuator/gateway/globalfilters
```

### **Service Routes**
```bash
# User Service
GET /api/user/profile
POST /api/user/register
POST /api/user/login

# Restaurant Service
GET /api/restaurants
GET /api/restaurants/{id}
GET /api/foods
GET /api/foods/restaurant/{restaurantId}

# Order Service
POST /api/orders
GET /api/orders/user
GET /api/orders/{id}

# Payment Service
POST /api/payments
POST /api/payments/verify
GET /api/payments/{id}

# Review Service
GET /api/reviews/restaurant/{restaurantId}
POST /api/reviews
GET /api/reviews/user

# Notification Service
GET /api/notifications/user
POST /api/notifications/send

# Loyalty Service
GET /api/loyalty/balance
POST /api/loyalty/earn
POST /api/loyalty/redeem

# Coupon Service
GET /api/coupons/available
POST /api/coupons/validate
POST /api/coupons/use
```

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues**

#### **1. Route Not Found**
```bash
# Check route configuration
curl http://localhost:8080/actuator/gateway/routes

# Verify service registration in Eureka
curl http://localhost:8761/eureka/apps
```

#### **2. Circuit Breaker Open**
```bash
# Check circuit breaker status
curl http://localhost:8080/actuator/health

# Monitor circuit breaker metrics
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls
```

#### **3. Rate Limiting Issues**
```bash
# Check Redis connection
redis-cli ping

# Monitor rate limiter metrics
curl http://localhost:8080/actuator/metrics/spring.cloud.gateway.requests
```

### **Logs**
```bash
# View logs
tail -f logs/api-gateway.log

# Key log patterns:
# - "Route matched"
# - "Circuit breaker opened"
# - "Rate limit exceeded"
```

---

## 📈 **PERFORMANCE TUNING**

### **JVM Settings**
```bash
# Production JVM settings
JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### **Gateway Configuration**
```yaml
spring:
  cloud:
    gateway:
      httpclient:
        connect-timeout: 1000
        response-timeout: 5000
        pool:
          max-connections: 500
          max-idle-time: 30s
```

---

## 🎯 **BEST PRACTICES**

### **1. Route Design**
- Use consistent URL patterns
- Implement proper error handling
- Use circuit breakers for resilience

### **2. Security**
- Implement JWT authentication
- Use HTTPS in production
- Implement proper CORS policies

### **3. Monitoring**
- Log all requests and responses
- Monitor circuit breaker status
- Track rate limiting metrics

### **4. Performance**
- Use connection pooling
- Implement caching where appropriate
- Monitor response times

---

## 📚 **INTEGRATION EXAMPLES**

### **Client Integration**
```javascript
// Frontend API calls
const apiClient = {
  baseURL: 'http://localhost:8080/api',
  
  async getUserProfile(token) {
    const response = await fetch(`${this.baseURL}/user/profile`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    return response.json();
  },
  
  async getRestaurants() {
    const response = await fetch(`${this.baseURL}/restaurants`);
    return response.json();
  },
  
  async createOrder(orderData, token) {
    const response = await fetch(`${this.baseURL}/orders`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData)
    });
    return response.json();
  }
};
```

---

## 📞 **SUPPORT**

For issues and questions:
- Check the [Spring Cloud Gateway documentation](https://spring.io/projects/spring-cloud-gateway)
- Review [Gateway GitHub repository](https://github.com/spring-cloud/spring-cloud-gateway)
- Open an issue in the project repository

---

## 📝 **CHANGELOG**

### **Version 1.0.0**
- Initial API Gateway setup
- Route configuration for all services
- JWT authentication integration
- Circuit breaker implementation
- Rate limiting with Redis
- CORS configuration
- Request/response logging
- Docker support

---

**Built with ❤️ using Spring Boot & Spring Cloud Gateway**

