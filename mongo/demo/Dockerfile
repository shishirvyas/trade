# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven build output (JAR file) into the container
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose the application port
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "demo.jar"]
