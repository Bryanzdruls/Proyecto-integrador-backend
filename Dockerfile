# Etapa 1: Construir el JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar el código
COPY . .

# Compilar (sin correr tests)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la app
FROM openjdk:21-jdk

WORKDIR /app

# Copiar el jar compilado de la etapa anterior
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar.original app.jar

# Comando para correr
CMD ["java", "-jar", "app.jar"]
