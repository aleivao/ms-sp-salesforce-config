# MS-SP-Salesforce-Config

Microservicio Spring Boot 3 para gestión de configuración de compañías desde Salesforce con arquitectura hexagonal.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Requisitos Previos](#requisitos-previos)
- [Configuración](#configuración)
- [Ejecución Local](#ejecución-local)
- [Testing](#testing)
- [Docker](#docker)
- [Despliegue en AWS](#despliegue-en-aws)
- [API Documentation](#api-documentation)
- [Monitoreo](#monitoreo)

## ✨ Características

- **Arquitectura Hexagonal (Ports & Adapters)**
- **Integración con Salesforce** (OAuth Username-Password Flow)
- **Cobertura de Tests**: 98%+
- **Circuit Breaker & Retry** con Resilience4j
- **Cache** con Caffeine (TTL: 5 minutos)
- **Docker Image** optimizada (<180MB) con Alpine
- **CI/CD** completo con GitHub Actions
- **Infraestructura como Código** con Terraform
- **Despliegue en AWS ECS Fargate**

## 🛠️ Tecnologías

- Java 17 LTS
- Spring Boot 3.2.1
- Gradle 8.5
- Spring WebFlux (WebClient)
- MapStruct
- Lombok
- Caffeine Cache
- Resilience4j
- JUnit 5 + Mockito + AssertJ
- WireMock
- Docker
- Terraform
- AWS (ECS Fargate, ECR, ALB, VPC)

## 🏗️ Arquitectura

```
ms-sp-salesforce-config/
├── domain/                    # Capa de dominio (entidades, excepciones)
├── application/              # Capa de aplicación (use cases, ports)
│   ├── port/
│   │   ├── in/              # Input ports (casos de uso)
│   │   └── out/             # Output ports (interfaces)
│   └── service/             # Implementación de use cases
└── infrastructure/           # Capa de infraestructura
    ├── adapter/
    │   ├── in/rest/        # Controladores REST
    │   └── out/salesforce/ # Adapter de Salesforce
    └── config/              # Configuraciones
```

## 📦 Requisitos Previos

- JDK 17
- Gradle 8.5+
- Docker 24+
- AWS CLI (para despliegue)
- Terraform 1.5+ (para infraestructura)

## ⚙️ Configuración

### Variables de Entorno

```bash
export SALESFORCE_USERNAME=webservice.zuperbodegas@integration.com.full
export SALESFORCE_PASSWORD=your-password
export SALESFORCE_CLIENT_ID=your-client-id
export SALESFORCE_CLIENT_SECRET=your-client-secret
export SPRING_PROFILES_ACTIVE=dev
```

### application.yml

El proyecto incluye configuraciones para tres ambientes:

- `application-dev.yml` - Desarrollo
- `application-qa.yml` - QA
- `application-prod.yml` - Producción

## 🚀 Ejecución Local

### Con Gradle

```bash
./gradlew bootRun
```

### Con Docker

```bash
docker-compose up
```

La aplicación estará disponible en: `http://localhost:8080`

## 🧪 Testing

### Ejecutar tests

```bash
./gradlew test
```

### Generar reporte de cobertura

```bash
./gradlew jacocoTestReport
```

El reporte HTML estará en: `build/reports/jacoco/test/html/index.html`

### Verificar cobertura mínima (98%)

```bash
./gradlew jacocoTestCoverageVerification
```

## 🐳 Docker

### Build

```bash
docker build -t ms-sp-salesforce-config:latest .
```

### Run

```bash
docker run -p 8080:8080 \
  -e SALESFORCE_USERNAME=your-username \
  -e SALESFORCE_PASSWORD=your-password \
  -e SALESFORCE_CLIENT_ID=your-client-id \
  -e SALESFORCE_CLIENT_SECRET=your-client-secret \
  ms-sp-salesforce-config:latest
```

### Verificar tamaño de imagen

```bash
docker images ms-sp-salesforce-config:latest
```

**Tamaño esperado**: < 180 MB

## ☁️ Despliegue en AWS

### Infraestructura

El proyecto incluye módulos de Terraform para:

- VPC con 2 AZs (subnets públicas y privadas)
- ECR (Elastic Container Registry)
- ECS Fargate (cluster, service, task definition)
- ALB (Application Load Balancer)
- CloudWatch Logs
- Secrets Manager

### Desplegar infraestructura

```bash
cd terraform/environments/dev
terraform init -backend-config=backend-config.tfvars
terraform plan
terraform apply
```

### Pipeline CI/CD

El proyecto incluye workflows de GitHub Actions:

#### CI Pipeline (`.github/workflows/ci.yml`)
- Build y tests
- Verificación de cobertura (98%)
- Security scan con Trivy
- Build y verificación de Docker image (<180MB)

#### CD Pipeline (`.github/workflows/cd.yml`)
- **DEV**: Auto-deploy al hacer push a branch `dev`
- **QA**: Deploy a QA con 1 aprobación (branch `qa`)
- **PROD**: Deploy a Producción con 2 aprobaciones (branch `main`)

## 📚 API Documentation

### Endpoints

#### 1. Listar compañías por país

```http
GET /api/v1/companias?pais={pais}
```

**Ejemplo:**
```bash
curl http://localhost:8080/api/v1/companias?pais=Peru
```

#### 2. Obtener compañía por ID

```http
GET /api/v1/companias/{id}
```

**Ejemplo:**
```bash
curl http://localhost:8080/api/v1/companias/a0X5f000000AbCdEFG
```

#### 3. Health Check

```http
GET /actuator/health
```

#### 4. Aplicación Info

```http
GET /actuator/info
```

### Swagger UI

Disponible en: `http://localhost:8080/swagger-ui.html`

## 📊 Monitoreo

### Actuator Endpoints

- `/actuator/health` - Estado de la aplicación
- `/actuator/info` - Información de la aplicación
- `/actuator/metrics` - Métricas
- `/actuator/prometheus` - Métricas en formato Prometheus

### CloudWatch

En AWS, los logs están disponibles en CloudWatch Logs:
- Log Group: `/ecs/ms-sp-salesforce-config-{env}`

### Métricas

- Cache hit ratio (Caffeine)
- Circuit breaker estado
- Request/Response times
- Error rates

## 🔐 Seguridad

- Credenciales almacenadas en AWS Secrets Manager
- Security scan con Trivy en CI/CD
- Usuario non-root en contenedor Docker
- HTTPS en producción (ALB con ACM)

## 📝 Campos del Objeto Salesforce Compania__c

El microservicio trabaja con los siguientes campos:

- `Id`, `Name`, `OwnerId`, `IsDeleted`, `CurrencyIsoCode`
- `CreatedDate`, `CreatedById`, `LastModifiedDate`, `LastModifiedById`
- `SystemModstamp`, `LastActivityDate`, `LastViewedDate`, `LastReferencedDate`
- `Codigo__c`, `Estado__c`, `Pais__c`, `Direccion__c`, `Telefono__c`, `Acronimo__c`
- `Integracion_ERP__c`, `Habilitar_seleccion_comprobante__c`
- `Habilitar_Comprobante_Preventa__c`, `Habilitar_Impuestos__c`, `Impuesto__c`
- `Habilitar_extra_modulo__c`, `Habilitar_linea_de_credito__c`
- `Acceso__c`, `Correo_compania__c`, `Enviar_a_Oracle__c`, `Enviar_a_Siesa__c`, `SELLIN__c`

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Proprietary - AJE Group © 2024

## 👥 Contacto

AJE Group - DevOps Team
- Email: devops@ajegroup.com
- Website: https://www.ajegroup.com
