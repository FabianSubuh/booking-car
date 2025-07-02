# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

# Crucial: Copy the entire multi-module project from the host's root context
COPY pom.xml ./
COPY api-gateway ./api-gateway
COPY car-catalog-service ./car-catalog-service
COPY testdrive-booking-service ./testdrive-booking-service

# --- ADD THESE LINES FOR DEBUGGING ---
RUN echo "--- Listing contents of /app ---"
RUN ls -R /app
RUN echo "--- Attempting Maven build for car-catalog-service ---"
# --- END DEBUGGING LINES ---

# Change to the specific module's directory inside the container python3 -m http.server 3000
WORKDIR /app

# Build only this module, but with the full reactor available
RUN mvn clean install -pl car-catalog-service -am -DskipTests

# Stage 2: Create the runtime image (remains the same)
FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=car-catalog-service/target/car-catalog-service-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]