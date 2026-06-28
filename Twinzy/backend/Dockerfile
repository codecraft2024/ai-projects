FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY backend/pom.xml ./pom.xml
RUN mvn -q dependency:go-offline

COPY backend/src ./src
RUN mvn -q package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /workspace/target/twinzy-backend-0.1.0.jar /app/app.jar
COPY database /app/database

ENV DATABASE_URL=jdbc:postgresql://postgres:5432/twinzy
ENV POSTGRES_USER=twinzy
ENV POSTGRES_PASSWORD=twinzy
ENV TWINZY_DB_MIGRATIONS=/app/database/migrations
ENV TWINZY_SEED_PATH=/app/database/seed

EXPOSE 8081

ENTRYPOINT ["java", "-Xmx2g", "-jar", "/app/app.jar"]
