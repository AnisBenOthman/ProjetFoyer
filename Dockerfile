# Use a multi-stage build for optimized image size
# Stage 1: Build the application
FROM maven:3.8-openjdk-17 AS builder

# Set working directory
WORKDIR /app

# Copy maven files first for better caching
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-slim

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Add metadata
LABEL maintainer="anis.benothman@esprit.tn"
LABEL version="1.0"
LABEL description="Spring Boot Application"

# Set Java options (customize as needed)
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Expose the port the app runs on
EXPOSE 8089


# Run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]