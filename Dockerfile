# Estágio de Build
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /workspace
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src. /src
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]