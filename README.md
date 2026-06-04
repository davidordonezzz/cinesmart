# CineSmart

Proyecto intermodular de 2 DAW. Aplicacion web de cine desarrollada con Java 21, Spring Boot 4, MySQL 8, Thymeleaf y Bootstrap.

Esta es la version final simplificada del proyecto: no incluye envio de emails, generacion o descarga de PDF, ni administracion de compras. El flujo principal se centra en consultar cartelera, ver sesiones, seleccionar butaca y guardar una compra simulada.

## Funcionalidades

### Usuario invitado

- Ver cartelera.
- Filtrar peliculas por titulo, genero y edad recomendada.
- Ver el detalle de una pelicula.
- Consultar sesiones disponibles.
- Registrarse e iniciar sesion.

### Usuario registrado

- Seleccionar una butaca en el mapa visual de la sala.
- Confirmar una compra simulada.
- Ver la confirmacion de compra en pantalla.
- Consultar sus compras en Mis Entradas.

### Administrador

- Ver el panel de administracion.
- CRUD de peliculas.
- CRUD de salas.
- CRUD de sesiones.

## Stack tecnologico

| Capa | Tecnologia |
|------|------------|
| Lenguaje | Java 21 |
| Backend | Spring Boot 4 |
| Seguridad | Spring Security |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8 |
| Vistas | Thymeleaf |
| Frontend | Bootstrap 5 |
| Validacion | Jakarta Validation |
| Build | Maven |
| Contenedores | Docker y Docker Compose |

## Estructura del proyecto

```text
cinesmart_mejorado/
|-- docs/
|   `-- diagramas/
|       |-- arquitectura.puml
|       |-- diagrama-clases.puml
|       |-- diagrama-er.puml
|       |-- secuencia-compra.puml
|       `-- casos-uso.puml
|
|-- src/main/java/com/cine/cinesmart/
|   |-- CinesmartApplication.java
|   |-- config/
|   |   `-- SecurityConfig.java
|   |-- controller/
|   |   |-- AdminController.java
|   |   |-- AdminPeliculaController.java
|   |   |-- AdminSalaController.java
|   |   |-- AdminSesionController.java
|   |   |-- AuthController.java
|   |   |-- CarteleraController.java
|   |   `-- CompraController.java
|   |-- dto/
|   |   `-- RegistroDTO.java
|   |-- init/
|   |   `-- DataInitializer.java
|   |-- model/
|   |-- repository/
|   `-- service/
|
|-- src/main/resources/
|   |-- application.properties
|   |-- static/css/style.css
|   `-- templates/
|       |-- cartelera.html
|       |-- compra-confirmacion.html
|       |-- login.html
|       |-- mis-compras.html
|       |-- pelicula-detalle.html
|       |-- registro.html
|       |-- seleccion-butacas.html
|       |-- admin/
|       `-- fragments/
|
|-- src/test/java/com/cine/cinesmart/service/
|   |-- CompraServiceTest.java
|   `-- UsuarioServiceTest.java
|
|-- Dockerfile
|-- docker-compose.yml
|-- pom.xml
`-- README.md
```

## Como arrancar

### Con Docker

```bash
docker-compose up --build
```

Despues abre:

```text
http://localhost:8080
```

Para parar los contenedores:

```bash
docker-compose down
```

### En local

Requisitos:

- Java 21.
- MySQL 8 en el puerto 3306.
- Maven o el wrapper incluido (`mvnw` / `mvnw.cmd`).

Base de datos:

```sql
CREATE DATABASE cinesmart;
```

Arranque:

```bash
./mvnw spring-boot:run
```

En Windows:

```bat
mvnw.cmd spring-boot:run
```

## Credenciales de prueba

`DataInitializer` carga datos iniciales cuando la base de datos esta vacia.

| Rol | Email | Contrasena |
|-----|-------|------------|
| ADMIN | `admin@cinesmart.com` | `admin123` |
| USER | `david@correo.com` | `user123` |

Tambien se cargan peliculas, salas, asientos y sesiones de prueba.

## Flujo principal de compra

1. El usuario entra en la cartelera.
2. Filtra o elige una pelicula.
3. Abre el detalle de la pelicula.
4. Selecciona una sesion.
5. Elige una butaca disponible en el mapa.
6. Confirma la compra simulada.
7. La aplicacion guarda la compra y muestra la confirmacion.

La reserva se protege en dos niveles:

- `CompraService` comprueba si la butaca esta ocupada antes de guardar.
- La entidad `Compra` define una restriccion unica para impedir dos compras con la misma sesion y el mismo asiento.

## Endpoints principales

### Publicos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/` | Redirige a cartelera |
| GET | `/cartelera` | Cartelera con filtros |
| GET | `/pelicula/{id}` | Detalle de pelicula y sesiones |
| GET | `/login` | Formulario de login |
| GET | `/registro` | Formulario de registro |
| POST | `/registro` | Alta de usuario |

### Usuario autenticado

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/sesion/{id}/butacas` | Mapa de butacas |
| POST | `/comprar` | Confirmar compra simulada |
| GET | `/compra/confirmacion/{id}` | Confirmacion de compra |
| GET | `/mis-compras` | Compras del usuario |

### Administrador

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/admin` | Panel de administracion |
| GET/POST | `/admin/peliculas/**` | CRUD de peliculas |
| GET/POST | `/admin/salas/**` | CRUD de salas |
| GET/POST | `/admin/sesiones/**` | CRUD de sesiones |

## Diagramas

Los diagramas editables estan en `docs/diagramas/`.

- `diagrama-er.puml`
- `diagrama-clases.puml`
- `arquitectura.puml`
- `secuencia-compra.puml`
- `casos-uso.puml`

La imagen `casos-uso.png` se elimina de esta version para no ensenar una imagen antigua. Si se quiere volver a tener PNG, debe regenerarse desde `casos-uso.puml`.

## Tests

El proyecto incluye tests unitarios para servicios:

- `CompraServiceTest`
- `UsuarioServiceTest`

Ejecucion:

```bash
./mvnw test
```

## Alcance de la version final

Incluido:

- Cartelera, filtros y detalle de pelicula.
- Sesiones.
- Registro y login.
- Seleccion visual de butacas.
- Compra simulada.
- Mis compras.
- CRUD de peliculas, salas y sesiones.
- Docker y Docker Compose.

No incluido:

- Envio de emails.
- Generacion o descarga de entradas PDF.
- Administracion de compras.
