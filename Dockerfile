# Base image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/TBoI-API-1.0.jar /app/tboi.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "tboi.jar"]
