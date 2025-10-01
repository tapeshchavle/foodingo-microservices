# Foodingo Microservices - Quick Start Guide 🚀

## Prerequisites

- Docker & Docker Compose installed
- Java 17+ (for local development)
- Maven 3.6+ (for local development)
- Minimum 8GB RAM
- 20GB free disk space

---

## 🏃 Quick Start (Docker Compose)

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/foodingo.git
cd foodingo/foodingo-microservices
```

### 2. Set Environment Variables
```bash
# Create .env file
cp .env.example .env

# Edit .env with your credentials
nano .env
```

### 3. Start All Services
```bash
# Build and start all services
docker-compose up -d

# Or build with no cache
docker-compose build --no-cache
docker-compose up -d
```

### 4. Check Service Health
```bash
# Check all running containers
docker-compose ps

# Check specific service logs
docker-compose logs -f api-gateway
docker-compose logs -f user-service
```

### 5. Access Services

| Service | URL | Purpose |
|---------|-----|---------|
| **API Gateway** | http://localhost:8080 | Main entry point |
| **Eureka Dashboard** | http://localhost:8761 | Service registry |
| **RabbitMQ Management** | http://localhost:15672 | Message queue (admin/admin123) |
| **Zipkin** | http://localhost:9411 | Distributed tracing |
| **Prometheus** | http://localhost:9090 | Metrics |
| **Grafana** | http://localhost:3000 | Monitoring (admin/admin123) |

---

## 📊 Service Startup Order

Services start in this order (automatic with Docker Compose):

```
1. Infrastructure Services
   ├── MongoDB (27017)
   ├── Redis (6379)
   ├── RabbitMQ (5672, 15672)
   └── Zipkin (9411)

2. Service Discovery & Config
   ├── Eureka Server (8761) - Wait for health check
   └── Config Server (8888) - Wait for Eureka

3. API Gateway (8080)
   └── Wait for Eureka & Redis

4. Business Services (parallel)
   ├── User Service (8081) x2 replicas
   ├── Restaurant Service (8082) x2 replicas
   ├── Order Service (8083) x3 replicas
   ├── Payment Service (8084) x2 replicas
   ├── Review Service (8085)
   ├── Notification Service (8086)
   ├── Loyalty Service (8087)
   └── Coupon Service (8088)

5. Monitoring (optional)
   ├── Prometheus (9090)
   └── Grafana (3000)
```

**Estimated Startup Time**: 3-5 minutes

---

## 🧪 Verify Installation

### 1. Check Eureka Dashboard
```bash
# Open browser
http://localhost:8761
```

You should see all services registered:
- API-GATEWAY
- USER-SERVICE (2 instances)
- RESTAURANT-SERVICE (2 instances)
- ORDER-SERVICE (3 instances)
- PAYMENT-SERVICE (2 instances)
- REVIEW-SERVICE
- NOTIFICATION-SERVICE
- LOYALTY-SERVICE
- COUPON-SERVICE

### 2. Test API Gateway
```bash
# Health check
curl http://localhost:8080/actuator/health

# Test user registration via gateway
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 3. Check Service Communication
```bash
# View distributed traces
http://localhost:9411

# Check service dependencies
# You should see traces showing:
# Gateway → User Service → Loyalty Service → Notification Service
```

---

## 🔧 Local Development Setup

### Build All Services
```bash
# From foodingo-microservices directory
mvn clean install -DskipTests

# Or build specific service
cd user-service
mvn clean package
```

### Run Individual Service Locally
```bash
# Start infrastructure first
docker-compose up -d mongodb redis rabbitmq eureka-server

# Run service
cd user-service
mvn spring-boot:run

# Or with Java
java -jar target/user-service-1.0.0.jar
```

---

## 📝 Configuration

### Service Ports Summary
```
8080  - API Gateway
8761  - Eureka Server
8888  - Config Server
8081  - User Service
8082  - Restaurant Service
8083  - Order Service
8084  - Payment Service
8085  - Review Service
8086  - Notification Service
8087  - Loyalty Service
8088  - Coupon Service
9090  - Prometheus
9411  - Zipkin
3000  - Grafana
5672  - RabbitMQ
15672 - RabbitMQ Management
6379  - Redis
27017 - MongoDB
```

### Environment Variables (.env)
```env
# MongoDB
MONGODB_URI=mongodb://admin:admin123@mongodb:27017/foodingo?authSource=admin

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=admin
RABBITMQ_PASSWORD=admin123

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# Razorpay
RAZORPAY_KEY_ID=your-razorpay-key
RAZORPAY_KEY_SECRET=your-razorpay-secret

# JWT
JWT_SECRET=your-super-secret-jwt-key-minimum-256-bits

# Email
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# AWS S3
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_S3_BUCKET_NAME=foodingo-images
AWS_REGION=us-east-1
```

---

## 🧪 API Testing

### Using cURL

#### 1. Register User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

#### 2. Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'

# Save the token from response
export TOKEN="eyJhbGc..."
```

#### 3. Get Restaurants
```bash
curl http://localhost:8080/api/restaurants \
  -H "Authorization: Bearer $TOKEN"
```

#### 4. Create Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": "rest123",
    "items": [
      {
        "foodId": "food123",
        "quantity": 2
      }
    ],
    "deliveryAddress": "123 Main St"
  }'
```

---

## 📊 Monitoring

### Zipkin (Distributed Tracing)
```
http://localhost:9411

Features:
- View request traces across services
- Identify performance bottlenecks
- Service dependency map
- Error tracking
```

### Prometheus (Metrics)
```
http://localhost:9090

Query Examples:
- http_server_requests_seconds_count
- jvm_memory_used_bytes
- system_cpu_usage
```

### Grafana (Visualization)
```
http://localhost:3000
Login: admin / admin123

Steps:
1. Add Prometheus as data source (http://prometheus:9090)
2. Import dashboard ID: 11378 (JVM Micrometer)
3. Create custom dashboards
```

### RabbitMQ Management
```
http://localhost:15672
Login: admin / admin123

Features:
- Queue monitoring
- Message rates
- Exchange overview
- Consumer tracking
```

---

## 🛠️ Troubleshooting

### Services Not Starting
```bash
# Check logs
docker-compose logs -f [service-name]

# Restart specific service
docker-compose restart user-service

# Rebuild and restart
docker-compose up -d --build user-service
```

### Service Not Registering with Eureka
```bash
# Check Eureka logs
docker-compose logs -f eureka-server

# Verify network connectivity
docker exec -it user-service ping eureka-server

# Check service application.yml
docker exec -it user-service cat /app/application.yml
```

### MongoDB Connection Issues
```bash
# Check MongoDB logs
docker-compose logs -f mongodb

# Connect to MongoDB
docker exec -it foodingo-mongodb mongosh -u admin -p admin123

# Verify database
use foodingo
show collections
```

### RabbitMQ Issues
```bash
# Check RabbitMQ logs
docker-compose logs -f rabbitmq

# Check queue status
curl -u admin:admin123 http://localhost:15672/api/queues
```

### High Memory Usage
```bash
# Check container stats
docker stats

# Reduce replicas
# Edit docker-compose.yml:
#   deploy:
#     replicas: 1  # Instead of 2 or 3
```

---

## 🔄 Scaling Services

### Scale Specific Service
```bash
# Scale order service to 5 instances
docker-compose up -d --scale order-service=5

# Scale down to 1
docker-compose up -d --scale order-service=1
```

### Auto-scaling (Kubernetes)
```bash
# See kubernetes/ directory
kubectl apply -f kubernetes/
kubectl autoscale deployment order-service --cpu-percent=70 --min=2 --max=10
```

---

## 🧹 Cleanup

### Stop All Services
```bash
docker-compose down
```

### Stop and Remove Volumes
```bash
docker-compose down -v
```

### Remove All Images
```bash
docker-compose down --rmi all -v
```

### Clean Build
```bash
# Maven clean
mvn clean

# Docker system prune
docker system prune -a --volumes
```

---

## 📚 Additional Resources

- **Architecture Document**: `MICROSERVICES_ARCHITECTURE.md`
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Service Health**: http://localhost:8080/actuator/health
- **Eureka Dashboard**: http://localhost:8761
- **Trace Visualization**: http://localhost:9411

---

## 🎯 Next Steps

1. ✅ **Test Core Functionality** - User registration, login, order creation
2. ✅ **Monitor Services** - Check Eureka, Zipkin, Prometheus
3. ✅ **Load Testing** - Use JMeter or Gatling
4. ✅ **Security Hardening** - Review API Gateway filters
5. ✅ **Production Deployment** - See `DEPLOYMENT_GUIDE.md`

---

## 💡 Tips

- **Always check Eureka** first to see if services are registered
- **Use Zipkin** to debug slow requests
- **Monitor RabbitMQ** for message processing
- **Check individual service logs** for detailed errors
- **Use circuit breakers** - Gateway automatically handles failures
- **Scale horizontally** - Add more replicas for high-load services

---

**Your microservices platform is ready! 🎉**

Need help? Check the logs:
```bash
docker-compose logs -f [service-name]
```

