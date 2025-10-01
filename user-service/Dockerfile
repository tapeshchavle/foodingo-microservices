# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom first
COPY ../pom.xml ../pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add non-root user
RUN addgroup -S foodingo && adduser -S foodingo -G foodingo

# Copy jar
COPY --from=build /app/target/user-service-1.0.0.jar app.jar

# Create logs directory
RUN mkdir -p logs && chown -R foodingo:foodingo /app

# Switch to non-root user
USER foodingo

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]

