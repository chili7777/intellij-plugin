# Manual de Usuario - DevPulse BP

## Introducción

DevPulse BP es tu canal directo de comunicación técnica dentro de IntelliJ IDEA. Recibe píldoras informativas, lineamientos, documentación y alertas importantes sin salir de tu entorno de desarrollo.

## Interfaz Principal

### Centro de Mensajes (Tool Window)

El centro de mensajes es el corazón de DevPulse BP.

#### Acceder al Centro de Mensajes

1. **Opción 1**: Click en la pestaña **DevPulse** en el panel lateral derecho
2. **Opción 2**: Menú **View → Tool Windows → DevPulse**
3. **Atajo de teclado**: Configurar en **Settings → Keymap** (buscar "DevPulse")

#### Componentes del Centro de Mensajes

```
┌─────────────────────────────────────────────────────┐
│ DevPulse - Centro de Mensajes      [Actualizar]    │
├──────────────────┬──────────────────────────────────┤
│ Mensajes         │ Detalles del Mensaje             │
│                  │                                  │
│ ● ⚠️ Alerta 1    │ Título: Vulnerabilidad Crítica   │
│   📋 Lineamiento │                                  │
│   📚 Docs        │ Tipo: Alerta                     │
│   🎓 Píldora     │ Prioridad: Crítica               │
│                  │                                  │
│                  │ [Contenido del mensaje...]       │
│                  │                                  │
│                  │ Ejemplo de código:               │
│                  │ ┌──────────────────────┐        │
│                  │ │ código aquí          │        │
│                  │ └──────────────────────┘        │
│                  │                                  │
│                  │ [Ver documentación completa]     │
└──────────────────┴──────────────────────────────────┘
```

## Tipos de Mensajes

### ℹ️ Informativo
Información general sobre actualizaciones, cambios o noticias técnicas.

**Ejemplo:**
- Nuevas versiones de librerías disponibles
- Cambios en procesos de desarrollo
- Anuncios del Chapter

### 📋 Lineamiento
Nuevas políticas, estándares o mejores prácticas que deben seguirse.

**Ejemplo:**
- Uso obligatorio de Lombok
- Estándares de nomenclatura
- Políticas de seguridad

### 📚 Documentación
Enlaces a documentación técnica, guías o wikis.

**Ejemplo:**
- Nueva página en Confluence
- Guías de arquitectura
- Documentación de APIs

### 📢 Anuncio
Comunicados importantes del Chapter o liderazgo técnico.

**Ejemplo:**
- Cambios organizacionales
- Nuevas iniciativas
- Eventos técnicos

### ⚠️ Alerta
Avisos críticos que requieren atención inmediata.

**Ejemplo:**
- Vulnerabilidades de seguridad
- Problemas en producción
- Actualizaciones urgentes

### 🎓 Píldora Educativa
Contenido de capacitación con ejemplos prácticos de código.

**Ejemplo:**
- Patrones de diseño
- Mejores prácticas
- Tutoriales paso a paso

## Prioridades

Los mensajes tienen diferentes niveles de prioridad:

- **🔴 Crítica**: Requiere acción inmediata
- **🟠 Alta**: Importante, revisar pronto
- **🟡 Normal**: Información relevante
- **🟢 Baja**: Información general

## Trabajando con Mensajes

### Leer un Mensaje

1. Click en cualquier mensaje de la lista
2. El contenido completo se mostrará en el panel derecho
3. El mensaje se marcará automáticamente como leído (● desaparece)

### Mensajes No Leídos

Los mensajes no leídos se identifican por:
- **●** Punto azul al inicio
- **Texto en negrita**

### Acceder a Documentación

Si un mensaje incluye documentación adicional:
1. Scroll hasta el final del mensaje
2. Click en el botón **"Ver documentación completa"**
3. Se abrirá el enlace en tu navegador predeterminado

### Copiar Ejemplos de Código

Los mensajes de tipo Píldora Educativa incluyen ejemplos de código:
1. Seleccionar el código en el área de ejemplo
2. **Ctrl+C** (Windows/Linux) o **Cmd+C** (Mac) para copiar
3. Pegar directamente en tu editor

### Actualizar Mensajes

Para obtener nuevos mensajes del servidor:
1. Click en el botón **"Actualizar"** en la parte superior
2. O usar el atajo de teclado configurado
3. Aparecerá una notificación confirmando la actualización

## Notificaciones

### Tipos de Notificaciones

DevPulse BP muestra notificaciones en diferentes situaciones:

#### Al Iniciar IntelliJ IDEA
```
┌─────────────────────────────────────┐
│ ℹ️ DevPulse BP                      │
│ Tienes 3 mensaje(s) sin leer       │
│                                     │
│ [Cerrar]                            │
└─────────────────────────────────────┘
```

#### Mensaje Nuevo (Futuro - con backend)
```
┌─────────────────────────────────────┐
│ ⚠️ Alerta: Vulnerabilidad Crítica   │
│ Se ha detectado CVE-2021-44228...   │
│                                     │
│ [Ver más] [Marcar como leído]      │
└─────────────────────────────────────┘
```

### Interactuar con Notificaciones

- **Ver más**: Abre el enlace de documentación en el navegador
- **Marcar como leído**: Marca el mensaje como leído sin abrir detalles
- **Cerrar (X)**: Cierra la notificación sin marcar como leído

### Deshabilitar Notificaciones

Si prefieres no recibir notificaciones emergentes:

1. **File → Settings → Appearance & Behavior → Notifications**
2. Buscar **"DevPulse Notifications"**
3. Cambiar a **"No popup"** o **"Disabled"**

## Casos de Uso Comunes

### Caso 1: Revisar Mensajes Diarios

**Flujo recomendado:**
1. Al iniciar IntelliJ IDEA, verás notificación de mensajes sin leer
2. Abrir el Tool Window de DevPulse
3. Revisar mensajes de mayor a menor prioridad
4. Leer y marcar como leídos según relevancia

### Caso 2: Implementar un Lineamiento

**Ejemplo: Nuevo estándar de uso de Lombok**

1. Recibir notificación de nuevo lineamiento
2. Abrir el mensaje en DevPulse
3. Leer el contenido y requisitos
4. Copiar el ejemplo de código proporcionado
5. Click en "Ver documentación completa" para detalles
6. Implementar en tu proyecto actual

### Caso 3: Responder a una Alerta Crítica

**Ejemplo: Vulnerabilidad de seguridad**

1. Recibir notificación de alerta crítica (roja)
2. Leer inmediatamente el contenido
3. Seguir los pasos indicados en el mensaje
4. Acceder a la documentación completa si es necesario
5. Aplicar el fix recomendado

### Caso 4: Aprender con Píldoras Educativas

**Ejemplo: Manejo de excepciones en Spring**

1. Abrir mensaje de píldora educativa
2. Leer la explicación teórica
3. Revisar el ejemplo de código incluido
4. Copiar el código de ejemplo
5. Probar en un proyecto de prueba
6. Acceder a la documentación completa para más ejemplos

## Atajos de Teclado

Puedes configurar atajos personalizados en:
**File → Settings → Keymap → Plugins → DevPulse**

Acciones disponibles:
- **Abrir DevPulse Tool Window**
- **Actualizar Mensajes**
- **Marcar Todos como Leídos** (futuro)

## Telemetría y Privacidad

### Datos Recopilados

DevPulse BP recopila información anónima para métricas:
- Instalación del plugin y versión
- Mensajes leídos (ID del mensaje, no contenido)
- Email del desarrollador (opcional)
- Timestamp de eventos

### Deshabilitar Telemetría

La telemetría ayuda a mejorar el servicio, pero puedes deshabilitarla:

1. Editar archivo: `~/.devpulse/config.json`
2. Cambiar: `"enableTelemetry": false`
3. Reiniciar IntelliJ IDEA

## Consejos y Mejores Prácticas

### 📌 Revisa Mensajes Regularmente
- Dedica 5 minutos al inicio del día
- Prioriza alertas críticas y lineamientos

### 📌 Mantén el Plugin Actualizado
- Acepta actualizaciones cuando se notifiquen
- Las nuevas versiones incluyen mejoras y correcciones

### 📌 Configura Notificaciones
- Ajusta según tu flujo de trabajo
- Mantén habilitadas al menos las alertas críticas

### 📌 Comparte Feedback
- Reporta problemas al Chapter
- Sugiere mejoras o nuevas funcionalidades

### 📌 Usa los Ejemplos de Código
- Los ejemplos están probados y validados
- Adáptalos a tu contexto específico

## Preguntas Frecuentes

### ¿Puedo eliminar mensajes?
Actualmente no, pero se marcará como leído y bajará en la lista.

### ¿Los mensajes se sincronizan entre computadoras?
En el MVP no, están almacenados localmente. En futuras versiones sí.

### ¿Puedo buscar mensajes antiguos?
Actualmente no hay búsqueda. Usa scroll en la lista de mensajes.

### ¿Cómo sé si hay mensajes nuevos?
- Notificación al iniciar IntelliJ
- Badge en el Tool Window (futuro)
- Notificaciones en tiempo real (futuro con backend)

### ¿Puedo responder a un mensaje?
No directamente. Para feedback, contacta al Chapter.

### ¿Funciona offline?
Sí, los mensajes descargados están disponibles offline.

## Solución de Problemas

### No veo el Tool Window
**Solución:** View → Tool Windows → DevPulse

### Las notificaciones no aparecen
**Solución:** Verificar Settings → Notifications → DevPulse Notifications

### Los mensajes no se actualizan
**Solución:** Click en "Actualizar" o reiniciar IntelliJ IDEA

### El plugin está lento
**Solución:** 
- Verificar que tienes Java 17+
- Limpiar caché: File → Invalidate Caches / Restart

## Soporte

**Contacto:**
- Email: devpulse@pichincha.com
- Confluence: https://confluence.pichincha.com/devpulse
- Chapter de Desarrollo

**Horario de Soporte:**
- Lunes a Viernes: 9:00 - 18:00 (horario de oficina)

---

**¡Gracias por usar DevPulse BP!**

Mantente conectado con el Chapter y mejora tu productividad recibiendo información técnica relevante directamente en tu IDE.

---

**Versión del Manual**: 1.0  
**Última actualización**: Febrero 2026
