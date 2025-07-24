# Stage 1: Build the application using Gradle Wrapper
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Gradle Wrapper and build scripts
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle.kts build.gradle.kts ./

# Give execution permission to the Gradle wrapper
RUN chmod +x gradlew

# Copy the rest of the project
COPY . .

# Build the app (skip tests for faster build)
RUN ./gradlew build -x test --no-daemon

# Stage 2: Create the lightweight runtime image
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

COPY .env .env
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
