-- Create database
CREATE DATABASE CampanaSalud;
GO

USE CampanaSalud;
GO

-- Create tables
CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    rol VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    fechaCreacion DATETIME DEFAULT GETDATE(),
    ultimoAcceso DATETIME,
    activo BIT DEFAULT 1
);
GO

CREATE TABLE Campana (
    idCampana INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    idResponsable INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'Planificada',
    metaParticipantes INT,
    participantesRegistrados INT DEFAULT 0,
    fechaCreacion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (idResponsable) REFERENCES Usuario(idUsuario)
);
GO

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
GO

CREATE TABLE Actividad (
    idActividad INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha DATE NOT NULL,
    hora VARCHAR(10) NOT NULL,
    idCampana INT NOT NULL,
    capacidad INT,
    participantesRegistrados INT DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'Planificada',
    FOREIGN KEY (idCampana) REFERENCES Campana(idCampana)
);
GO

CREATE TABLE RegistroParticipacion (
    idRegistro INT PRIMARY KEY IDENTITY(1,1),
    idParticipante INT NOT NULL,
    idActividad INT NOT NULL,
    resultado VARCHAR(100),
    observaciones TEXT,
    fechaRegistro DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (idParticipante) REFERENCES Participante(idParticipante),
    FOREIGN KEY (idActividad) REFERENCES Actividad(idActividad),
    CONSTRAINT UC_Participacion UNIQUE (idParticipante, idActividad)
);
GO

CREATE TABLE Prediccion (
    idPrediccion INT PRIMARY KEY IDENTITY(1,1),
    idCampana INT NOT NULL,
    fechaPrediccion DATE NOT NULL,
    participacionEstimada INT NOT NULL,
    nivelConfianza DECIMAL(5,2),
    notas TEXT,
    fechaCreacion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (idCampana) REFERENCES Campana(idCampana),
    CONSTRAINT UC_Prediccion_Campana UNIQUE (idCampana)
);
GO

CREATE TABLE LogActividad (
    idLog INT PRIMARY KEY IDENTITY(1,1),
    tabla VARCHAR(50) NOT NULL,
    accion VARCHAR(20) NOT NULL,
    idRegistro INT NOT NULL,
    fechaAccion DATETIME DEFAULT GETDATE(),
    usuario VARCHAR(100),
    detalles TEXT
);
GO

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
GO

CREATE VIEW vw_ParticipacionPorCampana AS
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
    c.idCampana, c.nombre, u.nombre, u.apellido;
GO

CREATE VIEW vw_PrediccionVsRealidad AS
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
    c.idCampana, c.nombre, p.participacionEstimada;
GO

-- Create stored procedures
CREATE PROCEDURE sp_RegistrarParticipacion
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
END;
GO

CREATE PROCEDURE sp_GenerarPrediccion
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
END;
GO

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
GO

-- Create triggers

-- Trigger para actualizar contadores de participación
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
GO

-- Trigger para logging de cambios
CREATE TRIGGER tr_LogCambios
ON Campana
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    DECLARE @accion VARCHAR(20);
    
    IF EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted)
        SET @accion = 'UPDATE';
    ELSE IF EXISTS (SELECT 1 FROM inserted)
        SET @accion = 'INSERT';
    ELSE
        SET @accion = 'DELETE';
    
    INSERT INTO LogActividad (tabla, accion, idRegistro, usuario, detalles)
    SELECT 
        'Campana',
        @accion,
        COALESCE(i.idCampana, d.idCampana),
        SYSTEM_USER,
        'Cambio en campaña: ' + COALESCE(i.nombre, d.nombre)
    FROM inserted i
    FULL OUTER JOIN deleted d ON i.idCampana = d.idCampana;
END;
GO

-- Create cursor for batch processing
CREATE PROCEDURE sp_ProcesarParticipantesInactivos
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
END;
GO

-- Insert initial admin user
INSERT INTO Usuario (nombre, apellido, correo, dni, rol, contrasena)
VALUES ('Admin', 'Sistema', 'admin@campanasalud.com', '00000000', 'Administrador', 'admin123');
GO

-- Insert sample data for testing
-- Sample users
INSERT INTO Usuario (nombre, apellido, correo, dni, rol, contrasena)
VALUES ('Juan', 'Pérez', 'juan.perez@campanasalud.com', '12345678', 'Gestor', 'pass123');

INSERT INTO Usuario (nombre, apellido, correo, dni, rol, contrasena)
VALUES ('María', 'López', 'maria.lopez@campanasalud.com', '87654321', 'Profesional de Salud', 'pass123');

-- Sample campaigns
INSERT INTO Campana (nombre, descripcion, fechaInicio, fechaFin, idResponsable)
VALUES ('Campaña de Vacunación 2023', 'Campaña anual de vacunación contra la influenza', '2023-05-01', '2023-06-30', 1);

INSERT INTO Campana (nombre, descripcion, fechaInicio, fechaFin, idResponsable)
VALUES ('Prevención de Diabetes', 'Campaña de concientización y detección temprana de diabetes', '2023-07-01', '2023-08-31', 2);

-- Sample activities
INSERT INTO Actividad (nombre, descripcion, fecha, hora, idCampana)
VALUES ('Vacunación en Centro Comunitario', 'Jornada de vacunación en el centro comunitario local', '2023-05-15', '09:00', 1);

INSERT INTO Actividad (nombre, descripcion, fecha, hora, idCampana)
VALUES ('Charla Informativa', 'Charla sobre la importancia de la vacunación', '2023-05-10', '15:00', 1);

INSERT INTO Actividad (nombre, descripcion, fecha, hora, idCampana)
VALUES ('Exámenes de Glucosa', 'Toma de muestras y análisis de niveles de glucosa', '2023-07-15', '08:00', 2);

-- Sample participants
INSERT INTO Participante (nombre, apellido, dni, edad, sexo, direccion)
VALUES ('Carlos', 'Gómez', '11223344', 45, 'Masculino', 'Av. Principal 123');

INSERT INTO Participante (nombre, apellido, dni, edad, sexo, direccion)
VALUES ('Ana', 'Martínez', '44332211', 35, 'Femenino', 'Calle Secundaria 456');

INSERT INTO Participante (nombre, apellido, dni, edad, sexo, direccion)
VALUES ('Pedro', 'Sánchez', '55667788', 60, 'Masculino', 'Jr. Los Pinos 789');

-- Sample registrations
INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones)
VALUES (1, 1, 'Completado', 'Se administró vacuna sin complicaciones');

INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones)
VALUES (2, 1, 'Completado', 'Se administró vacuna sin complicaciones');

INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones)
VALUES (1, 2, 'Asistió', 'Participó activamente en la charla');

INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones)
VALUES (3, 3, 'Requiere seguimiento', 'Niveles de glucosa ligeramente elevados');

-- Sample predictions
INSERT INTO Prediccion (idCampana, fechaPrediccion, participacionEstimada, notas)
VALUES (1, '2023-04-15', 150, 'Basado en la participación del año anterior');

INSERT INTO Prediccion (idCampana, fechaPrediccion, participacionEstimada, notas)
VALUES (2, '2023-06-15', 200, 'Estimación basada en campañas similares');
GO

-- Create indexes for better performance
CREATE INDEX idx_Campana_Fechas ON Campana(fechaInicio, fechaFin);
CREATE INDEX idx_Actividad_Campana ON Actividad(idCampana);
CREATE INDEX idx_RegistroParticipacion_Actividad ON RegistroParticipacion(idActividad);
CREATE INDEX idx_RegistroParticipacion_Participante ON RegistroParticipacion(idParticipante);
CREATE INDEX idx_Prediccion_Campana ON Prediccion(idCampana);
GO 