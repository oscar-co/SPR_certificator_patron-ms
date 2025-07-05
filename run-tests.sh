# - Si se ejecuta ./run-tests.sh                                          -> se ejecutan todos los tests
# - Si se le pasa nombre de clase por parametro, ejecuta
#        solo esa clase, ej. ./run-tests.sh CertificateRepositoryTest     -> se ejecuta solo esa clase


#!/bin/bash

set -e  # Salir si hay errores
COMPOSE_FILE="docker-compose.test.yml"
SERVICE_NAME="postgres-test"

echo "Levantando contenedor de PostgreSQL de test..."
docker-compose -f "$COMPOSE_FILE" up -d

echo "Esperando a que PostgreSQL esté listo..."
until docker exec "$SERVICE_NAME" pg_isready -U testuser > /dev/null 2>&1; do
  sleep 1
done

echo "PostgreSQL está listo."

if [ -n "$1" ]; then
  TEST_CLASS="$1"
  echo "Ejecutando solo la clase de test: $TEST_CLASS"
  SPRING_PROFILES_ACTIVE=test mvn -Dtest="$TEST_CLASS" test
else
  echo "Ejecutando todos los tests..."
  SPRING_PROFILES_ACTIVE=test mvn clean verify
fi

echo "Eliminando contenedor de test..."
docker-compose -f "$COMPOSE_FILE" down

echo "Todo completado correctamente."
