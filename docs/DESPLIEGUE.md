# 🐳 Guía de despliegue de CineSmart

CineSmart está preparado para ejecutarse de dos formas: **en local** (entorno de desarrollo) y **con Docker / Docker Compose** (despliegue principal y recomendado). El despliegue en una plataforma cloud como Railway queda como **mejora futura**.

---

## Opción principal: Docker Compose (recomendado)

Es la forma más sencilla y fiable. Levanta la aplicación y la base de datos MySQL juntas con un solo comando.

**Requisito:** tener Docker Desktop instalado.

```bash
# 1. Situarse en la raíz del proyecto (donde está docker-compose.yml)
cd cinesmart

# 2. Levantar la aplicación + MySQL
docker-compose up --build

# 3. Esperar 1-2 minutos a que arranque todo

# 4. Abrir en el navegador:
#    http://localhost:8080
```

Para detener:

```bash
docker-compose down
```

Para detener y borrar también los datos de la base de datos:

```bash
docker-compose down -v
```

### Cómo funciona

- El `Dockerfile` usa un **build multi-stage**: una fase compila el proyecto con Maven y Java 21, y otra fase copia solo el JAR resultante sobre una imagen ligera de Java. Así la imagen final es pequeña.
- El `docker-compose.yml` define dos servicios: la **aplicación** y **MySQL 8**, conectados por una red interna, con un volumen para que los datos persistan.
- La configuración se hace por **variables de entorno**, de modo que el mismo código funciona en local, en Docker o en cloud sin tocar nada.

---

## Opción de desarrollo: ejecución en local (sin Docker)

Útil para desarrollar y depurar desde el IDE.

**Requisitos:**
- Java 21
- MySQL 8 corriendo en el puerto 3306
- Maven (opcional, el proyecto incluye `mvnw`)

```bash
# 1. Crear la base de datos
mysql -u root -p
> CREATE DATABASE cinesmart;
> exit

# 2. Configurar variables de entorno (Linux/Mac)
export DB_USER=root
export DB_PASS=tu_contraseña
# En Windows PowerShell:
# $env:DB_USER="root"
# $env:DB_PASS="tu_contraseña"

# 3. Arrancar la aplicación
./mvnw spring-boot:run
# En Windows:
# mvnw.cmd spring-boot:run
```

Hibernate crea automáticamente las tablas en la primera ejecución y `DataInitializer` carga los datos de prueba (10 películas, salas, sesiones y los usuarios de prueba).

---

## Credenciales de prueba

| Rol   | Email                  | Contraseña |
|-------|------------------------|------------|
| ADMIN | `admin@cinesmart.com`  | `admin123` |
| USER  | `david@correo.com`     | `user123`  |

---

## Mejora futura: despliegue cloud (Railway)

Como línea de evolución, el proyecto está listo para desplegarse en una plataforma cloud de tipo PaaS como **Railway**, que detectaría automáticamente el `Dockerfile`, provisionaría una base de datos MySQL gestionada y permitiría exponer la aplicación con un dominio público y HTTPS.

Al estar ya dockerizado y parametrizado con variables de entorno, este paso **no requeriría cambios en el código**: bastaría con conectar el repositorio, añadir el servicio de MySQL y enlazar las variables de entorno (`MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`).

---

## Si algo va mal al arrancar con Docker

- **La app no responde:** revisa los logs con `docker-compose logs app`. Lo habitual es que MySQL aún no esté listo; espera un minuto y reinicia.
- **Error de conexión a la base de datos:** comprueba que el servicio de MySQL ha arrancado correctamente (`docker-compose ps`).
- **El puerto 8080 está ocupado:** cierra la aplicación que lo esté usando, o cambia el puerto en `docker-compose.yml`.
