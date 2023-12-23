FROM openjdk:11
WORKDIR /app
COPY target/backend.jar . 
EXPOSE 8089
CMD ["java", "-jar", "backend.jar"]
