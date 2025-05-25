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
    contrasena VARCHAR(100) NOT NULL
);
GO

CREATE TABLE Campana (
    idCampana INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    idResponsable INT NOT NULL,
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
    direccion VARCHAR(200)
);
GO

CREATE TABLE Actividad (
    idActividad INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha DATE NOT NULL,
    hora VARCHAR(10) NOT NULL,
    idCampana INT NOT NULL,
    FOREIGN KEY (idCampana) REFERENCES Campana(idCampana)
);
GO

CREATE TABLE RegistroParticipacion (
    idRegistro INT PRIMARY KEY IDENTITY(1,1),
    idParticipante INT NOT NULL,
    idActividad INT NOT NULL,
    resultado VARCHAR(100),
    observaciones TEXT,
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
    notas TEXT,
    FOREIGN KEY (idCampana) REFERENCES Campana(idCampana),
    CONSTRAINT UC_Prediccion_Campana UNIQUE (idCampana)
);
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
CREATE PROCEDURE sp_GenerarPrediccion
    @idCampana INT,
    @notas TEXT = NULL
AS
BEGIN
    DECLARE @participacionEstimada INT;
    DECLARE @idResponsable INT;
    
    -- Get the responsible user for the campaign
    SELECT @idResponsable = idResponsable FROM Campana WHERE idCampana = @idCampana;
    
    -- Calculate estimated participation based on historical data
    -- (simplified for this example)
    SELECT @participacionEstimada = AVG(cantidadParticipantes) * 1.1
    FROM vw_ParticipacionPorCampana
    WHERE idCampana IN (SELECT idCampana FROM Campana WHERE idResponsable = @idResponsable AND idCampana != @idCampana);
    
    -- If no historical data, use default value
    IF @participacionEstimada IS NULL
        SET @participacionEstimada = 50;
    
    -- Check if prediction already exists
    IF EXISTS (SELECT 1 FROM Prediccion WHERE idCampana = @idCampana)
    BEGIN
        -- Update existing prediction
        UPDATE Prediccion
        SET fechaPrediccion = GETDATE(),
            participacionEstimada = @participacionEstimada,
            notas = ISNULL(@notas, notas)
        WHERE idCampana = @idCampana;
    END
    ELSE
    BEGIN
        -- Insert new prediction
        INSERT INTO Prediccion (idCampana, fechaPrediccion, participacionEstimada, notas)
        VALUES (@idCampana, GETDATE(), @participacionEstimada, @notas);
    END
END;
GO

CREATE PROCEDURE sp_RegistrarParticipacion
    @idParticipante INT,
    @idActividad INT,
    @resultado VARCHAR(100) = NULL,
    @observaciones TEXT = NULL
AS
BEGIN
    -- Check if registration already exists
    IF EXISTS (SELECT 1 FROM RegistroParticipacion WHERE idParticipante = @idParticipante AND idActividad = @idActividad)
    BEGIN
        -- Update existing registration
        UPDATE RegistroParticipacion
        SET resultado = ISNULL(@resultado, resultado),
            observaciones = ISNULL(@observaciones, observaciones)
        WHERE idParticipante = @idParticipante AND idActividad = @idActividad;
    END
    ELSE
    BEGIN
        -- Insert new registration
        INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones)
        VALUES (@idParticipante, @idActividad, @resultado, @observaciones);
    END
END;
GO 