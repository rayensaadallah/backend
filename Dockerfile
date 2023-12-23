# Use the official Maven image as the build stage
FROM maven:3.6.3-openjdk-11 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only the pom.xml file to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the entire project into the container
COPY . .

# Run Maven clean and install to build the project
RUN mvn clean install

# Use the official OpenJDK 11 base image for the runtime stage
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage to the runtime image
COPY --from=builder /app/target/backend.jar .

# Expose the port that the application will run on
EXPOSE 8089

# Specify the command to run on container startup
CMD ["java", "-jar", "backend.jar"]
