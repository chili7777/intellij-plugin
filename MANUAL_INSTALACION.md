# Manual de Instalación - DevPulse BP

## Requisitos Previos

- **IntelliJ IDEA**: Versión 2023.1 o superior (Community o Ultimate)
- **Java**: JDK 17 o superior
- **Gradle**: 8.x (incluido en el wrapper del proyecto)

## Opción 1: Instalación desde Código Fuente (Desarrollo)

### Paso 1: Preparar el Entorno

1. Verificar instalación de Java:
```bash
java -version
```
Debe mostrar Java 17 o superior.

2. Navegar al directorio del proyecto:
```bash
cd C:\Users\hanmilton.berrezueta\Desktop\devpulse-intellij-plugin
```

### Paso 2: Construir el Plugin

```bash
# Windows
gradlew.bat buildPlugin

# Linux/Mac
./gradlew buildPlugin
```

El proceso tomará algunos minutos la primera vez mientras descarga dependencias.

### Paso 3: Ejecutar en Modo Desarrollo

Para probar el plugin sin instalarlo:

```bash
# Windows
gradlew.bat runIde

# Linux/Mac
./gradlew runIde
```

Esto abrirá una nueva instancia de IntelliJ IDEA con el plugin pre-instalado.

## Opción 2: Instalación Manual del ZIP

### Paso 1: Generar el Archivo ZIP

```bash
# Windows
gradlew.bat buildPlugin

# Linux/Mac
./gradlew buildPlugin
```

El archivo ZIP se generará en:
```
build/distributions/devpulse-intellij-plugin-1.0.0-SNAPSHOT.zip
```

### Paso 2: Instalar en IntelliJ IDEA

1. Abrir IntelliJ IDEA
2. Ir a **File → Settings** (Windows/Linux) o **IntelliJ IDEA → Preferences** (Mac)
3. Seleccionar **Plugins** en el menú lateral
4. Click en el ícono de engranaje ⚙️ en la parte superior
5. Seleccionar **Install Plugin from Disk...**
6. Navegar y seleccionar el archivo ZIP generado
7. Click en **OK**
8. Reiniciar IntelliJ IDEA cuando se solicite

### Paso 3: Verificar Instalación

1. Después de reiniciar, ir a **File → Settings → Plugins**
2. Buscar "DevPulse BP" en la lista de plugins instalados
3. Debe aparecer con estado "Enabled"

## Opción 3: Instalación desde JetBrains Marketplace (Futuro)

Una vez publicado en el marketplace:

1. Abrir IntelliJ IDEA
2. Ir a **File → Settings → Plugins**
3. Seleccionar la pestaña **Marketplace**
4. Buscar "DevPulse BP"
5. Click en **Install**
6. Reiniciar IntelliJ IDEA

## Verificación de Funcionamiento

### 1. Verificar Tool Window

1. Buscar la pestaña **DevPulse** en el panel lateral derecho
2. Si no está visible, ir a **View → Tool Windows → DevPulse**
3. Debe mostrar el centro de mensajes con mensajes de ejemplo

### 2. Verificar Notificaciones

1. Cerrar y reabrir IntelliJ IDEA
2. Debe aparecer una notificación en la esquina inferior derecha indicando mensajes sin leer

### 3. Verificar Configuración

1. Ir a **File → Settings**
2. Buscar "DevPulse" en la barra de búsqueda
3. Debe aparecer la página de configuración del plugin (si está implementada)

## Configuración Inicial

### Configurar Email del Usuario

El plugin almacena configuración en: `~/.devpulse/`

Para configurar manualmente (hasta que se implemente UI de configuración):

1. Crear/editar archivo: `~/.devpulse/config.json`
```json
{
  "userEmail": "tu.email@pichincha.com",
  "apiUrl": "https://devpulse-api.pichincha.com",
  "enableNotifications": true,
  "enableTelemetry": true
}
```

## Solución de Problemas

### El plugin no aparece después de instalar

**Solución:**
1. Verificar que IntelliJ IDEA se reinició completamente
2. Ir a **File → Settings → Plugins** y verificar que está habilitado
3. Verificar compatibilidad de versión (debe ser 2023.1+)

### Error al construir el plugin

**Problema:** `Could not resolve dependencies`

**Solución:**
```bash
# Limpiar caché de Gradle
gradlew clean --refresh-dependencies
gradlew buildPlugin
```

### El Tool Window no aparece

**Solución:**
1. Ir a **View → Tool Windows**
2. Buscar "DevPulse" en la lista
3. Si no aparece, reinstalar el plugin

### Notificaciones no se muestran

**Solución:**
1. Verificar que las notificaciones están habilitadas en IntelliJ:
   - **File → Settings → Appearance & Behavior → Notifications**
   - Buscar "DevPulse Notifications"
   - Asegurar que está configurado como "Balloon"

### Error de compatibilidad de Java

**Problema:** `Unsupported class file major version`

**Solución:**
- Asegurar que está usando Java 17 o superior
- Configurar JAVA_HOME correctamente:
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Linux/Mac
export JAVA_HOME=/path/to/jdk-17
```

## Desinstalación

1. Ir a **File → Settings → Plugins**
2. Buscar "DevPulse BP"
3. Click en el menú desplegable al lado del plugin
4. Seleccionar **Uninstall**
5. Reiniciar IntelliJ IDEA

Para eliminar datos del plugin:
```bash
# Windows
rmdir /s %USERPROFILE%\.devpulse

# Linux/Mac
rm -rf ~/.devpulse
```

## Actualización

### Actualización Manual

1. Descargar la nueva versión del plugin (archivo ZIP)
2. Seguir los pasos de instalación manual
3. IntelliJ IDEA detectará y actualizará automáticamente

### Actualización Automática (Futuro)

Una vez en el Marketplace, las actualizaciones serán automáticas:
1. IntelliJ IDEA notificará cuando haya actualizaciones disponibles
2. Click en **Update** en la notificación
3. Reiniciar cuando se solicite

## Logs y Debugging

### Ver Logs del Plugin

1. **Help → Show Log in Explorer** (Windows) / **Show Log in Finder** (Mac)
2. Buscar entradas con `DevPulse` en el archivo `idea.log`

### Habilitar Debug Logging

1. **Help → Diagnostic Tools → Debug Log Settings**
2. Agregar: `com.pichincha.devpulse`
3. Click en **OK**
4. Los logs detallados aparecerán en `idea.log`

## Soporte

Para problemas o preguntas:
- Contactar al Chapter de Desarrollo
- Email: devpulse@pichincha.com
- Confluence: https://confluence.pichincha.com/devpulse

## Próximos Pasos

Después de la instalación:
1. Explorar el centro de mensajes
2. Leer los mensajes de ejemplo
3. Familiarizarse con los diferentes tipos de mensajes
4. Configurar preferencias de notificación

---

**Versión del Manual**: 1.0  
**Última actualización**: Febrero 2026
