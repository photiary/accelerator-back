# Multi-stage build for Spring Boot application

# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy gradle files first for better layer caching
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
