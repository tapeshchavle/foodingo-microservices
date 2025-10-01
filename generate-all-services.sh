#!/bin/bash

# Foodingo Microservices Generator
# This script generates all 7 remaining business microservices

echo "🚀 Foodingo Microservices Generator"
echo "===================================="
echo ""

# Color codes
GREEN='\033[0.32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Service definitions
declare -A SERVICES
SERVICES["restaurant"]=8082
SERVICES["order"]=8083
SERVICES["payment"]=8084
SERVICES["review"]=8085
SERVICES["notification"]=8086
SERVICES["loyalty"]=8087
SERVICES["coupon"]=8088

create_service_structure() {
    local service=$1
    local port=$2
    
    echo -e "${BLUE}Creating $service-service...${NC}"
    
    # Create directory structure
    mkdir -p "$service-service/src/main/java/com/foodingo/$service"/{entity,repository,service/impl,controller,dto,config,client,messaging}
    mkdir -p "$service-service/src/main/resources"
    mkdir -p "$service-service/src/test/java/com/foodingo/$service"
    
    # Create pom.xml
    cat > "$service-service/pom.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.foodingo</groupId>
        <artifactId>foodingo-microservices</artifactId>
        <version>1.0.0</version>
    </parent>
    
    <artifactId>$service-service</artifactId>
    <name>$(echo $service | sed 's/.*/\u&/') Service</name>
    
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
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

    # Create Application class
    local class_name="$(echo $service | sed 's/.*/\u&/')ServiceApplication"
    cat > "$service-service/src/main/java/com/foodingo/$service/${class_name}.java" << EOF
package com.foodingo.$service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableMongoAuditing
public class $class_name {
    public static void main(String[] args) {
        SpringApplication.run(${class_name}.class, args);
    }
}
EOF

    # Create application.yml
    cat > "$service-service/src/main/resources/application.yml" << EOF
server:
  port: $port

spring:
  application:
    name: $service-service
  
  data:
    mongodb:
      uri: \${MONGODB_URI:mongodb://admin:admin123@localhost:27017/foodingo?authSource=admin}
      auto-index-creation: true
  
  rabbitmq:
    host: \${RABBITMQ_HOST:localhost}
    port: \${RABBITMQ_PORT:5672}
    username: \${RABBITMQ_USERNAME:admin}
    password: \${RABBITMQ_PASSWORD:admin123}

eureka:
  client:
    service-url:
      defaultZone: \${EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE:http://localhost:8761/eureka/}
  instance:
    prefer-ip-address: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  tracing:
    sampling:
      probability: 1.0
  zipkin:
    tracing:
      endpoint: \${MANAGEMENT_ZIPKIN_TRACING_ENDPOINT:http://localhost:9411/api/v2/spans}

logging:
  level:
    root: INFO
    com.foodingo.$service: DEBUG
EOF

    # Create Dockerfile
    cat > "$service-service/Dockerfile" << EOF
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY ../pom.xml ../pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S foodingo && adduser -S foodingo -G foodingo
COPY --from=build /app/target/$service-service-1.0.0.jar app.jar
RUN mkdir -p logs && chown -R foodingo:foodingo /app
USER foodingo
EXPOSE $port
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:$port/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

    # Create README for the service
    cat > "$service-service/README.md" << EOF
# $(echo $service | sed 's/.*/\u&/') Service

Port: $port

## Copy Implementation

Copy the following files from the monolith (\`foodingo/src/main/java/com/food/\`):

### Entities
- Copy relevant entity files to \`src/main/java/com/foodingo/$service/entity/\`

### Repositories  
- Copy relevant repository files to \`src/main/java/com/foodingo/$service/repository/\`

### Services
- Copy relevant service files to \`src/main/java/com/foodingo/$service/service/\`
- Copy implementation files to \`src/main/java/com/foodingo/$service/service/impl/\`

### Controllers
- Copy relevant controller files to \`src/main/java/com/foodingo/$service/controller/\`

### DTOs
- Copy relevant request/response DTOs to \`src/main/java/com/foodingo/$service/dto/\`

### Configuration
- Copy relevant config files to \`src/main/java/com/foodingo/$service/config/\`

## Next Steps

1. Copy implementation from monolith
2. Update package names from \`com.food\` to \`com.foodingo.$service\`
3. Add Feign clients if needed
4. Add event publishers/listeners if needed
5. Build: \`mvn clean package\`
6. Run: \`mvn spring-boot:run\`
EOF

    echo -e "${GREEN}✅ Created $service-service structure${NC}"
}

# Main execution
echo "Creating all microservices..."
echo ""

for service in "${!SERVICES[@]}"; do
    port="${SERVICES[$service]}"
    create_service_structure "$service" "$port"
done

echo ""
echo -e "${GREEN}✅ All service structures created!${NC}"
echo ""
echo "📋 Next steps:"
echo "1. For each service, copy code from monolith:"
echo "   cd foodingo-microservices/[service-name]-service"
echo "   # Copy relevant files from ../foodingo/"
echo ""
echo "2. Update package names:"
echo "   # Change com.food.* to com.foodingo.[service].*"
echo ""
echo "3. Build services:"
echo "   cd foodingo-microservices"
echo "   mvn clean install"
echo ""
echo "4. Run with Docker Compose:"
echo "   docker-compose up -d"
echo ""
echo "🎉 Happy coding!"

