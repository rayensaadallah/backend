FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar
EXPOSE 8089
CMD ["java", "-jar", "backend.jar"]
