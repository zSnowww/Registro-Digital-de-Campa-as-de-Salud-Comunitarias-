# Sistema de Registro Digital de Campañas de Salud Comunitarias

## Descripción
Sistema para el registro, monitoreo y análisis de campañas de salud comunitarias en tiempo real.

## Requisitos Previos
- Java JDK 8 o superior
- SQL Server 2019 o superior
- NetBeans IDE (recomendado)

## Configuración del Proyecto

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/nombre-del-repo.git
```

2. Configurar la base de datos:
   - Crear una base de datos SQL Server llamada `CampanaSalud`
   - Ejecutar el script `database/create_database.sql` para crear las tablas y datos iniciales

3. Configurar la conexión a la base de datos:
   - Copiar `src/resources/database.properties.example` a `src/resources/database.properties`
   - Editar `database.properties` con tus datos de conexión:
     - db.url: URL de tu servidor SQL Server
     - db.user: Tu usuario de SQL Server
     - db.password: Tu contraseña de SQL Server

4. Agregar dependencias:
   - Descargar el driver JDBC de SQL Server desde [Microsoft](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server)
   - Colocar el archivo .jar en la carpeta `lib` del proyecto
   - Agregar el .jar al classpath del proyecto en NetBeans

5. Compilar y ejecutar:
   - Abrir el proyecto en NetBeans
   - Ejecutar la clase `Main.java`

## Credenciales Iniciales
- DNI: 12345678
- Contraseña: admin123

## Estructura del Proyecto
```
src/
├── controller/    # Controladores de la aplicación
├── model/        # Modelos y DAOs
├── view/         # Vistas de la aplicación
└── resources/    # Recursos (configuración, etc.)
```

## Contribuir
1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia
Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles. 