-- Crear la base de datos
CREATE DATABASE CampanaSalud;
USE CampanaSalud;

-- Crear tabla Usuario
CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    rol VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    fechaCreacion DATETIME DEFAULT GETDATE(),
    ultimoAcceso DATETIME DEFAULT GETDATE(),
    activo BIT DEFAULT 1
);

-- Crear tabla TipoActividad
CREATE TABLE TipoActividad (
    idTipoActividad INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- Crear tabla Actividad
CREATE TABLE Actividad (
    idActividad INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fechaInicio DATETIME NOT NULL,
    fechaFin DATETIME NOT NULL,
    ubicacion VARCHAR(200),
    idTipoActividad INT,
    capacidad INT NOT NULL DEFAULT 40,
    estado VARCHAR(20) DEFAULT 'planificada' CHECK (estado IN ('planificada', 'en_curso', 'finalizada', 'cancelada')),
    FOREIGN KEY (idTipoActividad) REFERENCES TipoActividad(idTipoActividad)
);

-- Crear tabla Participante
CREATE TABLE Participante (
    idParticipante INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    edad INT NOT NULL,
    sexo VARCHAR(10) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    fechaRegistro DATETIME DEFAULT GETDATE(),
    activo BIT DEFAULT 1
);

-- Crear tabla RegistroParticipacion
CREATE TABLE RegistroParticipacion (
    idRegistro INT PRIMARY KEY IDENTITY(1,1),
    idParticipante INT,
    idActividad INT,
    fechaRegistro DATETIME DEFAULT GETDATE(),
    resultado VARCHAR(20) DEFAULT 'completado' CHECK (resultado IN ('completado', 'incompleto', 'ausente')),
    observaciones TEXT,
    FOREIGN KEY (idParticipante) REFERENCES Participante(idParticipante),
    FOREIGN KEY (idActividad) REFERENCES Actividad(idActividad)
);

-- Crear tabla Campana
CREATE TABLE Campana (
    idCampana INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    estado VARCHAR(20) DEFAULT 'activa' CHECK (estado IN ('activa', 'finalizada', 'cancelada')),
    metaParticipantes INT NOT NULL DEFAULT 30,
    participantesRegistrados INT DEFAULT 0
);

-- Crear tabla Prediccion
CREATE TABLE Prediccion (
    idPrediccion INT PRIMARY KEY IDENTITY(1,1),
    idActividad INT,
    fechaPrediccion DATETIME DEFAULT GETDATE(),
    participantesEsperados INT NOT NULL,
    nivelConfianza DECIMAL(3,2) NOT NULL DEFAULT 0.85,
    FOREIGN KEY (idActividad) REFERENCES Actividad(idActividad)
);

-- Insertar datos iniciales
INSERT INTO TipoActividad (nombre, descripcion) VALUES
('Taller', 'Sesiones prácticas de aprendizaje'),
('Charla', 'Presentaciones informativas'),
('Seminario', 'Eventos académicos extensos'),
('Curso', 'Programas de formación estructurados');

-- Insertar usuario administrador por defecto
INSERT INTO Usuario (nombre, apellido, correo, dni, rol, contrasena) VALUES
('Manuel', 'Vera', 'mvera@dev.com', 'zsnow', 'Administrador', '1'),
('Juan', 'Jaramillo', 'juanjx6@dev.com', 'juanjx6', 'Desarrollador', '1'),
('Jorge', 'Moreno', 'jmoreno@dev.com', 'jmoreno', 'Desarrollador', '1');

-- Insertar actividades de ejemplo con capacidades predefinidas
INSERT INTO Actividad (nombre, descripcion, fechaInicio, fechaFin, ubicacion, idTipoActividad, capacidad) VALUES
('Taller de Programación Java', 'Introducción a Java', '2024-03-01 09:00:00', '2024-03-01 13:00:00', 'Aula 101', 1, 30),
('Charla sobre Inteligencia Artificial', 'Fundamentos de IA', '2024-03-02 15:00:00', '2024-03-02 17:00:00', 'Auditorio', 2, 50),
('Seminario de Desarrollo Web', 'Tecnologías modernas', '2024-03-03 09:00:00', '2024-03-03 18:00:00', 'Centro de Convenciones', 3, 100),
('Curso de Base de Datos', 'SQL y NoSQL', '2024-03-04 14:00:00', '2024-03-04 18:00:00', 'Laboratorio 2', 4, 25);

-- Insertar campañas de ejemplo con metas predefinidas
INSERT INTO Campana (nombre, descripcion, fechaInicio, fechaFin, metaParticipantes) VALUES
('Campaña Talleres 2024', 'Promoción de talleres', '2024-03-01', '2024-03-31', 25),
('Campaña Charlas 2024', 'Promoción de charlas', '2024-03-01', '2024-03-31', 40),
('Campaña Seminarios 2024', 'Promoción de seminarios', '2024-03-01', '2024-03-31', 80),
('Campaña Cursos 2024', 'Promoción de cursos', '2024-03-01', '2024-03-31', 20);

-- Insertar predicciones de ejemplo con nivel de confianza predefinido
INSERT INTO Prediccion (idActividad, participantesEsperados, nivelConfianza) VALUES
(1, 25, 0.85),
(2, 40, 0.85),
(3, 80, 0.85),
(4, 20, 0.85);

-- Create views for reporting
CREATE VIEW vw_ParticipacionPorActividad AS
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
    a.idActividad, a.nombre, c.nombre;

CREATE VIEW vw_ParticipacionPorCampana AS
SELECT 
    c.idCampana,
    c.nombre AS nombreCampana,
    COUNT(DISTINCT rp.idParticipante) AS cantidadParticipantes,
    COUNT(DISTINCT a.idActividad) AS cantidadActividades
FROM 
    Campana c
    LEFT JOIN Actividad a ON c.idCampana = a.idCampana
    LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
GROUP BY 
    c.idCampana, c.nombre;

CREATE VIEW vw_PrediccionVsRealidad AS
SELECT 
    c.idCampana,
    c.nombre AS nombreCampana,
    p.participantesEsperados,
    COUNT(DISTINCT rp.idParticipante) AS participacionReal,
    (COUNT(DISTINCT rp.idParticipante) - p.participantesEsperados) AS diferencia
FROM 
    Campana c
    INNER JOIN Prediccion p ON c.idCampana = p.idCampana
    LEFT JOIN Actividad a ON c.idCampana = a.idCampana
    LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
GROUP BY 
    c.idCampana, c.nombre, p.participantesEsperados;

-- Create stored procedures
CREATE PROCEDURE sp_RegistrarParticipacion
    @idParticipante INT,
    @idActividad INT,
    @resultado VARCHAR(20) = NULL,
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
END;

CREATE PROCEDURE sp_GenerarPrediccion
    @idCampana INT
AS
BEGIN
    DECLARE @participantesEsperados INT;
    DECLARE @nivelConfianza DECIMAL(3,2);
    
    -- Calcular predicción basada en datos históricos
    SELECT @participantesEsperados = AVG(participantesRegistrados) * 1.1
    FROM Campana
    WHERE estado = 'finalizada'
    AND fechaInicio >= DATEADD(MONTH, -6, GETDATE());
    
    -- Calcular nivel de confianza basado en la variabilidad de datos históricos
    SELECT @nivelConfianza = 100 - (STDEV(participantesRegistrados) * 10)
    FROM Campana
    WHERE estado = 'finalizada'
    AND fechaInicio >= DATEADD(MONTH, -6, GETDATE());
    
    -- Insertar o actualizar predicción
    IF EXISTS (SELECT 1 FROM Prediccion WHERE idCampana = @idCampana)
    BEGIN
        UPDATE Prediccion
        SET participantesEsperados = @participantesEsperados,
            nivelConfianza = @nivelConfianza,
            fechaPrediccion = GETDATE()
        WHERE idCampana = @idCampana;
    END
    ELSE
    BEGIN
        INSERT INTO Prediccion (idCampana, fechaPrediccion, participantesEsperados, nivelConfianza)
        VALUES (@idCampana, GETDATE(), @participantesEsperados, @nivelConfianza);
    END
END;

CREATE PROCEDURE sp_ObtenerEstadisticasCampana
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
END;

-- Create triggers
CREATE TRIGGER tr_ActualizarContadores
ON RegistroParticipacion
AFTER DELETE
AS
BEGIN
    UPDATE a
    SET a.participantesRegistrados = a.participantesRegistrados - 1
    FROM Actividad a
    INNER JOIN deleted d ON a.idActividad = d.idActividad;
    
    UPDATE c
    SET c.participantesRegistrados = c.participantesRegistrados - 1
    FROM Campana c
    INNER JOIN Actividad a ON c.idCampana = a.idCampana
    INNER JOIN deleted d ON a.idActividad = d.idActividad;
END;

-- Create indexes for better performance
CREATE INDEX idx_Campana_Fechas ON Campana(fechaInicio, fechaFin);
CREATE INDEX idx_Actividad_Campana ON Actividad(idCampana);
CREATE INDEX idx_RegistroParticipacion_Actividad ON RegistroParticipacion(idActividad);
CREATE INDEX idx_RegistroParticipacion_Participante ON RegistroParticipacion(idParticipante);
CREATE INDEX idx_Prediccion_Campana ON Prediccion(idCampana); 