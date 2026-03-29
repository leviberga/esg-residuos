# Estágio 1: Build com Maven
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /workspace

# Otimização de Cache: Copia apenas o pom.xml primeiro
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte e gera o artefato
COPY src ./src
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# Estágio 2: Runtime com JRE leve
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia apenas o JAR gerado no estágio anterior
COPY --from=builder /workspace/target/*.jar app.jar

# Variáveis de ambiente para ajuste da JVM
ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080

# Execução da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]