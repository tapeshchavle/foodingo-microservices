# 🍔 FOODINGO MICROSERVICES

> Production-Grade Food Ordering Platform - Microservices Architecture

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

---

## 📊 PROJECT STATUS

| Component | Status | Port | Files |
|-----------|--------|------|-------|
| **Infrastructure** |
| Eureka Server | ✅ Complete | 8761 | 6 |
| Config Server | ✅ Complete | 8888 | 6 |
| API Gateway | ✅ Complete | 8080 | 8 |
| **Business Services** |
| User Service | ✅ Complete | 8081 | 16 |
| Restaurant Service | ✅ Complete | 8082 | 18 |
| Order Service | ✅ Complete | 8083 | 15 |
| Payment Service | ⏳ Pending | 8084 | - |
| Review Service | ⏳ Pending | 8085 | - |
| Notification Service | ⏳ Pending | 8086 | - |
| Loyalty Service | ⏳ Pending | 8087 | - |
| Coupon Service | ⏳ Pending | 8088 | - |
| **Supporting Services** |
| MongoDB | ✅ Ready | 27017 | - |
| Redis | ✅ Ready | 6379 | - |
| RabbitMQ | ✅ Ready | 5672, 15672 | - |
| Zipkin | ✅ Ready | 9411 | - |
| Prometheus | ✅ Ready | 9090 | - |
| Grafana | ✅ Ready | 3000 | - |

**Overall Progress: 60% Complete (73 files created)**

---

## 🏗️ ARCHITECTURE

```
                        ┌─────────────────────┐
                        │   External Users    │
                        └──────────┬──────────┘
                                   │
                        ┌──────────▼──────────┐
                        │    API Gateway      │
                        │    Port: 8080       │
                        │  • Rate Limiting    │
                        │  • Circuit Breakers │
                        │  • Load Balancing   │
                        └──────────┬──────────┘
                                   │
          ┌────────────────────────┼────────────────────────┐
          │                        │                        │
    ┌─────▼──────┐         ┌──────▼─────┐         ┌───────▼──────┐
    │   User     │         │ Restaurant │         │    Order     │
    │  Service   │◄────────┤  Service   │◄────────┤   Service    │
    │ Port: 8081 │         │ Port: 8082 │         │  Port: 8083  │
    └─────┬──────┘         └──────┬─────┘         └───────┬──────┘
          │                       │                        │
          │     ┌─────────────────┼────────────────────────┤
          │     │                 │                        │
    ┌─────▼─────▼──┐      ┌──────▼────────┐      ┌────────▼──────┐
    │   Eureka     │      │    MongoDB    │      │   RabbitMQ    │
    │   Server     │      │  Port: 27017  │      │  Port: 5672   │
    │  Port: 8761  │      └───────────────┘      └───────────────┘
    └──────────────┘
           │
    ┌──────▼──────────┐
    │     Zipkin      │
    │  Port: 9411     │
    │ (Tracing)       │
    └─────────────────┘
```

---

## ✨ KEY FEATURES

### ✅ **Already Implemented**

- 🔐 **JWT Authentication & Authorization** - Secure user login with role-based access
- 🏢 **Restaurant Management** - Full CRUD operations for restaurants and food items
- 📷 **Image Upload** - AWS S3 integration for food images
- 🛒 **Order Management** - Complete order lifecycle with status tracking
- 💳 **Payment Integration** - Razorpay payment gateway
- 🔍 **Service Discovery** - Automatic service registration with Eureka
- 🚪 **API Gateway** - Single entry point with routing and security
- ⚡ **Caching** - Redis for performance optimization
- 📊 **Monitoring** - Prometheus & Grafana dashboards
- 🔎 **Distributed Tracing** - Zipkin for request tracking
- 💬 **Message Queue** - RabbitMQ for async communication
- 🔄 **Circuit Breakers** - Resilience4j for fault tolerance
- 🚦 **Rate Limiting** - Prevent API abuse
- 📈 **Health Checks** - Actuator endpoints
- 🐳 **Docker Support** - Containerization ready

### ⏳ **Pending Implementation**

- ⭐ **Reviews & Ratings** - Review Service (Port 8085)
- 🔔 **Real-time Notifications** - Notification Service (Port 8086)
- 🎁 **Loyalty Program** - Loyalty Service (Port 8087)
- 🎟️ **Coupons & Discounts** - Coupon Service (Port 8088)
- 💰 **Payment Processing** - Payment Service (Port 8084)

---

## 🚀 QUICK START

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- MongoDB
- Redis

### 1. Clone & Setup

```bash
cd c:\Tapesh\foodingo\foodingo-microservices
```

### 2. Start Infrastructure

```bash
docker-compose up -d
```

This starts:
- MongoDB (27017)
- Redis (6379)
- RabbitMQ (5672, 15672)
- Zipkin (9411)
- Prometheus (9090)
- Grafana (3000)

### 3. Build All Services

```bash
mvn clean install -DskipTests
```

### 4. Start Services

```bash
# Terminal 1: Eureka Server
cd eureka-server
mvn spring-boot:run

# Wait 30 seconds for Eureka to start

# Terminal 2: Config Server
cd config-server
mvn spring-boot:run

# Terminal 3: API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 4: User Service
cd user-service
mvn spring-boot:run

# Terminal 5: Restaurant Service
cd restaurant-service
mvn spring-boot:run

# Terminal 6: Order Service
cd order-service
mvn spring-boot:run
```

### 5. Verify

Visit:
- **Eureka Dashboard**: http://localhost:8761
- **Zipkin**: http://localhost:9411
- **RabbitMQ Management**: http://localhost:15672 (admin/admin123)
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)

---

## 🎯 API ENDPOINTS

### User Service (Port 8081)

```bash
# Register
POST   /api/user/register
{
  "email": "user@example.com",
  "password": "Pass@123",
  "name": "John Doe",
  "phoneNumber": "1234567890"
}

# Login
POST   /api/user/login
{
  "email": "user@example.com",
  "password": "Pass@123"
}

# Get Profile (requires JWT)
GET    /api/user/profile
Header: Authorization: Bearer <token>

# Update Profile
PUT    /api/user/profile

# Delete Profile
DELETE /api/user/profile
```

### Restaurant Service (Port 8082)

```bash
# Create Restaurant
POST   /api/restaurants
Header: Authorization: Bearer <token>

# Get All Restaurants
GET    /api/restaurants

# Get Restaurant by ID
GET    /api/restaurants/{id}

# Search Restaurants
GET    /api/restaurants/search?name=Pizza

# Get by City
GET    /api/restaurants/city/Mumbai

# Get by Cuisine
GET    /api/restaurants/cuisine/Italian

# Add Food Item
POST   /api/foods/add
Content-Type: multipart/form-data

# Get All Foods
GET    /api/foods

# Get Food by Restaurant
GET    /api/foods/restaurant/{restaurantId}
```

### Order Service (Port 8083)

```bash
# Create Order
POST   /api/orders
Header: Authorization: Bearer <token>
{
  "restaurantId": "...",
  "orderedItems": [...],
  "userAddress": "...",
  "amount": 500.00,
  "email": "user@example.com",
  "phoneNumber": "1234567890"
}

# Verify Payment
POST   /api/orders/verify-payment

# Get User Orders
GET    /api/orders/user

# Get Order by ID
GET    /api/orders/{id}

# Update Order Status
PATCH  /api/orders/{id}/status?status=CONFIRMED

# Cancel Order
PATCH  /api/orders/{id}/cancel?reason=...
```

---

## 📚 DOCUMENTATION

- **[COMPLETE_IMPLEMENTATION_SUMMARY.md](./COMPLETE_IMPLEMENTATION_SUMMARY.md)** - Full feature list and status
- **[COMPLETE_REMAINING_SERVICES_GUIDE.md](./COMPLETE_REMAINING_SERVICES_GUIDE.md)** - How to create pending services
- **[SERVICES_CREATION_STATUS.md](./SERVICES_CREATION_STATUS.md)** - Detailed progress tracking
- **[MICROSERVICES_ARCHITECTURE.md](./MICROSERVICES_ARCHITECTURE.md)** - Architecture deep dive
- **[MICROSERVICES_QUICKSTART.md](./MICROSERVICES_QUICKSTART.md)** - Quick start guide

---

## 🧪 TESTING

### Health Checks

```bash
# Check all services
curl http://localhost:8761  # Eureka
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8082/actuator/health  # Restaurant Service
curl http://localhost:8083/actuator/health  # Order Service
```

### Integration Tests

```bash
# Run tests for specific service
cd user-service
mvn test

# Run all tests
cd ..
mvn test
```

---

## 🐳 DOCKER DEPLOYMENT

### Build Docker Images

```bash
# Build all services
docker-compose build

# Build specific service
docker build -t foodingo/user-service -f user-service/Dockerfile .
```

### Run with Docker Compose

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

---

## 📊 MONITORING & OBSERVABILITY

### Prometheus Metrics

Access: http://localhost:9090

Key metrics:
- `http_server_requests_seconds` - API response times
- `jvm_memory_used_bytes` - Memory usage
- `process_cpu_usage` - CPU usage

### Grafana Dashboards

Access: http://localhost:3000 (admin/admin)

Pre-configured dashboards:
- JVM (Micrometer)
- Spring Boot Statistics
- API Performance

### Zipkin Tracing

Access: http://localhost:9411

View distributed traces across services.

### RabbitMQ Management

Access: http://localhost:15672 (admin/admin123)

Monitor message queues and exchanges.

---

## 🔧 CONFIGURATION

### Environment Variables

```bash
# MongoDB
MONGODB_URI=mongodb://localhost:27017/foodingo

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672

# Eureka
EUREKA_SERVER=http://localhost:8761/eureka/

# AWS S3 (for Restaurant Service)
AWS_ACCESS_KEY=your_access_key
AWS_SECRET_KEY=your_secret_key
AWS_S3_BUCKET=your_bucket_name

# Razorpay (for Order Service)
RAZORPAY_KEY=your_razorpay_key
RAZORPAY_SECRET=your_razorpay_secret
```

---

## 🤝 CONTRIBUTING

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests
5. Submit a pull request

---

## 📝 LICENSE

This project is licensed under the MIT License.

---

## 👨‍💻 AUTHOR

**Tapesh**

---

## 📞 SUPPORT

For issues and questions:
- Open an issue on GitHub
- Email: support@foodingo.com

---

## 🎉 ACKNOWLEDGMENTS

- Spring Boot Team
- Netflix OSS
- MongoDB
- Redis
- RabbitMQ
- AWS

---

## 🗺️ ROADMAP

### Phase 1: Core Services (✅ 60% Complete)
- ✅ Infrastructure setup
- ✅ User authentication
- ✅ Restaurant management
- ✅ Order processing

### Phase 2: Enhanced Features (⏳ In Progress)
- ⏳ Review & rating system
- ⏳ Notification service
- ⏳ Loyalty program
- ⏳ Coupon system
- ⏳ Payment service

### Phase 3: Advanced Features (📋 Planned)
- 📋 Real-time order tracking
- 📋 Delivery partner management
- 📋 Analytics dashboard
- 📋 ML-based recommendations
- 📋 Multi-language support
- 📋 Mobile app APIs

### Phase 4: Scale & Optimize (📋 Planned)
- 📋 Kubernetes deployment
- 📋 ELK stack logging
- 📋 Advanced caching strategies
- 📋 Load testing
- 📋 Performance optimization

---

**Built with ❤️ using Spring Boot & Microservices Architecture**