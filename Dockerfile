# Builder stage
FROM maven:3.6.3-openjdk-17 as builder
WORKDIR /app
COPY . /app
RUN mvn clean install

# Runtime stage
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar .
EXPOSE 8089
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
