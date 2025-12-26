# 🚀 Guía de Inicio Rápido - ms-sp-salesforce-config

## ⚡ Inicio Rápido (5 minutos)

### Prerequisitos
```bash
✅ Java 17 LTS instalado
✅ Docker & Docker Compose instalados
✅ Credenciales de Salesforce disponibles
```

### 1️⃣ Configurar Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto:

```bash
cat > .env << 'EOF'
SALESFORCE_PASSWORD=tu_password_salesforce
SALESFORCE_CLIENT_ID=tu_client_id
SALESFORCE_CLIENT_SECRET=tu_client_secret
SALESFORCE_SECURITY_TOKEN=tu_security_token
EOF
```

### 2️⃣ Ejecutar con Docker Compose (Más Rápido)

```bash
# Build y run en un solo comando
docker-compose up --build

# En otro terminal, probar la API
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/v1/companias?pais=PE
```

✅ **Listo! La aplicación está corriendo en http://localhost:8080**

### 3️⃣ Verificar Swagger UI

Abre en tu navegador:
```
http://localhost:8080/swagger-ui.html
```

## 📋 Comandos Útiles

### Desarrollo Local (sin Docker)

```bash
# Compilar
./gradlew clean build

# Ejecutar tests
./gradlew test

# Ver reporte de cobertura
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html

# Ejecutar aplicación
SPRING_PROFILES_ACTIVE=dev \
SALESFORCE_PASSWORD=tu_password \
SALESFORCE_CLIENT_ID=tu_client_id \
SALESFORCE_CLIENT_SECRET=tu_client_secret \
SALESFORCE_SECURITY_TOKEN=tu_token \
./gradlew bootRun
```

### Docker Manual

```bash
# Build image
docker build -t ms-sp-salesforce-config:latest .

# Verificar tamaño (debe ser < 180 MB)
docker images ms-sp-salesforce-config:latest

# Run container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SALESFORCE_PASSWORD=tu_password \
  -e SALESFORCE_CLIENT_ID=tu_client_id \
  -e SALESFORCE_CLIENT_SECRET=tu_client_secret \
  -e SALESFORCE_SECURITY_TOKEN=tu_token \
  ms-sp-salesforce-config:latest
```

### Tests

```bash
# Todos los tests
./gradlew test

# Solo tests unitarios
./gradlew test --tests "*Test"

# Solo tests de integración
./gradlew test --tests "*IntegrationTest"

# Con reporte de cobertura
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

## 🌐 Endpoints Disponibles

### API Endpoints
```bash
# Listar compañías por país
GET http://localhost:8080/api/v1/companias?pais=PE

# Obtener compañía por ID
GET http://localhost:8080/api/v1/companias/{id}
```

### Actuator Endpoints
```bash
# Health check
GET http://localhost:8080/actuator/health

# Application info
GET http://localhost:8080/actuator/info

# Metrics
GET http://localhost:8080/actuator/metrics
```

### Documentación
```bash
# Swagger UI
http://localhost:8080/swagger-ui.html

# OpenAPI JSON
http://localhost:8080/v3/api-docs
```

## 🧪 Ejemplos de Uso con cURL

### Obtener compañías de Perú
```bash
curl -X GET "http://localhost:8080/api/v1/companias?pais=PE" \
  -H "Accept: application/json" | jq
```

### Obtener compañía específica
```bash
curl -X GET "http://localhost:8080/api/v1/companias/a0X5e000000ABC123" \
  -H "Accept: application/json" | jq
```

### Health check
```bash
curl http://localhost:8080/actuator/health | jq
```

## 🐛 Troubleshooting

### Problema: Error de autenticación con Salesforce
**Solución**: Verifica que todas las credenciales estén correctas y que el security token esté actualizado.

```bash
# Verificar variables de entorno
docker-compose config
```

### Problema: Puerto 8080 ya está en uso
**Solución**: Cambiar el puerto en docker-compose.yml o detener el proceso que usa el puerto 8080.

```bash
# Detener contenedor actual
docker-compose down

# Cambiar puerto en docker-compose.yml
ports:
  - "8081:8080"  # Usar puerto 8081 en el host
```

### Problema: Tests fallan localmente
**Solución**: Asegúrate de usar el profile test.

```bash
SPRING_PROFILES_ACTIVE=test ./gradlew test
```

### Problema: Docker image muy grande
**Solución**: Verifica que estés usando multi-stage build y Alpine.

```bash
# Verificar tamaño
docker images ms-sp-salesforce-config:latest

# Debe mostrar menos de 180 MB
```

## 📊 Verificar Instalación

Ejecuta este script para verificar que todo esté correcto:

```bash
#!/bin/bash

echo "🔍 Verificando instalación..."

# Java
echo -n "Java 17: "
java -version 2>&1 | grep -q "17" && echo "✅" || echo "❌"

# Docker
echo -n "Docker: "
docker --version > /dev/null 2>&1 && echo "✅" || echo "❌"

# Docker Compose
echo -n "Docker Compose: "
docker-compose --version > /dev/null 2>&1 && echo "✅" || echo "❌"

# Archivos del proyecto
echo -n "build.gradle: "
[ -f build.gradle ] && echo "✅" || echo "❌"

echo -n "Dockerfile: "
[ -f Dockerfile ] && echo "✅" || echo "❌"

echo -n "docker-compose.yml: "
[ -f docker-compose.yml ] && echo "✅" || echo "❌"

echo ""
echo "Si todos tienen ✅, estás listo para comenzar!"
```

## 🎯 Siguientes Pasos

1. ✅ **Ejecutar la aplicación localmente** (arriba)
2. ⚠️  **Completar módulos Terraform** (ver TERRAFORM_INSTRUCTIONS.md)
3. ⚠️  **Configurar CI/CD en GitHub** (push del código)
4. ⚠️  **Deploy a AWS DEV** (terraform apply)
5. ⚠️  **Configurar monitoreo** (CloudWatch)

## 📚 Documentación Adicional

- **README.md** - Documentación completa del proyecto
- **ESTRUCTURA_PROYECTO.md** - Estructura detallada de archivos
- **TERRAFORM_INSTRUCTIONS.md** - Instrucciones para Terraform
- **Swagger UI** - http://localhost:8080/swagger-ui.html (cuando está corriendo)

## 🆘 Soporte

¿Problemas? Revisa:
1. Logs de Docker: `docker-compose logs -f`
2. Logs de aplicación: En CloudWatch (AWS) o logs locales
3. Health endpoint: `curl http://localhost:8080/actuator/health`

---

**¡Éxito con tu proyecto!** 🎉
