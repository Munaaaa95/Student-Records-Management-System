# Step 1: Build the application using an active Maven image
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using the official Eclipse Temurin Java runtime
FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]