# 🔧 Guía de Solución de Problemas - Base de Datos

## ❌ Error: "Schema validation: missing table [order_products]"

### **Causa del Problema**

Este error ocurre cuando:
1. La aplicación Spring Boot está configurada con `ddl-auto: validate`
2. La base de datos PostgreSQL no tiene las tablas necesarias
3. Hibernate intenta validar el esquema y falla

### **✅ Solución Rápida**

Ejecuta el script PowerShell que creé para ti:

```powershell
.\run-sql-scripts.ps1
```

Este script ejecutará automáticamente los archivos SQL:
- `db/postgresql/01-schema.sql` (crea las tablas)
- `db/postgresql/02-data.sql` (inserta datos de prueba)

---

## 🔄 Reinicializar la Base de Datos Completa

Si necesitas borrar todo y empezar desde cero:

```powershell
.\reset-db.ps1
```

**⚠️ ADVERTENCIA:** Esto borrará todos los datos de la base de datos.

---

## 🛠️ Configuración de `ddl-auto`

La aplicación ahora soporta configuración mediante variables de entorno:

### **Modo Producción (Predeterminado)**
```yaml
spring.jpa.hibernate.ddl-auto: validate
```
- ✅ Valida que las tablas existan
- ❌ No crea ni modifica tablas
- 🎯 **Requiere** ejecutar scripts SQL manualmente

### **Modo Desarrollo**
```powershell
# Windows PowerShell
$env:DDL_AUTO="update"
.\gradlew bootRun

# O en application.yaml temporalmente
spring.jpa.hibernate.ddl-auto: update
```
- ✅ Crea tablas automáticamente
- ✅ Actualiza el esquema sin borrar datos
- ⚠️ NO recomendado para producción

### **Modo Testing**
```yaml
spring.jpa.hibernate.ddl-auto: create-drop
```
- ✅ Crea tablas al inicio
- ⚠️ Borra todas las tablas al cerrar
- 🎯 Solo para pruebas automatizadas

---

## 📋 Comandos Útiles

### Verificar que PostgreSQL está corriendo
```powershell
docker-compose ps
```

### Ver tablas en la base de datos
```powershell
docker exec -it erp-postgres psql -U rojinsky -d erp_db -c "\dt"
```

### Conectarse a PostgreSQL
```powershell
docker exec -it erp-postgres psql -U rojinsky -d erp_db
```

### Ver datos en una tabla
```powershell
docker exec -it erp-postgres psql -U rojinsky -d erp_db -c "SELECT * FROM order_products LIMIT 10;"
```

### Contar registros
```powershell
docker exec -it erp-postgres psql -U rojinsky -d erp_db -c "SELECT COUNT(*) FROM order_products;"
```

---

## 🚀 Flujo de Trabajo Recomendado

### **Primera vez que ejecutas el proyecto:**

1. Inicia los contenedores Docker:
   ```powershell
   docker-compose up -d
   ```

2. Ejecuta los scripts SQL:
   ```powershell
   .\run-sql-scripts.ps1
   ```

3. Inicia la aplicación:
   ```powershell
   .\gradlew bootRun
   ```

### **Desarrollo diario:**

1. Asegúrate de que Docker esté corriendo:
   ```powershell
   docker-compose up -d
   ```

2. Inicia la aplicación:
   ```powershell
   .\gradlew bootRun
   ```

### **Si cambias el esquema SQL:**

1. Detén la aplicación

2. Ejecuta los scripts actualizados:
   ```powershell
   .\run-sql-scripts.ps1
   ```

3. O reinicializa completamente:
   ```powershell
   .\reset-db.ps1
   ```

---

## 🔍 Diagnóstico de Problemas

### El contenedor de PostgreSQL no inicia
```powershell
docker-compose logs postgres
```

### La aplicación no se conecta a PostgreSQL
1. Verifica la configuración en `erp-api/src/main/resources/application.yaml`
2. Confirma que las credenciales coincidan con `docker-compose.yml`
3. Verifica que el puerto 5432 no esté ocupado

### Las tablas no se crean automáticamente
- PostgreSQL solo ejecuta scripts en `/docker-entrypoint-initdb.d` cuando el contenedor se crea por primera vez
- Si el contenedor ya existía, usa `.\reset-db.ps1` o `.\run-sql-scripts.ps1`

---

## 📚 Más Información

- [Documentación de Spring Boot JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jpa-and-spring-data)
- [Hibernate ddl-auto](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#configurations-hbmddl)
- [PostgreSQL Docker](https://hub.docker.com/_/postgres)

