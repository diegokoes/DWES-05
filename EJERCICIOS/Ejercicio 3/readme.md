
# EJERCICIO 3: creación de logs por consola y en archivos

Primero vamos a crear un nuevo proyecto llamado **GestionExcepciones** donde haremos un RestController muy sencillo que nos permitirá usar excepciones propias y además crear trazas cuando se produzca dicha excepción.

Vamos a añadir trazas a logs por consola y en ficheros en el servidor usando SLF4J y Logback **(revisar la teoría en el aula virtual de la UT05, "Logging en Java".**

Los logs se imprimirán en la consola y se guardarán en un archivo en la carpeta logs en el directorio donde se ejecuta la aplicación.

### Configurar logs desde application.properties (opción básica)

Si no necesitas configuraciones avanzadas como las anteriores, puedes configurar logs básicos desde el archivo application.properties.

Por ejemplo, puedes habilitar logs en un archivo simple con:

```
# Habilitar logs en un archivo
logging.file.name=logs/application.log

# Tamaño máximo del archivo antes de rotar
# Cuando el archivo de log application.log alcance los 10MB, se renombrará (generalmente con un sufijo, como application-1.log) y se iniciará un nuevo archivo.
logging.logback.rollingpolicy.max-file-size=10MB

# Número máximo de archivos de respaldo
# Si el tamaño combinado de todos los archivos de respaldo supera 100MB, los archivos más antiguos se eliminarán automáticamente para liberar espacio.
logging.logback.rollingpolicy.total-size-cap=100MB

# Nivel de logs global
# El nivel INFO significa que se registrarán mensajes de nivel INFO o superior (INFO, WARN, ERROR, FATAL).
# Los mensajes de niveles más bajos, como DEBUG o TRACE, no se mostrarán a menos que se configure específicamente para ciertos paquetes o clases.
logging.level.root=INFO

# Configurar niveles para paquetes específicos
logging.level.org.springframework=DEBUG

# El nivel TRACE es el más detallado y normalmente incluye información muy granular, como la entrada y salida de métodos.
logging.level.es.daw.excepciones.gestionexcepciones.controller=TRACE
```

### Configuración básica en logback-spring.xml

Necesitas personalizar un archivo de configuración de Logback. 

Crea un archivo llamado **logback-spring.xml en el directorio src/main/resources.**

Aquí tienes un ejemplo básico de configuración:

```
<configuration>
    <!-- Appender para escribir en la consola -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Appender para escribir en un archivo -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- Ruta del archivo donde se guardarán los logs -->
        <file>logs/application.log</file>

        <!-- Configuración de la política de rotación -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- Archivo con la fecha para cada día -->
            <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- Mantener solo los últimos 30 días de logs -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>

        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Configurar el nivel de logs -->
    <root level="INFO">
        <!-- Enviar logs a la consola -->
        <appender-ref ref="CONSOLE" />
        <!-- Enviar logs al archivo -->
        <appender-ref ref="FILE" />
    </root>
</configuration>

```

- **Appender para consola (CONSOLE):** Sigue enviando logs a la consola para facilitar el desarrollo.
- **Appender para archivo (FILE):**
    - Los logs se escribirán en un archivo llamado logs/application.log.
    - Los archivos de log se rotarán diariamente (cada día se crea un nuevo archivo con la fecha en el nombre, por ejemplo, application.2025-01-06.log).
    - Solo se conservarán los últimos 30 días de logs gracias a la propiedad <maxHistory>30</maxHistory>.
 
### Configuración avanzada: logs separados por niveles

Si deseas que los logs se guarden en diferentes archivos según el nivel (INFO, ERROR, etc.), puedes usar configuraciones más avanzadas:

```
<configuration>
    <!-- Appender para logs de INFO y superior -->
    <appender name="INFO_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/info.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- Appender para logs de ERROR -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/error.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- Nivel de logs global -->
    <root level="INFO">
        <appender-ref ref="INFO_FILE" />
        <appender-ref ref="ERROR_FILE" />
    </root>
</configuration>
```

- Los logs de nivel INFO, WARN, y DEBUG se guardarán en logs/info.log.
- Los logs de nivel ERROR se guardarán en logs/error.log.
- Los logs rotarán diariamente y se conservarán solo los últimos 30 días.

### También ese pueden crear logs para diferentes paquetes

```
<configuration>
    <!-- Definir el formato de los logs -->
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>

    <!-- Appender para los logs generales -->
    <appender name="GENERAL_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/general.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/general-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory> <!-- Guardar logs de los últimos 30 días -->
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Appender para el paquete "es.daw.excepciones" -->
    <appender name="EXCEPTIONS_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/exceptions.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/exceptions-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Appender para el paquete "org.springframework" -->
    <appender name="SPRING_LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/spring.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/spring-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Logger para el paquete "es.daw.excepciones" -->
    <logger name="es.daw.excepciones" level="DEBUG" additivity="false">
        <appender-ref ref="EXCEPTIONS_LOG" />
    </logger>

    <!-- Logger para el paquete "org.springframework" -->
    <logger name="org.springframework" level="INFO" additivity="false">
        <appender-ref ref="SPRING_LOG" />
    </logger>

    <!-- Logger global para el resto de la aplicación -->
    <root level="INFO">
        <appender-ref ref="GENERAL_LOG" />
    </root>
</configuration>
```
