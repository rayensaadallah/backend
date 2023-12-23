FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/backend.jar . 
EXPOSE 8089
CMD ["java", "-jar", "backend.jar"]
