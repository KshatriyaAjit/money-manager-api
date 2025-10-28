# Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the application (skip tests to speed up)
RUN ./mvnw clean package -DskipTests

# Expose the app port (Render uses 10000 internally)
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/money-manager-api-0.0.1-SNAPSHOT.jar"]
