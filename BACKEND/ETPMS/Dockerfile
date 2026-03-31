# Build stage
FROM maven:3.9.9-eclipse-temurin-21 as build
WORKDIR /app

COPY BACKEND/ETPMS/pom.xml .
COPY BACKEND/ETPMS/mvnw .
COPY BACKEND/ETPMS/.mvn ./.mvn
RUN chmod +x mvnw

COPY BACKEND/ETPMS/src ./src
RUN ./mvnw -B package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/ETPMS-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
