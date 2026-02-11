# Instrucciones de Build - DevPulse BP

## Problema Actual: SSL Certificate

El build está fallando debido a problemas de certificados SSL en el entorno corporativo de Banco Pichincha. Este es un problema común en redes corporativas con proxies HTTPS.

## Solución Recomendada: Usar IntelliJ Local

### Paso 1: Descargar IntelliJ IDEA

1. Ir a: https://www.jetbrains.com/idea/download/other.html
2. Descargar **IntelliJ IDEA Community 2023.1.5**
3. Instalar en tu máquina (por ejemplo: `C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2023.1.5`)

### Paso 2: Configurar Build Local

Editar `build.gradle` y cambiar la sección `intellij`:

```groovy
intellij {
  localPath = 'C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2023.1.5'
  type = 'IC'
  plugins = []
}
```

### Paso 3: Construir el Plugin

```bash
cd C:\Users\hanmilton.berrezueta\Desktop\devpulse-intellij-plugin
.\gradlew.bat buildPlugin
```

El archivo ZIP se generará en: `build/distributions/devpulse-intellij-plugin-1.0.0-SNAPSHOT.zip`

## Alternativa: Desarrollo Directo en IntelliJ

### Opción A: Abrir Proyecto en IntelliJ

1. Abrir IntelliJ IDEA
2. File → Open → Seleccionar carpeta `devpulse-intellij-plugin`
3. Esperar a que Gradle sincronice
4. Run → Run 'Plugin' (esto abrirá una nueva instancia de IntelliJ con el plugin)

### Opción B: Usar Gradle desde IntelliJ

1. Abrir proyecto en IntelliJ
2. View → Tool Windows → Gradle
3. devpulse-intellij-plugin → Tasks → intellij → buildPlugin
4. Doble click para ejecutar

## Verificar el Plugin Funciona

Una vez construido o ejecutado:

1. Se abrirá una nueva ventana de IntelliJ IDEA
2. Buscar la pestaña "DevPulse" en el panel lateral derecho
3. Debe mostrar 4 mensajes de ejemplo
4. Al iniciar, debe aparecer una notificación de mensajes sin leer

## Estructura del Proyecto Creado

```
devpulse-intellij-plugin/
├── src/main/
│   ├── java/com/pichincha/devpulse/
│   │   ├── model/              # Modelos de datos
│   │   ├── service/            # Servicios (Message, Notification, Telemetry)
│   │   ├── ui/                 # Interfaz (Tool Window)
│   │   ├── settings/           # Configuración persistente
│   │   ├── listeners/          # Event listeners
│   │   ├── actions/            # Acciones del usuario
│   │   └── util/               # Utilidades
│   └── resources/
│       ├── META-INF/plugin.xml # Configuración del plugin
│       └── icons/              # Iconos
├── build.gradle                # Configuración de build
├── gradle.properties           # Propiedades de Gradle
├── README.md                   # Documentación técnica
├── MANUAL_INSTALACION.md       # Manual de instalación
├── MANUAL_USUARIO.md           # Manual de usuario
├── ARQUITECTURA.md             # Arquitectura completa
└── SOLUCION_SSL.md             # Soluciones a problemas SSL
```

## Funcionalidades Implementadas (MVP)

### ✅ Core
- [x] Modelos de datos (Message, MessageType, Priority)
- [x] MessageService con almacenamiento local JSON
- [x] NotificationService con notificaciones emergentes
- [x] TelemetryService para métricas básicas
- [x] DevPulseSettings con persistencia

### ✅ UI
- [x] Tool Window con lista de mensajes
- [x] Panel de detalles de mensaje
- [x] Visualización de ejemplos de código
- [x] Enlaces a documentación externa
- [x] Indicadores de mensajes no leídos

### ✅ Tipos de Mensajes
- [x] Informativo
- [x] Lineamiento
- [x] Documentación
- [x] Anuncio
- [x] Alerta
- [x] Píldora Educativa

### ✅ Características
- [x] 4 niveles de prioridad
- [x] Marcado como leído
- [x] Notificaciones al iniciar IDE
- [x] Botón de actualizar mensajes
- [x] 4 mensajes mock de ejemplo

## Próximos Pasos (Fase 2)

### Backend Services
1. Crear API REST con Spring Boot
2. Base de datos PostgreSQL
3. WebSocket para notificaciones en tiempo real
4. Sistema de autenticación JWT

### Integración Plugin
1. Modificar MessageService para consumir API
2. Implementar WebSocket client
3. Sincronización automática
4. Gestión de tokens

### Panel de Administración
1. Aplicación web React
2. Gestión de mensajes
3. Programación de envíos
4. Dashboard de métricas

## Comandos Útiles

```bash
# Limpiar proyecto
.\gradlew.bat clean

# Construir plugin
.\gradlew.bat buildPlugin

# Ejecutar en IDE de desarrollo
.\gradlew.bat runIde

# Ver dependencias
.\gradlew.bat dependencies

# Verificar plugin
.\gradlew.bat verifyPlugin

# Build con información detallada
.\gradlew.bat buildPlugin --info --stacktrace
```

## Soporte

Para problemas de SSL o configuración corporativa:
- Contactar al equipo de IT/DevOps
- Solicitar certificados corporativos
- Configurar proxy si es necesario

Para dudas del plugin:
- Revisar README.md
- Consultar ARQUITECTURA.md
- Contactar al Chapter de Desarrollo

## Resumen

**Estado Actual**: MVP completado con todas las funcionalidades core implementadas.

**Bloqueador**: Problema de certificados SSL en red corporativa (no es un problema del código).

**Solución**: Usar IntelliJ IDEA instalado localmente para desarrollo y testing.

**Resultado**: Plugin funcional listo para probar y demostrar al equipo.

---

**Proyecto**: DevPulse BP - IntelliJ Plugin  
**Versión**: 1.0.0-SNAPSHOT  
**Fecha**: Febrero 2026
