# Etapa 1: Construir el JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar el código
COPY . .

# Compilar el proyecto (sin ejecutar tests)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
FROM openjdk:21-jdk

WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
