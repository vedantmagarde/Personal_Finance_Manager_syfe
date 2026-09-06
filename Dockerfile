# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY personal-finance-manager-springboot-server/pom.xml .
# Download dependencies first for better caching
RUN mvn dependency:go-offline -B
# Copy the rest of the source code
COPY personal-finance-manager-springboot-server/src ./src
# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/personal-finance-manager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
