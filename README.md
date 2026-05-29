# Restaurant API

Aplicacion Spring Boot para una fase del sistema de restaurante. Incluye autenticacion con JWT, roles de usuario, panel web basico y administracion de productos.

## Tecnologias

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- SQL Server
- Maven
- HTML, CSS y JavaScript

## Como correr la app

1. Crear una base de datos SQL Server llamada `restaurant_db`.
2. Verificar la configuracion en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=restaurant_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=restaurant_user
spring.datasource.password=Restaurant123!
```

3. Ejecutar la aplicacion:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

4. Abrir el login:

```text
http://localhost:8080/login.html
```

## Usuarios de prueba

La aplicacion crea estos usuarios automaticamente si no existen:

| Rol | Email | Password |
| --- | --- | --- |
| ADMINISTRADOR | admin@restaurant.com | Admin123! |
| MESERO | mesero@restaurant.com | Mesero123! |
| USUARIO | usuario@restaurant.com | Usuario123! |

## Roles

### ADMINISTRADOR

- Puede iniciar sesion.
- Puede ver productos.
- Puede crear productos.
- Puede editar productos.
- Puede borrar productos.
- Puede acceder a `/api/admin/test`.

### MESERO

- Puede iniciar sesion.
- Puede ver productos.
- Puede crear productos.
- Puede editar productos.
- No puede borrar productos.
- Puede acceder a `/api/mesero/test`.

### USUARIO

- Puede iniciar sesion.
- Puede ver productos existentes.
- No puede crear, editar ni borrar productos.
- Puede acceder a `/api/usuario/test`.

## Endpoints principales

### Autenticacion

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| POST | `/api/auth/login` | Inicia sesion y devuelve un token JWT |

Ejemplo de login:

```json
{
  "email": "admin@restaurant.com",
  "password": "Admin123!"
}
```

### Productos

| Metodo | Ruta | Permiso |
| --- | --- | --- |
| GET | `/api/products` | Publico |
| GET | `/api/products/{id}` | Publico |
| POST | `/api/products` | ADMINISTRADOR o MESERO |
| PUT | `/api/products/{id}` | ADMINISTRADOR o MESERO |
| DELETE | `/api/products/{id}` | ADMINISTRADOR |

Ejemplo de producto:

```json
{
  "name": "Hamburguesa",
  "price": 5.99,
  "stock": 10
}
```

### Pruebas de rol

| Metodo | Ruta | Permiso |
| --- | --- | --- |
| GET | `/api/admin/test` | ADMINISTRADOR |
| GET | `/api/mesero/test` | ADMINISTRADOR o MESERO |
| GET | `/api/usuario/test` | Usuario autenticado |

## Validaciones

Los productos tienen validaciones basicas:

- El nombre es obligatorio.
- El precio es obligatorio.
- El precio no puede ser negativo.
- El stock es obligatorio.
- El stock no puede ser negativo.

Los errores se responden con mensajes simples, por ejemplo:

```json
{
  "message": "Datos invalidos: El nombre es obligatorio"
}
```

## Frontend incluido

La app incluye dos paginas estaticas:

- `/login.html`: permite iniciar sesion con los usuarios de prueba.
- `/dashboard.html`: permite ver la sesion, probar rutas por rol y administrar productos segun permisos.

## Estado actual

Ya esta implementado:

- Login con JWT.
- Usuarios iniciales por rol.
- Seguridad por rol.
- CRUD de productos.
- Validaciones basicas de productos.
- Mensajes de error claros.
- Login y dashboard basico.

Pendiente para futuras fases:

- CRUD de mesas.
- Pedidos completos.
- Detalle de productos por pedido.
- Pagos.
- Reportes.
- Registro de usuarios.
- Administracion completa de usuarios.
