# Usa una imagen oficial de Java
FROM openjdk:21-jdk

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR al contenedor
COPY target/app-0.0.1-SNAPSHOT.jar app.jar

# Comando para correr tu app
CMD ["java", "-jar", "app.jar"]
