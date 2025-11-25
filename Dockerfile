# ===== Stage 1: Build with Gradle Wrapper =====
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Copy only Gradle wrapper first (for caching)
COPY gradlew .
COPY gradle ./gradle

# Ensure gradlew is executable
RUN chmod +x gradlew

# Copy entire project
COPY . .

# Build Spring Boot jar (skip tests)
RUN ./gradlew clean bootJar -x test --no-daemon

# ===== Stage 2: Runtime Image =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy jar
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
