# Etapa 1: Build do JAR
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Construção da Imagem Final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/mini-autorizador.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
