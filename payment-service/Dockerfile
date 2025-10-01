FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ../pom.xml
COPY payment-service/pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY payment-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8084
HEALTHCHECK --interval=30s --timeout=3s CMD wget --spider http://localhost:8084/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

