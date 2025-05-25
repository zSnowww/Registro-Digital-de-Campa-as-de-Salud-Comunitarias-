# Registro Digital de Campañas de Salud Comunitarias

Aplicación para el registro, monitoreo y análisis de campañas de salud comunitarias en tiempo real.

## Descripción

Esta plataforma web inteligente permite registrar, monitorear y analizar las campañas de salud comunitarias en tiempo real. El sistema optimiza el registro de participantes, cobertura y resultados por actividad, y permite generar predicciones sobre la participación esperada en futuras campañas.

La solución está dirigida a gestores sanitarios, autoridades locales y profesionales de la salud, quienes podrán tomar decisiones más acertadas a partir de datos estructurados, exportables y visualizados eficientemente.

## Características

- Registro digital de participantes, cobertura y resultados por actividad
- Visualización de indicadores clave a través de un dashboard interactivo
- Autenticación segura basada en DNI
- Generación de predicciones de participación futura en campañas
- Exportación de datos en formatos estándar
- Accesibilidad desde dispositivos móviles y escritorio

## Arquitectura

El proyecto está estructurado siguiendo una arquitectura de 4 capas:

1. **Capa de Presentación (Vista)**: Interfaces de usuario desarrolladas con Java Swing.
2. **Capa de Lógica de Negocio (Controlador)**: Implementa la lógica de la aplicación y conecta la vista con el modelo.
3. **Capa de Acceso a Datos (Modelo)**: Clases de entidad y DAO (Data Access Objects) para acceder a la base de datos.
4. **Capa de Base de Datos**: SQL Server como motor de base de datos.

## Requisitos del Sistema

- Java JDK 8 o superior
- SQL Server 2019 o superior
- Microsoft JDBC Driver para SQL Server
- NetBeans IDE 8.2 o superior (recomendado para desarrollo)

## Instalación

1. Clone el repositorio:
```
git clone https://github.com/tuusuario/AppInforme.git
```

2. Cree la base de datos ejecutando el script SQL ubicado en `database/create_database.sql`.

3. Configure la conexión a la base de datos en el archivo `src/config/DatabaseConfig.properties`.

4. Compile y ejecute el proyecto desde NetBeans o mediante línea de comandos:
```
javac -d build/classes -cp lib/*:src src/view/LoginView.java
java -cp build/classes:lib/* view.LoginView
```

## Uso

1. Inicie la aplicación y acceda con sus credenciales (DNI y contraseña).
2. Desde el menú principal puede acceder a las diferentes secciones:
   - Gestión de Campañas
   - Gestión de Actividades
   - Registro de Participantes
   - Generación de Predicciones
   - Reportes y Estadísticas

## Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes, por favor abra primero un issue para discutir lo que le gustaría cambiar.

## Licencia

[MIT](https://choosealicense.com/licenses/mit/)

## Contacto

Proyecto desarrollado para el curso de Emprendimiento Tecnológico. 