# Solución a Problemas de SSL en Entorno Corporativo

## Problema

Al intentar construir el plugin de IntelliJ IDEA, se presenta el siguiente error:

```
PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: 
unable to find valid certification path to requested target
```

Este error es común en entornos corporativos con:
- Proxies HTTPS con inspección SSL
- Certificados corporativos autofirmados
- Firewalls que interceptan tráfico SSL

## Soluciones

### Opción 1: Importar Certificado Corporativo (Recomendado)

1. **Obtener el certificado corporativo**:
   - Contactar al equipo de IT/Seguridad
   - O exportarlo desde el navegador:
     - Chrome: Settings → Privacy and security → Security → Manage certificates
     - Exportar como `.cer` o `.crt`

2. **Importar al Java KeyStore**:
```bash
# Ubicar el Java usado por Gradle
java -version

# Importar certificado (reemplazar rutas)
keytool -import -alias pichincha-cert -file C:\path\to\cert.cer -keystore "%JAVA_HOME%\lib\security\cacerts" -storepass changeit
```

3. **Verificar importación**:
```bash
keytool -list -keystore "%JAVA_HOME%\lib\security\cacerts" -storepass changeit | findstr pichincha
```

### Opción 2: Configurar Proxy en Gradle

Si tu organización usa un proxy corporativo:

1. **Crear/editar** `gradle.properties`:
```properties
systemProp.http.proxyHost=proxy.pichincha.com
systemProp.http.proxyPort=8080
systemProp.http.nonProxyHosts=localhost|127.0.0.1
systemProp.https.proxyHost=proxy.pichincha.com
systemProp.https.proxyPort=8080
systemProp.https.nonProxyHosts=localhost|127.0.0.1

# Si requiere autenticación
systemProp.http.proxyUser=tu_usuario
systemProp.http.proxyPassword=tu_password
systemProp.https.proxyUser=tu_usuario
systemProp.https.proxyPassword=tu_password
```

### Opción 3: Descargar IntelliJ SDK Manualmente

Si las opciones anteriores no funcionan:

1. **Descargar IntelliJ IDEA Community**:
   - Ir a: https://www.jetbrains.com/idea/download/
   - Descargar versión 2023.1.5 Community Edition
   - Instalar en tu máquina

2. **Configurar Gradle para usar instalación local**:

Editar `build.gradle`:
```groovy
intellij {
  localPath = 'C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2023.1.5'
  type = 'IC'
  plugins = []
}
```

3. **Construir el plugin**:
```bash
.\gradlew.bat buildPlugin
```

### Opción 4: Usar Repositorio Maven Local

1. **Configurar Maven local** en `build.gradle`:
```groovy
repositories {
  mavenLocal()
  mavenCentral()
  maven { 
    url 'https://www.jetbrains.com/intellij-repository/releases'
  }
}
```

2. **Descargar dependencias manualmente** y colocarlas en `.m2/repository`

### Opción 5: Construcción Offline (Temporal)

Para desarrollo inicial sin acceso a internet:

1. **En otra máquina con acceso** (casa, laptop personal):
```bash
.\gradlew.bat buildPlugin
```

2. **Copiar caché de Gradle**:
   - Ubicación: `C:\Users\<usuario>\.gradle\caches`
   - Copiar a la máquina corporativa

3. **Construir offline**:
```bash
.\gradlew.bat buildPlugin --offline
```

## Configuración Actual Aplicada

Ya he aplicado las siguientes configuraciones en tu proyecto:

### `gradle.properties`
```properties
org.gradle.jvmargs=-Xmx2048m -Djavax.net.ssl.trustStoreType=Windows-ROOT
systemProp.javax.net.ssl.trustStoreType=Windows-ROOT
```

### `build.gradle`
```groovy
repositories {
  mavenCentral()
  maven { 
    url 'https://www.jetbrains.com/intellij-repository/releases'
  }
  maven { 
    url 'https://www.jetbrains.com/intellij-repository/snapshots'
  }
}

intellij {
  version = '2023.1.5'
  type = 'IC'
  downloadSources = false  // Reduce descargas SSL
}
```

## Recomendación para Banco Pichincha

### Solución Corporativa Permanente

1. **Configurar Nexus/Artifactory interno**:
   - Proxy de repositorios Maven/Gradle
   - Cache de dependencias
   - Elimina dependencia de internet

2. **Configurar en `build.gradle`**:
```groovy
repositories {
  maven {
    url 'http://nexus.pichincha.local/repository/maven-public/'
    allowInsecureProtocol = true  // Si es HTTP interno
  }
}
```

3. **Ventajas**:
   - Builds más rápidos
   - Sin problemas de SSL
   - Control de versiones
   - Seguridad mejorada

## Próximos Pasos

1. **Contactar IT/DevOps** para:
   - Obtener certificado corporativo
   - Configuración de proxy
   - Acceso a repositorio interno (si existe)

2. **Mientras tanto**, usar **Opción 3** (IntelliJ local):
   - Descargar e instalar IntelliJ IDEA 2023.1.5
   - Configurar `localPath` en `build.gradle`
   - Continuar desarrollo

3. **Para el equipo**, documentar:
   - Configuración de proxy
   - Certificados necesarios
   - Repositorios internos disponibles

## Comandos Útiles

```bash
# Limpiar caché de Gradle
.\gradlew.bat clean --refresh-dependencies

# Ver dependencias
.\gradlew.bat dependencies

# Build con más información
.\gradlew.bat buildPlugin --info --stacktrace

# Verificar Java
java -version
echo %JAVA_HOME%

# Listar certificados Java
keytool -list -keystore "%JAVA_HOME%\lib\security\cacerts" -storepass changeit
```

## Contacto para Soporte

- **IT/Seguridad**: Para certificados y proxy
- **DevOps**: Para repositorios internos
- **Chapter Lead**: Para estándares de desarrollo

---

**Nota**: La configuración SSL debe ser manejada por el equipo de IT para garantizar la seguridad corporativa.
