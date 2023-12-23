# Build stage
FROM maven:4.0.0-openjdk-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean install

# Run stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/backend.jar /app
EXPOSE 8089
CMD ["java", "-jar", "backend.jar"]
