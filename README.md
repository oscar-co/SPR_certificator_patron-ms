# SPR_certificator_patron-ms – API REST de Certificación Técnica

Este proyecto es una API RESTful construida con **Spring Boot**, diseñada para gestionar patrones de medida, certificados técnicos e incertidumbres asociadas a procesos de calibración. Forma parte de un sistema mayor de certificación y medición técnica.

Desarrollado como parte de mi formación práctica en Spring Boot, este proyecto refleja mi compromiso con las buenas prácticas backend, el diseño de APIs limpias y una arquitectura sólida orientada a producción.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.x
- Spring Web (REST)
- Spring Data JPA
- Spring Security (JWT y control de acceso con roles)
- Swagger / SpringDoc OpenAPI
- Spring Boot Actuator
- PostgreSQL (desarrollo, testing, CI)
- MySQL (configuración inicial)
- Docker y Docker Compose
- GitHub Actions (CI)
- JaCoCo (cobertura)
- Lombok
- SLF4J / Logback
- Postman

## Seguridad

El sistema utiliza autenticación basada en JWT. Los usuarios se almacenan en base de datos y el control de acceso está definido por roles (por ejemplo, ROLE_USER).

El endpoint de registro está abierto, mientras que el resto requiere autenticación.

## Entornos y perfiles

- `dev` (por defecto): usa PostgreSQL local en el puerto 5432 (`pg-certificator`).
- `test`: para tests locales y CI, usa `docker-compose.test.yml` en local y servicios de GitHub Actions al hacer PR y push.
- `prod` (planificado): desplegado en imagen Docker.

## Variables de entorno

Utiliza variables para desacoplar credenciales y configuraciones entre entornos. En `application-test.properties`:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/testdb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:testuser}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:testpass}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.profiles.active=test
```

## Pruebas automatizadas

### Tipos de pruebas

- Unitarias con Mockito y JUnit 5
- Integración con base de datos real (Testcontainers o Docker Compose)

### Ejecución local con entorno de test

Usa el script `run-tests.sh` para levantar PostgreSQL en Docker y ejecutar las pruebas:

```bash
./run-tests.sh                      # Ejecuta todos los tests
./run-tests.sh CertificateServiceTest  # Ejecuta una clase específica
```

### Ejecución en CI (GitHub Actions)

Cada push o PR sobre `master` ejecuta los tests y sube el informe JaCoCo. Ver `.github/workflows/ci.yml`.

## Endpoints disponibles

### Certificados (`/api/patrones`)

| Método  | Ruta             | Descripción                                           |
|---------|------------------|-------------------------------------------------------|
| POST    | `/`              | Crear un nuevo certificado                           |
| GET     | `/`              | Listar todos los certificados                        |
| GET     | `/{id}`          | Obtener certificado por ID                           |
| PUT     | `/{id}`          | Actualizar certificado existente                     |
| DELETE  | `/{id}`          | Eliminar certificado por ID                          |

### Cálculos (`/api/patrones`)

| Método  | Ruta                         | Descripción                                                  |
|---------|------------------------------|--------------------------------------------------------------|
| POST    | `/patrones-disponibles`     | Devuelve patrones adecuados por requisitos técnicos         |
| POST    | `/incertidumbre-patron`     | Calcula la incertidumbre de un patrón                       |

### Autenticación (`/api/auth`)

| Método  | Ruta         | Descripción                             |
|---------|--------------|-----------------------------------------|
| POST    | `/register`  | Registrar nuevo usuario (por defecto ROLE_USER) |
| POST    | `/login`     | Iniciar sesión y obtener JWT            |

## Estructura de respuesta

```json
{
  "status": "success",
  "message": "Certificado eliminado correctamente",
  "data": null,
  "timestamp": "2025-04-24T16:25:00Z"
}
```

En caso de error:

```json
{
  "status": "error",
  "message": "No se encontró un certificado con ese ID",
  "data": null,
  "timestamp": "2025-04-24T16:25:03Z"
}
```

## Cobertura de pruebas

El informe HTML se genera con:

```bash
mvn clean verify
```

Y puede consultarse en `target/site/jacoco/index.html`.

## Ejecución local

1. Levantar PostgreSQL (por ejemplo, usando Docker):

```bash
docker start pg-certificator
```

2. Ejecutar el servidor:

```bash
mvn spring-boot:run
```

3. Acceder a los servicios:

- API base: `http://localhost:8080/api/patrones`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html#/`
- Actuator: `http://localhost:8080/actuator`

## CI/CD

- El workflow de GitHub Actions valida cada PR con PostgreSQL (servicio GitHub).
- Se publica el artefacto Docker (`image.tar.gz`) tras éxito de los tests.
- Plan futuro: despliegue automático.