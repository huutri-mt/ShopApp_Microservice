FROM maven:3.9.9-amazoncorretto-21 AS build
# Copy source code
COPY pom.xml /app/
COPY src /app/src
WORKDIR /app
# Build the application
RUN mvn clean package -DskipTests
# Stage 2: create image
FROM amazoncorretto:21.0.7
# Set working folder
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
