
# LogiTrack S.A - Sistema de Gestion de Bodegas

## Descripcion

LogiTrack es un sistema backend desarrollado en **Spring Boot** para la gestion de bodegas, productos y movimientos de inventario. Permite registrar entradas y salidas de productos, controlar el stock en tiempo real, gestionar multiples bodegas y llevar un registro de auditoria de todas las operaciones realizadas.

---

## Stack Tecnologico

| Tecnologia | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.3.5 |
| MySQL | 8.x |
| Lombok | latest |
| JWT (jjwt) | 0.11.5 |
| SpringDoc / Swagger | latest |

---

## Requisitos previos

- Java 21 instalado
- MySQL corriendo en puerto 3306
- Maven (incluido con el proyecto via `mvnw`)

---

## Instalacion y ejecucion

### 1. Clonar el proyecto

```bash
cd LogiTrack
```

### 2. Crear la base de datos en MySQL

```sql
CREATE DATABASE logitrack;
```

### 3. Configurar `application.properties`

Ubicado en `src/main/resources/application.properties`:

```properties
spring.application.name=logitrack

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Swagger
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.packagesToScan=com.s1.LogiTrack

# JWT
jwt.secret=LogiTrackSaExtraSecure&SecretPassword
jwt.expiration=86400000

# Puerto
server.port=8080
```

### 4. Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8080`

---

## Estructura del proyecto

```
src/main/java/com/s1/LogiTrack/
├── Config/
│   └── SwaggerConfig.java
├── Controller/
│   ├── AuthController.java
│   ├── BodegaController.java
│   ├── ProductoController.java
│   ├── MovimientoController.java
│   ├── MovimientoDetalleController.java
│   ├── AuditoriaController.java
│   └── PersonaController.java
├── Dto/
│   ├── Request/
│   └── Response/
├── Model/
│   ├── Persona.java
│   ├── Bodega.java
│   ├── Producto.java
│   ├── Movimiento.java
│   ├── MovimientoDetalle.java
│   ├── Auditoria.java
│   ├── Rol.java
│   ├── TipoMovimiento.java
│   └── TipoOperacion.java
├── Repository/
├── Security/
│   ├── JwtUtil.java
│   ├── JwtFilter.java
│   ├── UserDetailsServiceImpl.java
│   ├── SecurityConfig.java
│   └── SecurityUtils.java
└── Service/
    ├── interfaces/
    └── impl/
```

---

## Documentacion de endpoints

### Autenticacion

#### Registro de usuario

```
POST /api/auth/registro
```

**Body:**
```json
{
  "nombre": "Juan",
  "apellido": "Perez",
  "documento": "123456789",
  "email": "juan@empresa.com",
  "password": "123456",
  "rol": "ADMIN"
}
```

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Perez",
  "documento": "123456789",
  "email": "juan@empresa.com",
  "rol": "ADMIN"
}
```

---

#### Login

```
POST /api/auth/login
```

**Body:**
```json
{
  "email": "juan@empresa.com",
  "password": "123456"
}
```

**Respuesta exitosa (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan@empresa.com",
  "rol": "ADMIN"
}
```

>  El token debe enviarse en todas las peticiones siguientes en el header:
> `Authorization: Bearer <token>`

---

### Bodegas

#### Crear bodega

```
POST /api/bodegas
Authorization: Bearer <token>
```

**Body:**
```json
{
  "nombre": "Bodega Central",
  "ubicacion": "Piso 1 Zona A",
  "capacidad": 500,
  "encargadoId": 1
}
```

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "nombre": "Bodega Central",
  "ubicacion": "Piso 1 Zona A",
  "capacidad": 500,
  "encargadoId": 1,
  "encargadoNombre": "Juan Perez"
}
```

#### Listar bodegas

```
GET /api/bodegas
Authorization: Bearer <token>
```

#### Eliminar bodega

```
DELETE /api/bodegas/{id}
Authorization: Bearer <token>
```

---

### Productos

#### Crear producto

```
POST /api/productos
Authorization: Bearer <token>
```

**Body:**
```json
{
  "nombre": "Pallet de madera",
  "categoria": "Logistica",
  "stock": 100,
  "precio": 15000,
  "bodegaId": 1
}
```

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "nombre": "Pallet de madera",
  "categoria": "Logistica",
  "stock": 100,
  "precio": 15000.00,
  "bodegaId": 1,
  "bodegaNombre": "Bodega Central"
}
```

#### Productos con stock bajo

```
GET /api/productos/stock-bajo?limite=10
Authorization: Bearer <token>
```

---

### Movimientos

#### Registrar movimiento

```
POST /api/movimientos
Authorization: Bearer <token>
```

**Body:**
```json
{
  "tipoMovimiento": "ENTRADA",
  "descripcion": "Reposicion mensual",
  "bodegaOrigenId": null,
  "bodegaDestinoId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 50
    }
  ]
}
```

**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "tipoMovimiento": "ENTRADA",
  "descripcion": "Reposicion mensual",
  "fecha": "2026-03-16T19:30:00",
  "usuarioNombre": "Juan",
  "bodegaOrigenNombre": null,
  "bodegaDestinoNombre": "Bodega Central",
  "detalles": [
    {
      "id": 1,
      "productoId": 1,
      "productoNombre": "Pallet de madera",
      "cantidad": 50
    }
  ]
}
```

#### Movimientos por rango de fechas

```
GET /api/movimientos/por-fechas?inicio=2026-01-01T00:00:00&fin=2026-12-31T23:59:59
Authorization: Bearer <token>
```

---

### Auditorias

#### Listar auditorias

```
GET /api/auditoria
Authorization: Bearer <token>
```

**Respuesta exitosa (200):**
```json
[
  {
    "id": 1,
    "entidad": "Producto",
    "operacion": "CREAR",
    "fecha": "2026-03-16T19:34:00",
    "usuarioId": 1,
    "usuarioNombre": "Juan",
    "valorAnterior": null,
    "valorNuevo": "Se creo el producto: Pallet de madera"
  }
]
```

#### Filtrar por operacion

```
GET /api/auditoria/por-operacion?operacion=CREAR
Authorization: Bearer <token>
```

---

## Seguridad y roles

| Endpoint | ADMIN | EMPLEADO |
|---|---|---|
| `/api/auth/**` | ✅ | ✅ |
| `/api/bodegas/**` | ✅ | ✅ |
| `/api/productos/**` | ✅ | ✅ |
| `/api/movimientos/**` | ✅ | ✅ |
| `/api/personas/**` | ✅ | ❌ |
| `/api/auditoria/**` | ✅ | ❌ |

---

## Swagger UI

Accede a la documentacion interactiva en:

```
http://localhost:8080/swagger-ui/index.html
```

Para autenticarte en Swagger:
1. Ejecuta `POST /api/auth/login` y copia el token
2. Click en el boton **Authorize** 🔒
3. Pega el token (sin escribir `Bearer`)
4. Click en **Authorize** → **Close**

---

## Frontend

El proyecto incluye una carpeta `frontend/` con una interfaz web desarrollada en HTML, CSS y JavaScript puro que consume todos los endpoints del backend.

### Estructura

```
frontend/
├── index.html
├── style/
│   └── style.css
└── script/
    └── app.js
```

### Como ejecutar el frontend

1. Instala la extension **Live Server** en VS Code
2. Click derecho sobre `index.html` → **Open with Live Server**
3. Se abre en `http://127.0.0.1:5500`
4. Ingresa con las credenciales creadas

### Funcionalidades del frontend

- Login y registro con autenticacion JWT
- Dashboard con estadisticas en tiempo real
- Gestion de bodegas (crear, listar, eliminar)
- Gestion de productos (crear, listar, eliminar)
- Registro de movimientos de inventario
- Consulta y filtrado de auditorias

---

## Auditoria automatica

El sistema registra automaticamente en la tabla de auditorias cada operacion de creacion, actualizacion o eliminacion realizada sobre bodegas, productos y movimientos, incluyendo el usuario que realizo la accion y la fecha exacta.

---

## Usuario de prueba

```
Email:    admin@logitrack.com
Password: admin123
Rol:      ADMIN
```