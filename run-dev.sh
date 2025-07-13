#!/bin/bash

echo "Levantando base de datos para desarrollo..."

# Intentar levantar PostgreSQL
if ! docker compose -f docker-compose.dev.yml up -d; then
  echo "Error al levantar los contenedores. Limpiando redes huérfanas..."
  docker compose -f docker-compose.dev.yml down --volumes --remove-orphans
  docker network prune -f

  echo "Reintentando levantar contenedores..."
  docker compose -f docker-compose.dev.yml up -d --build
fi

echo "Esperando a que PostgreSQL esté en estado 'healthy'..."

# Esperar hasta que el contenedor esté healthy
until [ "$(docker inspect --format='{{.State.Health.Status}}' pg-certificator 2>/dev/null)" = "healthy" ]; do
  echo -n "."
  sleep 1
done

echo ""
echo "PostgreSQL listo. Lanzando backend Spring Boot con perfil 'dev'..."
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
