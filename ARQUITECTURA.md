# Arquitectura DevPulse BP

## Visión General

DevPulse BP es una solución completa de comunicación técnica para desarrolladores, compuesta por:
- **Plugin IntelliJ IDEA** (MVP completado)
- **Plugin VSCode** (Fase 4)
- **Backend Services** (Fase 2)
- **Panel de Administración** (Fase 3)

## Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                         DESARROLLADORES                          │
├──────────────────────┬──────────────────────────────────────────┤
│  IntelliJ IDEA       │         VSCode                           │
│  ┌────────────────┐  │  ┌────────────────┐                     │
│  │ DevPulse Plugin│  │  │ DevPulse Plugin│                     │
│  │  - Tool Window │  │  │  - Sidebar     │                     │
│  │  - Notifications│  │  │  - Notifications│                    │
│  │  - Settings    │  │  │  - Settings    │                     │
│  └────────┬───────┘  │  └────────┬───────┘                     │
└───────────┼──────────┴───────────┼─────────────────────────────┘
            │                      │
            │   REST API + WebSocket
            │                      │
┌───────────▼──────────────────────▼─────────────────────────────┐
│                    BACKEND SERVICES                             │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              API Gateway (Spring Cloud Gateway)          │  │
│  └──────────────────┬───────────────────────────────────────┘  │
│                     │                                           │
│  ┌──────────────────┼───────────────────────────────────────┐  │
│  │  Message Service │  WebSocket Service │  Telemetry Service│  │
│  │  (Spring Boot)   │  (Spring WebSocket)│  (Spring Boot)   │  │
│  └──────────────────┴───────────────────────────────────────┘  │
│                     │                                           │
│  ┌──────────────────▼───────────────────────────────────────┐  │
│  │              PostgreSQL Database                          │  │
│  │  - messages      - users         - telemetry             │  │
│  │  - schedules     - read_status   - plugin_versions       │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
            │
            │   REST API
            │
┌───────────▼─────────────────────────────────────────────────────┐
│                  PANEL DE ADMINISTRACIÓN                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              Web Application (React)                      │  │
│  │  - Crear/Editar Mensajes    - Programar Envíos          │  │
│  │  - Gestión de Usuarios      - Métricas y Reportes       │  │
│  │  - Segmentación             - Gestión de Versiones      │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Componentes Detallados

### 1. Plugin IntelliJ IDEA (MVP - Completado)

#### Tecnologías
- **Lenguaje**: Java 17
- **Framework**: IntelliJ Platform SDK
- **Build**: Gradle 8.5
- **Persistencia**: JSON local (Gson)

#### Componentes
```
com.pichincha.devpulse/
├── model/
│   ├── Message.java           # Modelo de mensaje
│   ├── MessageType.java       # Enum de tipos
│   └── Priority.java          # Enum de prioridades
├── service/
│   ├── MessageService.java    # Gestión de mensajes
│   ├── NotificationService.java # Sistema de notificaciones
│   └── TelemetryService.java  # Telemetría
├── ui/
│   ├── DevPulseToolWindow.java # Ventana principal
│   └── DevPulseToolWindowFactory.java
├── settings/
│   └── DevPulseSettings.java  # Configuración persistente
├── listeners/
│   └── DevPulseStartupListener.java # Eventos de inicio
└── actions/
    └── RefreshMessagesAction.java # Acciones del usuario
```

#### Funcionalidades Implementadas
- ✅ Centro de mensajes (Tool Window)
- ✅ Sistema de notificaciones
- ✅ 6 tipos de mensajes
- ✅ 4 niveles de prioridad
- ✅ Almacenamiento local
- ✅ Telemetría básica
- ✅ Ejemplos de código
- ✅ Enlaces externos

### 2. Backend Services (Fase 2 - Por Implementar)

#### Stack Tecnológico Propuesto
- **Framework**: Spring Boot 3.2
- **Base de Datos**: PostgreSQL 15
- **Cache**: Redis (opcional)
- **Mensajería**: WebSocket (Spring WebSocket)
- **Seguridad**: Spring Security + JWT
- **Documentación**: OpenAPI 3.0 (Swagger)

#### Microservicios

##### 2.1 Message Service
**Responsabilidad**: Gestión de mensajes y contenido

**Endpoints**:
```
POST   /api/v1/messages              # Crear mensaje
GET    /api/v1/messages              # Listar mensajes
GET    /api/v1/messages/{id}         # Obtener mensaje
PUT    /api/v1/messages/{id}         # Actualizar mensaje
DELETE /api/v1/messages/{id}         # Eliminar mensaje
POST   /api/v1/messages/{id}/schedule # Programar envío
GET    /api/v1/messages/unread       # Mensajes sin leer
POST   /api/v1/messages/{id}/read    # Marcar como leído
```

**Modelo de Datos**:
```sql
CREATE TABLE messages (
  id UUID PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  type VARCHAR(50) NOT NULL,
  priority VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  scheduled_for TIMESTAMP,
  link VARCHAR(500),
  code_example TEXT,
  created_by VARCHAR(100),
  target_audience JSONB,
  active BOOLEAN DEFAULT true
);

CREATE TABLE message_reads (
  id UUID PRIMARY KEY,
  message_id UUID REFERENCES messages(id),
  user_email VARCHAR(255) NOT NULL,
  read_at TIMESTAMP NOT NULL,
  plugin_version VARCHAR(50)
);

CREATE INDEX idx_messages_type ON messages(type);
CREATE INDEX idx_messages_priority ON messages(priority);
CREATE INDEX idx_messages_scheduled ON messages(scheduled_for);
CREATE INDEX idx_message_reads_user ON message_reads(user_email);
```

##### 2.2 WebSocket Service
**Responsabilidad**: Notificaciones en tiempo real

**Endpoints**:
```
WS /ws/notifications              # WebSocket endpoint
```

**Flujo**:
1. Plugin se conecta al WebSocket al iniciar
2. Backend autentica conexión (JWT)
3. Backend envía notificaciones cuando:
   - Se crea un nuevo mensaje
   - Se programa un envío
   - Hay actualizaciones del plugin

##### 2.3 Telemetry Service
**Responsabilidad**: Métricas y reportes

**Endpoints**:
```
POST /api/v1/telemetry/install      # Registro de instalación
POST /api/v1/telemetry/startup      # Registro de inicio
POST /api/v1/telemetry/read         # Registro de lectura
GET  /api/v1/telemetry/stats        # Estadísticas generales
GET  /api/v1/telemetry/users        # Usuarios activos
GET  /api/v1/telemetry/messages/{id}/stats # Stats de mensaje
```

**Modelo de Datos**:
```sql
CREATE TABLE plugin_installations (
  id UUID PRIMARY KEY,
  user_email VARCHAR(255) NOT NULL,
  plugin_version VARCHAR(50) NOT NULL,
  ide_version VARCHAR(100),
  os VARCHAR(50),
  installed_at TIMESTAMP NOT NULL,
  last_seen TIMESTAMP NOT NULL
);

CREATE TABLE telemetry_events (
  id UUID PRIMARY KEY,
  event_type VARCHAR(50) NOT NULL,
  user_email VARCHAR(255) NOT NULL,
  plugin_version VARCHAR(50),
  metadata JSONB,
  created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_installations_user ON plugin_installations(user_email);
CREATE INDEX idx_events_type ON telemetry_events(event_type);
CREATE INDEX idx_events_user ON telemetry_events(user_email);
```

##### 2.4 User Service
**Responsabilidad**: Autenticación y gestión de usuarios

**Endpoints**:
```
POST /api/v1/auth/login             # Login
POST /api/v1/auth/refresh           # Refresh token
GET  /api/v1/users/me               # Perfil del usuario
PUT  /api/v1/users/me/settings      # Actualizar configuración
```

#### Seguridad

**Autenticación**:
- JWT tokens para API REST
- Token en header: `Authorization: Bearer <token>`
- Refresh tokens con rotación

**Autorización**:
- Roles: `DEVELOPER`, `ADMIN`, `CHAPTER_LEAD`
- Permisos por endpoint

**Configuración**:
```yaml
security:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 3600000  # 1 hora
    refresh-expiration: 604800000  # 7 días
```

### 3. Panel de Administración (Fase 3 - Por Implementar)

#### Stack Tecnológico
- **Framework**: React 18 + TypeScript
- **UI Library**: Material-UI o Ant Design
- **State Management**: Redux Toolkit
- **API Client**: Axios
- **Charts**: Recharts o Chart.js

#### Funcionalidades

##### 3.1 Gestión de Mensajes
- Crear/editar/eliminar mensajes
- Editor WYSIWYG para contenido
- Editor de código para ejemplos
- Vista previa del mensaje
- Validación de campos

##### 3.2 Programación de Envíos
- Calendario de envíos
- Programar fecha y hora
- Envío inmediato
- Envío recurrente (opcional)

##### 3.3 Segmentación de Audiencia
- Por email
- Por versión de plugin
- Por IDE (IntelliJ/VSCode)
- Por equipo/chapter

##### 3.4 Métricas y Reportes
- Dashboard con KPIs:
  - Total de instalaciones
  - Usuarios activos
  - Mensajes enviados/leídos
  - Tasa de lectura por tipo
- Gráficos de tendencias
- Exportar reportes (CSV/PDF)

##### 3.5 Gestión de Versiones
- Listar versiones del plugin
- Marcar versión como obsoleta
- Notificar actualizaciones

### 4. Plugin VSCode (Fase 4 - Por Implementar)

#### Tecnologías
- **Lenguaje**: TypeScript
- **Framework**: VSCode Extension API
- **Build**: Webpack
- **Persistencia**: VSCode Storage API

#### Componentes Equivalentes
```
src/
├── models/
│   ├── Message.ts
│   ├── MessageType.ts
│   └── Priority.ts
├── services/
│   ├── MessageService.ts
│   ├── NotificationService.ts
│   └── TelemetryService.ts
├── views/
│   └── MessageTreeView.ts
└── extension.ts
```

## Modelo de Datos Completo

### Entidades Principales

#### Message
```typescript
interface Message {
  id: string;
  title: string;
  content: string;
  type: MessageType;
  priority: Priority;
  createdAt: Date;
  scheduledFor?: Date;
  link?: string;
  codeExample?: string;
  createdBy: string;
  targetAudience?: {
    emails?: string[];
    teams?: string[];
    minPluginVersion?: string;
    ides?: ('intellij' | 'vscode')[];
  };
  active: boolean;
}
```

#### User
```typescript
interface User {
  id: string;
  email: string;
  name: string;
  role: 'DEVELOPER' | 'ADMIN' | 'CHAPTER_LEAD';
  team?: string;
  settings: {
    enableNotifications: boolean;
    enableTelemetry: boolean;
    notificationTypes: MessageType[];
  };
}
```

#### Installation
```typescript
interface Installation {
  id: string;
  userEmail: string;
  pluginVersion: string;
  ideVersion: string;
  os: string;
  installedAt: Date;
  lastSeen: Date;
}
```

## Flujos de Trabajo

### Flujo 1: Publicar Mensaje
```
1. Admin crea mensaje en panel web
2. Backend valida y guarda en BD
3. Si es envío inmediato:
   a. Backend envía evento WebSocket
   b. Plugins conectados reciben notificación
   c. Plugin muestra notificación al usuario
4. Si es programado:
   a. Scheduler ejecuta en fecha/hora
   b. Continúa desde paso 3
```

### Flujo 2: Leer Mensaje
```
1. Usuario abre mensaje en plugin
2. Plugin marca como leído localmente
3. Plugin envía telemetría a backend
4. Backend registra lectura en BD
5. Backend actualiza métricas
```

### Flujo 3: Sincronización
```
1. Plugin inicia o usuario hace refresh
2. Plugin envía timestamp de última sync
3. Backend retorna mensajes nuevos/actualizados
4. Plugin actualiza almacenamiento local
5. Plugin muestra notificación si hay nuevos
```

## Consideraciones de Infraestructura

### Banco Pichincha - Restricciones

#### Red y Conectividad
- Firewall corporativo
- Proxy obligatorio
- Whitelist de dominios
- VPN para acceso externo

#### Despliegue
- Kubernetes on-premise o cloud privado
- CI/CD con Jenkins o GitLab CI
- Registry interno de Docker
- Artifact repository (Nexus/Artifactory)

#### Base de Datos
- PostgreSQL en cluster
- Backups automáticos
- Replicación para HA

#### Monitoreo
- Prometheus + Grafana
- ELK Stack para logs
- Alertas vía email/Teams

### Configuración de Plugins para Entorno Corporativo

```java
// Plugin IntelliJ
public class DevPulseSettings {
  public String apiUrl = System.getenv("DEVPULSE_API_URL");
  public String proxyHost = System.getenv("HTTP_PROXY_HOST");
  public int proxyPort = Integer.parseInt(
    System.getenv().getOrDefault("HTTP_PROXY_PORT", "8080")
  );
  public boolean useProxy = Boolean.parseBoolean(
    System.getenv().getOrDefault("USE_PROXY", "true")
  );
}
```

## Roadmap de Implementación

### Sprint 1-2 (Semanas 1-2): Backend Core
- [ ] Setup proyecto Spring Boot
- [ ] Modelo de datos y migraciones
- [ ] Message Service endpoints
- [ ] Autenticación JWT
- [ ] Tests unitarios

### Sprint 3-4 (Semanas 3-4): WebSocket y Telemetría
- [ ] WebSocket Service
- [ ] Telemetry Service
- [ ] Integración con plugin IntelliJ
- [ ] Tests de integración

### Sprint 5-6 (Semanas 5-6): Panel de Administración
- [ ] Setup React + TypeScript
- [ ] Gestión de mensajes
- [ ] Programación de envíos
- [ ] Dashboard de métricas

### Sprint 7-8 (Semanas 7-8): VSCode Plugin
- [ ] Setup proyecto VSCode
- [ ] Port de funcionalidades
- [ ] Integración con backend
- [ ] Tests end-to-end

## Métricas de Éxito

### KPIs Técnicos
- **Disponibilidad**: > 99.5%
- **Latencia API**: < 200ms p95
- **Tiempo de sincronización**: < 5s
- **Tasa de error**: < 0.1%

### KPIs de Negocio
- **Tasa de instalación**: > 80% desarrolladores
- **Tasa de lectura**: > 70% mensajes
- **Engagement**: > 60% usuarios activos semanales
- **Satisfacción**: > 4/5 en encuestas

## Seguridad y Compliance

### Datos Sensibles
- Emails encriptados en BD
- Logs sin información personal
- GDPR compliance (derecho al olvido)

### Auditoría
- Logs de todas las operaciones admin
- Trazabilidad de cambios
- Retención de logs: 90 días

---

**Versión**: 1.0  
**Última actualización**: Febrero 2026  
**Autor**: Chapter de Desarrollo - Banco Pichincha
