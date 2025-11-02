# Multi-stage Dockerfile to build and run the Spring Boot application
# Stage 1: build with Maven
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /workspace
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
# Copy source
COPY src ./src
# Build the application (package)
RUN mvn -B -DskipTests package

# Stage 2: run with a lightweight JDK
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the packaged jar from the build stage
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
