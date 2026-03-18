# Conexión Java con Supabase (PostgreSQL)

Este proyecto muestra cómo conectarse desde **Java** a una base de datos **Supabase PostgreSQL** usando **JDBC**, crear una tabla, insertar un registro y consultar los datos almacenados.

## Descripción

La clase `ConexionSupabase` realiza las siguientes acciones:

1. Se conecta a una base de datos PostgreSQL alojada en Supabase.
2. Crea la tabla `alumnos` si no existe.
3. Inserta un alumno de ejemplo.
4. Consulta todos los alumnos almacenados.
5. Muestra el resultado por consola.

---

## Requisitos

- **Java 17** o superior recomendado
- **Driver JDBC de PostgreSQL**
- Una base de datos activa en **Supabase**

---

## Dependencia necesaria

Debes añadir el driver JDBC de PostgreSQL a tu proyecto.

### Si usas Maven

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.5</version>
</dependency>
