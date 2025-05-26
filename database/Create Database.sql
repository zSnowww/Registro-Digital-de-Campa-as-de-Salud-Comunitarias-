-- Eliminar la base de datos si existe
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'CampanaSalud')
BEGIN
    ALTER DATABASE CampanaSalud SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE CampanaSalud;
END
GO

-- Crear la base de datos
CREATE DATABASE [CampanaSalud]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'CampanaSalud', FILENAME = N'/var/opt/mssql/data/CampanaSalud.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'CampanaSalud_log', FILENAME = N'/var/opt/mssql/data/CampanaSalud_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO

USE [CampanaSalud]
GO

-- Crear tabla Usuario
CREATE TABLE [dbo].[Usuario](
    [idUsuario] [int] IDENTITY(1,1) NOT NULL,
    [nombre] [varchar](100) NOT NULL,
    [apellido] [varchar](100) NOT NULL,
    [correo] [varchar](100) NOT NULL,
    [dni] [varchar](20) NOT NULL,
    [rol] [varchar](50) NOT NULL,
    [contrasena] [varchar](100) NOT NULL,
    [fechaCreacion] [datetime] NULL DEFAULT GETDATE(),
    [ultimoAcceso] [datetime] NULL,
    [activo] [bit] NULL DEFAULT 1,
    PRIMARY KEY CLUSTERED ([idUsuario] ASC),
    UNIQUE NONCLUSTERED ([dni] ASC)
)
GO

-- Crear tabla TipoActividad
CREATE TABLE TipoActividad (
    idTipoActividad INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);
GO

-- Crear tabla Campana
CREATE TABLE [dbo].[Campana](
    [idCampana] [int] IDENTITY(1,1) NOT NULL,
    [nombre] [varchar](200) NOT NULL,
    [descripcion] [text] NULL,
    [fechaInicio] [date] NOT NULL,
    [fechaFin] [date] NOT NULL,
    [idResponsable] [int] NOT NULL,
    [estado] [varchar](20) NULL DEFAULT 'Planificada',
    [metaParticipantes] [int] NULL,
    [participantesRegistrados] [int] NULL DEFAULT 0,
    [fechaCreacion] [datetime] NULL DEFAULT GETDATE(),
    PRIMARY KEY CLUSTERED ([idCampana] ASC),
    FOREIGN KEY ([idResponsable]) REFERENCES [dbo].[Usuario] ([idUsuario])
)
GO

-- Crear tabla Actividad
CREATE TABLE [dbo].[Actividad](
    [idActividad] [int] IDENTITY(1,1) NOT NULL,
    [nombre] [varchar](200) NOT NULL,
    [descripcion] [text] NULL,
    [fecha] [date] NOT NULL,
    [hora] [varchar](20) NOT NULL,
    [idCampana] [int] NOT NULL,
    [capacidad] [int] NULL,
    [participantesRegistrados] [int] NULL DEFAULT 0,
    [estado] [varchar](20) NULL DEFAULT 'Planificada',
    PRIMARY KEY CLUSTERED ([idActividad] ASC),
    FOREIGN KEY ([idCampana]) REFERENCES [dbo].[Campana] ([idCampana])
)
GO

-- Crear tabla Participante
CREATE TABLE [dbo].[Participante](
    [idParticipante] [int] IDENTITY(1,1) NOT NULL,
    [nombre] [varchar](100) NOT NULL,
    [apellido] [varchar](100) NOT NULL,
    [dni] [varchar](20) NOT NULL,
    [edad] [int] NOT NULL,
    [sexo] [varchar](10) NOT NULL,
    [direccion] [varchar](200) NULL,
    [telefono] [varchar](20) NULL,
    [correo] [varchar](100) NULL,
    [fechaRegistro] [datetime] NULL DEFAULT GETDATE(),
    [activo] [bit] NULL DEFAULT 1,
    PRIMARY KEY CLUSTERED ([idParticipante] ASC),
    UNIQUE NONCLUSTERED ([dni] ASC)
)
GO

-- Crear tabla RegistroParticipacion
CREATE TABLE [dbo].[RegistroParticipacion](
    [idRegistro] [int] IDENTITY(1,1) NOT NULL,
    [idParticipante] [int] NOT NULL,
    [idActividad] [int] NOT NULL,
    [resultado] [varchar](100) NULL,
    [observaciones] [text] NULL,
    [fechaRegistro] [datetime] NULL DEFAULT GETDATE(),
    PRIMARY KEY CLUSTERED ([idRegistro] ASC),
    CONSTRAINT [UC_Participacion] UNIQUE NONCLUSTERED ([idParticipante] ASC, [idActividad] ASC),
    FOREIGN KEY ([idParticipante]) REFERENCES [dbo].[Participante] ([idParticipante]),
    FOREIGN KEY ([idActividad]) REFERENCES [dbo].[Actividad] ([idActividad])
)
GO

-- Crear tabla Prediccion
CREATE TABLE [dbo].[Prediccion](
    [idPrediccion] [int] IDENTITY(1,1) NOT NULL,
    [idCampana] [int] NOT NULL,
    [fechaPrediccion] [date] NOT NULL,
    [participacionEstimada] [int] NOT NULL,
    [nivelConfianza] [decimal](5, 2) NULL,
    [notas] [text] NULL,
    [fechaCreacion] [datetime] NULL DEFAULT GETDATE(),
    PRIMARY KEY CLUSTERED ([idPrediccion] ASC),
    CONSTRAINT [UC_Prediccion_Campana] UNIQUE NONCLUSTERED ([idCampana] ASC),
    FOREIGN KEY ([idCampana]) REFERENCES [dbo].[Campana] ([idCampana])
)
GO

-- Crear tabla LogActividad
CREATE TABLE [dbo].[LogActividad](
    [idLog] [int] IDENTITY(1,1) NOT NULL,
    [tabla] [varchar](50) NOT NULL,
    [accion] [varchar](20) NOT NULL,
    [idRegistro] [int] NOT NULL,
    [fechaAccion] [datetime] NULL DEFAULT GETDATE(),
    [usuario] [varchar](100) NULL,
    [detalles] [text] NULL,
    PRIMARY KEY CLUSTERED ([idLog] ASC)
)
GO

-- Crear índices
CREATE NONCLUSTERED INDEX [idx_Actividad_Campana] ON [dbo].[Actividad] ([idCampana] ASC)
GO
CREATE NONCLUSTERED INDEX [idx_Campana_Fechas] ON [dbo].[Campana] ([fechaInicio] ASC, [fechaFin] ASC)
GO
CREATE NONCLUSTERED INDEX [idx_Prediccion_Campana] ON [dbo].[Prediccion] ([idCampana] ASC)
GO
CREATE NONCLUSTERED INDEX [idx_RegistroParticipacion_Actividad] ON [dbo].[RegistroParticipacion] ([idActividad] ASC)
GO
CREATE NONCLUSTERED INDEX [idx_RegistroParticipacion_Participante] ON [dbo].[RegistroParticipacion] ([idParticipante] ASC)
GO

-- Crear vistas
CREATE VIEW [dbo].[vw_ParticipacionPorActividad] AS
SELECT 
    a.idActividad,
    a.nombre AS nombreActividad,
    c.nombre AS nombreCampana,
    COUNT(rp.idParticipante) AS cantidadParticipantes
FROM 
    Actividad a
    INNER JOIN Campana c ON a.idCampana = c.idCampana
    LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
GROUP BY 
    a.idActividad, a.nombre, c.nombre
GO

CREATE VIEW [dbo].[vw_ParticipacionPorCampana] AS
SELECT 
    c.idCampana,
    c.nombre AS nombreCampana,
    COUNT(DISTINCT rp.idParticipante) AS cantidadParticipantes,
    COUNT(DISTINCT a.idActividad) AS cantidadActividades,
    u.nombre + ' ' + u.apellido AS responsable
FROM 
    Campana c
    INNER JOIN Usuario u ON c.idResponsable = u.idUsuario
    LEFT JOIN Actividad a ON c.idCampana = a.idCampana
    LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
GROUP BY 
    c.idCampana, c.nombre, u.nombre, u.apellido
GO

CREATE VIEW [dbo].[vw_PrediccionVsRealidad] AS
SELECT 
    c.idCampana,
    c.nombre AS nombreCampana,
    p.participacionEstimada,
    COUNT(DISTINCT rp.idParticipante) AS participacionReal,
    (COUNT(DISTINCT rp.idParticipante) - p.participacionEstimada) AS diferencia
FROM 
    Campana c
    INNER JOIN Prediccion p ON c.idCampana = p.idCampana
    LEFT JOIN Actividad a ON c.idCampana = a.idCampana
    LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
GROUP BY 
    c.idCampana, c.nombre, p.participacionEstimada
GO

-- Crear procedimientos almacenados
CREATE PROCEDURE [dbo].[sp_RegistrarParticipacion]
    @idParticipante INT,
    @idActividad INT,
    @resultado VARCHAR(100) = NULL,
    @observaciones TEXT = NULL
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- Verificar si la actividad existe y tiene capacidad
        DECLARE @capacidad INT, @registrados INT;
        SELECT @capacidad = capacidad, @registrados = participantesRegistrados
        FROM Actividad
        WHERE idActividad = @idActividad;
        
        IF @registrados >= @capacidad
        BEGIN
            RAISERROR('La actividad ha alcanzado su capacidad máxima', 16, 1);
            RETURN;
        END
        
        -- Insertar registro de participación
        INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones)
        VALUES (@idParticipante, @idActividad, @resultado, @observaciones);
        
        -- Actualizar contador de participantes
        UPDATE Actividad
        SET participantesRegistrados = participantesRegistrados + 1
        WHERE idActividad = @idActividad;
        
        -- Actualizar contador de campaña
        UPDATE Campana
        SET participantesRegistrados = participantesRegistrados + 1
        WHERE idCampana = (SELECT idCampana FROM Actividad WHERE idActividad = @idActividad);
        
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE PROCEDURE [dbo].[sp_GenerarPrediccion]
    @idCampana INT
AS
BEGIN
    DECLARE @participacionEstimada INT;
    DECLARE @nivelConfianza DECIMAL(5,2);
    
    -- Calcular predicción basada en datos históricos
    SELECT @participacionEstimada = AVG(participantesRegistrados) * 1.1
    FROM Campana
    WHERE estado = 'Completada'
    AND fechaInicio >= DATEADD(MONTH, -6, GETDATE());
    
    -- Calcular nivel de confianza basado en la variabilidad de datos históricos
    SELECT @nivelConfianza = 100 - (STDEV(participantesRegistrados) * 10)
    FROM Campana
    WHERE estado = 'Completada'
    AND fechaInicio >= DATEADD(MONTH, -6, GETDATE());
    
    -- Insertar o actualizar predicción
    IF EXISTS (SELECT 1 FROM Prediccion WHERE idCampana = @idCampana)
    BEGIN
        UPDATE Prediccion
        SET participacionEstimada = @participacionEstimada,
            nivelConfianza = @nivelConfianza,
            fechaPrediccion = GETDATE()
        WHERE idCampana = @idCampana;
    END
    ELSE
    BEGIN
        INSERT INTO Prediccion (idCampana, fechaPrediccion, participacionEstimada, nivelConfianza)
        VALUES (@idCampana, GETDATE(), @participacionEstimada, @nivelConfianza);
    END
END
GO

CREATE PROCEDURE [dbo].[sp_ObtenerEstadisticasCampana]
    @idCampana INT
AS
BEGIN
    SELECT 
        c.nombre AS Campana,
        c.metaParticipantes,
        c.participantesRegistrados,
        COUNT(DISTINCT a.idActividad) AS TotalActividades,
        COUNT(DISTINCT rp.idParticipante) AS TotalParticipantes,
        AVG(CAST(a.participantesRegistrados AS FLOAT) / NULLIF(a.capacidad, 0)) * 100 AS PorcentajeOcupacion
    FROM Campana c
    LEFT JOIN Actividad a ON c.idCampana = a.idCampana
    LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
    WHERE c.idCampana = @idCampana
    GROUP BY c.nombre, c.metaParticipantes, c.participantesRegistrados;
END
GO

CREATE PROCEDURE [dbo].[sp_ProcesarParticipantesInactivos]
AS
BEGIN
    DECLARE @idParticipante INT;
    DECLARE @ultimaParticipacion DATE;
    
    DECLARE participante_cursor CURSOR FOR
    SELECT p.idParticipante, MAX(rp.fechaRegistro)
    FROM Participante p
    LEFT JOIN RegistroParticipacion rp ON p.idParticipante = rp.idParticipante
    GROUP BY p.idParticipante
    HAVING MAX(rp.fechaRegistro) < DATEADD(MONTH, -6, GETDATE())
    OR MAX(rp.fechaRegistro) IS NULL;
    
    OPEN participante_cursor;
    FETCH NEXT FROM participante_cursor INTO @idParticipante, @ultimaParticipacion;
    
    WHILE @@FETCH_STATUS = 0
    BEGIN
        UPDATE Participante
        SET activo = 0
        WHERE idParticipante = @idParticipante;
        
        FETCH NEXT FROM participante_cursor INTO @idParticipante, @ultimaParticipacion;
    END
    
    CLOSE participante_cursor;
    DEALLOCATE participante_cursor;
END
GO 