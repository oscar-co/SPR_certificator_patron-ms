# 1. Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copia los ficheros de Maven y descarga dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente y construye el JAR empaquetado
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Puerto en el que corre Spring Boot
EXPOSE 8080

# Punto de entrada
ENTRYPOINT ["java","-jar","/app/app.jar"]
