# SPR_certificator_patron-ms – API REST de Certificación Técnica

Este proyecto es una API RESTful construida con **Spring Boot**, diseñada para gestionar patrones de medida, certificados técnicos e incertidumbres asociadas a procesos de calibración. Forma parte de un sistema mayor de certificación y medición técnica.

> Desarrollado como parte de mi formación práctica en Java y Spring Boot, este proyecto refleja mi compromiso con las buenas prácticas backend, el diseño de APIs limpias y una arquitectura sólida orientada a producción.

---

## Tecnologías utilizadas

- Java 17  
- Spring Boot 3.x  
- Spring Web (REST)  
- Spring Data JPA  
- Spring Security (usuarios y roles con control de acceso)  
- Swagger / SpringDoc OpenAPI (documentación de endpoints)  
- JaCoCo (generación de informes de cobertura de pruebas)  
- Spring Boot Actuator (monitorización y métricas)  
- MySQL  
- Lombok  
- SLF4J / Logback para logging  
- Postman (para pruebas de endpoints)

---

## Seguridad

La API cuenta con autenticación y autorización mediante **Spring Security**, con gestión de **usuarios y roles** para proteger los endpoints sensibles. Esto garantiza que solo usuarios autenticados y autorizados puedan acceder a determinadas funcionalidades.

---

## Documentación de la API

Se ha implementado **Swagger/OpenAPI** para explorar y probar los endpoints directamente desde el navegador. Accede a la documentación en:

```
http://localhost:8080/swagger-ui/index.html#/
```

---

## Cobertura de Pruebas

El proyecto utiliza **JaCoCo** para medir la cobertura de las pruebas automatizadas. El informe se genera en formato HTML tras ejecutar:

```
./mvnw test
```

El reporte se encuentra en `target/site/jacoco/index.html`.

---

## Endpoints disponibles

### Certificados (`/api/patrones`)

| Método  | Ruta       | Descripción                                           |
|---------|------------|-------------------------------------------------------|
| `POST`  | `/`        | Crear un nuevo certificado (patrón).                 |
| `GET`   | `/`        | Obtener todos los certificados registrados.          |
| `GET`   | `/{id}`    | Obtener los datos de un certificado por su ID.       |
| `PUT`   | `/{id}`    | Actualizar un certificado existente por ID.          |
| `DELETE`| `/{id}`    | Eliminar un certificado por ID (manejo de errores incluido). |

### Cálculo de patrones (`/api/patrones`)

| Método  | Ruta                         | Descripción                                                  |
|---------|------------------------------|--------------------------------------------------------------|
| `POST`  | `/patrones-disponibles`     | Retorna los patrones adecuados según los parámetros técnicos. |
| `POST`  | `/incertidumbre-patron`     | Calcula la incertidumbre asociada a un patrón de medida.     |

> Todas las respuestas siguen una estructura JSON estándar con `status`, `message`, `data` y `timestamp`.

---

## Ejemplo de respuesta

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

---

## Cómo ejecutar el proyecto

1. Clona el repositorio:

```bash
git clone https://github.com/oscar-co/SPR_certificator_patron-ms.git
cd SPR_certificator_patron-ms
```

2. Configura tu base de datos en `src/main/resources/application.properties`.

3. Ejecuta el servidor local:

```bash
./mvnw spring-boot:run
```

4. Accede a los servicios desde:

- API base: `http://localhost:8080/api/patrones`  
- Swagger UI: `http://localhost:8080/swagger-ui/index.html#/`  
- Actuator: `http://localhost:8080/actuator`
