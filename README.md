# Sistema de Registro Digital de Campañas de Salud Comunitarias (AppInforme)

## 📋 Descripción
Sistema integral para el registro, monitoreo, análisis y predicción de campañas de salud comunitarias. Permite gestionar participantes, actividades, generar reportes interactivos y realizar predicciones de participación basadas en datos históricos.

## ✨ Características Principales

### 🎯 Requerimientos Funcionales Implementados

1. **📊 Registro de Participantes y Cobertura por Actividad**
   - Registro completo de participantes con datos demográficos
   - Gestión de actividades de campaña con control de capacidad
   - Seguimiento de cobertura y resultados en tiempo real
   - Reportes detallados de participación

2. **📈 Dashboard Interactivo con Indicadores Clave**
   - Visualización de métricas principales en tiempo real
   - Gráficos interactivos de participación por campaña
   - Indicadores de cobertura y efectividad
   - Análisis de tendencias históricas

3. **🔮 Predicciones de Participación Futura**
   - Algoritmo de predicción basado en datos históricos
   - Cálculo automático de nivel de confianza (0-100%)
   - Generación automática y manual de predicciones
   - Análisis de factores que influyen en la participación

### 🛠️ Funcionalidades Adicionales

- **👥 Gestión de Usuarios**: Control de acceso con roles (Administrador, Usuario)
- **🏥 Gestión de Campañas**: Creación y administración de campañas de salud
- **📅 Gestión de Actividades**: Programación y control de actividades
- **📊 Reportes Avanzados**: Generación de reportes personalizados
- **🔐 Seguridad**: Autenticación y autorización por roles

## 🔧 Requisitos Técnicos

- **Java**: JDK 8 o superior
- **Base de Datos**: SQL Server 2019 o superior
- **IDE**: NetBeans (recomendado) o cualquier IDE compatible con Java
- **Dependencias**: Driver JDBC de SQL Server (incluido en `lib/`)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/AppInforme.git
cd AppInforme
```

### 2. Configurar la Base de Datos
```sql
-- Crear base de datos
CREATE DATABASE CampanaSalud;

-- Ejecutar scripts en orden:
-- 1. database/Create Database.sql (estructura y datos iniciales)
-- 2. database/Insert Data.sql (datos de prueba)
```

### 3. Configurar Conexión
Editar `src/model/database/DatabaseConnection.java` con tus datos:
```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CampanaSalud";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

### 4. Compilar y Ejecutar
```bash
# Compilar
javac -cp "lib/*" -d build src/model/database/*.java
javac -cp "lib/*;build" -d build src/model/*.java src/model/dao/*.java src/controller/*.java src/view/*.java src/Main.java

# Ejecutar
java -cp "lib/*;build" Main
```

## 👤 Credenciales de Acceso

### Usuario Administrador
- **DNI**: zsnow
- **Contraseña**: 1


## 📁 Estructura del Proyecto

```
AppInforme/
├── src/
│   ├── Main.java                    # Clase principal
│   ├── controller/                  # Lógica de negocio
│   │   ├── ActividadController.java
│   │   ├── CampanaController.java
│   │   ├── ParticipanteController.java
│   │   ├── PrediccionController.java
│   │   ├── RegistroParticipacionController.java
│   │   └── UsuarioController.java
│   ├── model/                       # Modelos de datos
│   │   ├── database/
│   │   │   └── DatabaseConnection.java
│   │   ├── dao/                     # Acceso a datos
│   │   │   ├── ActividadDAO.java
│   │   │   ├── CampanaDAO.java
│   │   │   ├── ParticipanteDAO.java
│   │   │   ├── PrediccionDAO.java
│   │   │   ├── RegistroParticipacionDAO.java
│   │   │   └── UsuarioDAO.java
│   │   ├── Actividad.java
│   │   ├── Campana.java
│   │   ├── Participante.java
│   │   ├── Prediccion.java
│   │   ├── RegistroParticipacion.java
│   │   └── Usuario.java
│   └── view/                        # Interfaz de usuario
│       ├── ActividadFormView.java
│       ├── BaseForm.java
│       ├── CoberturaActividadView.java
│       ├── DashboardView.java
│       ├── LoginView.java
│       ├── MainView.java
│       ├── PrediccionFormView.java
│       └── RegistroParticipacionView.java
├── lib/                             # Dependencias
│   └── mssql-jdbc-12.8.1.jre8.jar
├── database/                        # Scripts de base de datos
│   ├── Create Database.sql
│   └── Insert Data.sql
├── build/                           # Archivos compilados (no subir a Git)
└── README.md
```

## 🎮 Guía de Uso

### 1. Inicio de Sesión
- Ejecutar la aplicación
- Ingresar DNI y contraseña
- Acceder al dashboard principal

### 2. Gestión de Actividades
- **Menú**: Actividades → Gestionar Actividades
- **Funciones**: Crear, editar, eliminar actividades
- **Registro**: Actividades → Registrar Participación

### 3. Visualización de Datos
- **Dashboard**: Inicio → Ver Dashboard
- **Cobertura**: Actividades → Ver Cobertura y Resultados

### 4. Predicciones
- **Ver**: Predicciones → Ver Predicciones
- **Generar**: Predicciones → Generar Predicción
- **Automático**: Usar algoritmo basado en datos históricos

## 🔮 Algoritmo de Predicciones

### Factores de Cálculo
- **Experiencia Histórica**: Número de campañas anteriores del responsable
- **Consistencia**: Variación en participación de campañas pasadas
- **Recencia**: Peso de datos más recientes
- **Factor de Crecimiento**: Proyección de mejora (10%)

### Nivel de Confianza
- **90-100%**: Predicción muy confiable
- **80-89%**: Predicción confiable
- **70-79%**: Predicción moderada
- **60-69%**: Predicción con incertidumbre
- **<60%**: Predicción poco confiable

## 🐛 Solución de Problemas

### Error de Conexión a Base de Datos
1. Verificar que SQL Server esté ejecutándose
2. Confirmar credenciales en `DatabaseConnection.java`
3. Verificar que la base de datos `CampanaSalud` exista

### Error de Compilación
1. Verificar que el JDK esté instalado correctamente
2. Confirmar que el driver JDBC esté en la carpeta `lib/`
3. Compilar en el orden correcto (database → model → controller → view)

## 📊 Tecnologías Utilizadas

- **Lenguaje**: Java 8+
- **GUI**: Java Swing
- **Base de Datos**: SQL Server 2019
- **Conectividad**: JDBC
- **Arquitectura**: MVC (Model-View-Controller)
- **Patrones**: DAO (Data Access Object)

## 🤝 Contribuir

1. Fork el proyecto
2. Crear rama para nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**Manuel Vera** - [✉️](mailto:mveraba01@ucvvirtual.edu.pe)

**Juan Vidal** - [✉️](mailto:jvidalmo02@ucvvirtual.edu.pe)

**Juan Moreno** - [✉️](mailto:jmorenoto01@ucvvirtual.edu.pe)

---

*Sistema desarrollado para la gestión integral de campañas de salud comunitarias con capacidades de análisis predictivo.* 