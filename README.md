# DevPulse BP - IntelliJ IDEA Plugin

Plugin de comunicación técnica para desarrolladores de Banco Pichincha.

## 🎯 Objetivo

DevPulse BP es un canal de comunicación directa entre el Chapter y los desarrolladores, permitiendo distribuir:

- 📋 Píldoras informativas
- 📢 Anuncios técnicos
- 📚 Lineamientos de desarrollo
- 📖 Documentación
- ⚠️ Alertas y avisos críticos

## ✨ Características del MVP

### Implementado
- ✅ Sistema de notificaciones push
- ✅ Centro de mensajes (Tool Window)
- ✅ Múltiples tipos de mensajes (Informativo, Lineamiento, Documentación, Anuncio, Alerta, Píldora Educativa)
- ✅ Gestión de prioridades (Baja, Normal, Alta, Crítica)
- ✅ Marcado de mensajes como leídos
- ✅ Telemetría básica (instalación, lectura de mensajes)
- ✅ Almacenamiento local de mensajes
- ✅ Ejemplos de código en mensajes
- ✅ Enlaces a documentación externa

### Por Implementar (Fase 2)
- ⏳ Integración con API backend
- ⏳ WebSockets para tiempo real
- ⏳ Programación de envíos
- ⏳ Panel de administración
- ⏳ Métricas de alcance
- ⏳ Gestión de versiones del plugin

## 🚀 Instalación

### Requisitos
- IntelliJ IDEA 2023.1 o superior (Community o Ultimate)
- Java 17 o superior
- Gradle 8.x

### Desarrollo Local

1. **Clonar el proyecto**
```bash
cd C:\Users\hanmilton.berrezueta\Desktop\devpulse-intellij-plugin
```

2. **Construir el plugin**
```bash
gradle buildPlugin
```

3. **Ejecutar en modo desarrollo**
```bash
gradle runIde
```

Esto abrirá una nueva instancia de IntelliJ IDEA con el plugin instalado.

### Instalación Manual

1. Construir el archivo ZIP del plugin:
```bash
gradle buildPlugin
```

2. El archivo ZIP se generará en: `build/distributions/devpulse-intellij-plugin-1.0.0-SNAPSHOT.zip`

3. En IntelliJ IDEA:
   - Ve a `File > Settings > Plugins`
   - Click en el ícono de engranaje ⚙️
   - Selecciona `Install Plugin from Disk...`
   - Selecciona el archivo ZIP generado
   - Reinicia IntelliJ IDEA

## 📖 Uso

### Acceder al Centro de Mensajes

1. Abre la ventana lateral derecha
2. Busca la pestaña "DevPulse"
3. Verás la lista de mensajes disponibles

### Recibir Notificaciones

Las notificaciones aparecerán automáticamente en la esquina inferior derecha cuando:
- Se inicie IntelliJ IDEA con mensajes sin leer
- Lleguen nuevos mensajes (en futuras versiones con backend)

### Tipos de Mensajes

- **ℹ️ Informativo**: Información general
- **📋 Lineamiento**: Nuevas políticas o estándares de desarrollo
- **📚 Documentación**: Enlaces a documentación técnica
- **📢 Anuncio**: Comunicados importantes
- **⚠️ Alerta**: Avisos críticos (vulnerabilidades, etc.)
- **🎓 Píldora Educativa**: Contenido de capacitación con ejemplos de código

## 🏗️ Arquitectura

```
devpulse-intellij-plugin/
├── src/main/java/com/pichincha/devpulse/
│   ├── model/              # Modelos de datos
│   │   ├── Message.java
│   │   ├── MessageType.java
│   │   └── Priority.java
│   ├── service/            # Servicios de negocio
│   │   ├── MessageService.java
│   │   ├── NotificationService.java
│   │   └── TelemetryService.java
│   ├── ui/                 # Componentes de interfaz
│   │   ├── DevPulseToolWindow.java
│   │   └── DevPulseToolWindowFactory.java
│   ├── settings/           # Configuración del plugin
│   │   └── DevPulseSettings.java
│   ├── listeners/          # Event listeners
│   │   └── DevPulseStartupListener.java
│   ├── actions/            # Acciones del usuario
│   │   └── RefreshMessagesAction.java
│   └── util/               # Utilidades
│       └── LocalDateTimeAdapter.java
└── src/main/resources/
    ├── META-INF/
    │   ├── plugin.xml      # Configuración del plugin
    │   └── pluginIcon.svg
    └── icons/
        └── toolWindowIcon.svg
```

## 🔧 Configuración

Los ajustes del plugin se almacenan en: `~/.devpulse/`

### Configuración Disponible
- `apiUrl`: URL del backend (default: https://devpulse-api.pichincha.com)
- `userEmail`: Email del desarrollador
- `enableNotifications`: Habilitar/deshabilitar notificaciones
- `enableTelemetry`: Habilitar/deshabilitar telemetría

## 📊 Telemetría

El plugin recopila información anónima para métricas:
- Instalación del plugin
- Versión del plugin
- Mensajes leídos
- Email del desarrollador (si se configura)

Puedes deshabilitar la telemetría en la configuración.

## 🧪 Testing

```bash
# Ejecutar tests
gradle test

# Verificar el plugin
gradle verifyPlugin
```

## 📦 Publicación

### Preparar para Publicación

1. Actualizar versión en `build.gradle`
2. Actualizar `change-notes` en `plugin.xml`
3. Construir el plugin:
```bash
gradle buildPlugin
```

### Publicar en JetBrains Marketplace

```bash
gradle publishPlugin
```

Nota: Requiere configurar `PUBLISH_TOKEN` en variables de entorno.

## 🛠️ Desarrollo

### Comandos Útiles

```bash
# Limpiar build
gradle clean

# Construir plugin
gradle buildPlugin

# Ejecutar en IDE de desarrollo
gradle runIde

# Verificar compatibilidad
gradle verifyPlugin

# Ver logs del plugin
gradle runIde --info
```

### Estructura de Datos

#### Message
```json
{
  "id": "uuid",
  "title": "Título del mensaje",
  "content": "Contenido detallado",
  "type": "ALERT",
  "priority": "HIGH",
  "createdAt": "2026-02-06T10:00:00",
  "link": "https://confluence.pichincha.com/...",
  "codeExample": "código de ejemplo",
  "read": false
}
```

## 📝 Próximos Pasos (Roadmap)

### Fase 2 - Backend Integration
- [ ] API REST para gestión de mensajes
- [ ] WebSocket para notificaciones en tiempo real
- [ ] Autenticación y autorización
- [ ] Base de datos PostgreSQL

### Fase 3 - Advanced Features
- [ ] Panel de administración web
- [ ] Programación de envíos
- [ ] Segmentación de audiencia
- [ ] Métricas y reportes
- [ ] Gestión de versiones del plugin

### Fase 4 - VSCode Extension
- [ ] Port del plugin a VSCode
- [ ] Unificación de backend

## 👥 Contribución

Este proyecto es desarrollado por el Chapter de Banco Pichincha.

## 📄 Licencia

Uso interno - Banco Pichincha

## 📞 Soporte

Para soporte o preguntas, contactar al Chapter de Desarrollo.

---

**Versión**: 1.0.0-SNAPSHOT  
**Última actualización**: Febrero 2026
