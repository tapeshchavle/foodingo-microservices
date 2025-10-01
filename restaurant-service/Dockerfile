# Multi-stage build for Restaurant Service
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml ../pom.xml
COPY restaurant-service/pom.xml ./pom.xml

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY restaurant-service/src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", \
  "-Xms256m", \
  "-Xmx512m", \
  "-XX:+UseG1GC", \
  "-XX:MaxGCPauseMillis=100", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]

