
# Use the official OpenJDK 11 base image for the runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage to the runtime image
COPY --from=builder /app/target/backend.jar .

# Expose the port that the application will run on
EXPOSE 8089

# Specify the command to run on container startup
CMD ["java", "-jar", "backend.jar"]
