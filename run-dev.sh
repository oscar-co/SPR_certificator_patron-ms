#!/bin/bash

echo "Levantando base de datos para desarrollo..."
docker compose -f docker-compose.dev.yml up -d

echo "Esperando a que PostgreSQL esté en estado 'healthy'..."

# Esperar hasta que el contenedor esté healthy
until [ "$(docker inspect --format='{{.State.Health.Status}}' pg-certificator 2>/dev/null)" = "healthy" ]; do
  echo -n "."
  sleep 1
done

echo ""
echo "PostgreSQL listo. Lanzando backend Spring Boot con perfil 'dev'..."
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
