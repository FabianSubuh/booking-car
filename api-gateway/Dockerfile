# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./
COPY api-gateway ./api-gateway
COPY car-catalog-service ./car-catalog-service
COPY testdrive-booking-service ./testdrive-booking-service

WORKDIR /app

RUN mvn clean install -pl api-gateway -am -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre

WORKDIR /app

ARG JAR_FILE=api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]