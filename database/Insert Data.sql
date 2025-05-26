-- Insertar usuarios
INSERT INTO [dbo].[Usuario] (nombre, apellido, correo, dni, rol, contrasena) VALUES
('Manuel', 'Vera', 'mvera@dev.com', 'zsnow', 'Administrador', '1'),
('Juan', 'Jaramillo', 'juanjx6@dev.com', 'juanjx6', 'Desarrollador', '1'),
('Jorge', 'Moreno', 'jmoreno@dev.com', 'jmoreno', 'Desarrollador', '1');

-- Insertar campañas
INSERT INTO [dbo].[Campana] (nombre, descripcion, fechaInicio, fechaFin, idResponsable, estado, metaParticipantes) VALUES
('Campaña de Vacunación COVID-19', 'Campaña masiva de vacunación contra COVID-19', '2024-01-01', '2024-03-31', 1, 'activa', 500),
('Chequeo Médico Preventivo', 'Chequeo médico general para adultos mayores', '2024-02-01', '2024-02-28', 1, 'finalizada', 200),
('Educación en Salud Bucal', 'Talleres de higiene bucal para escolares', '2024-01-15', '2024-02-15', 2, 'activa', 300),
('Vacunación contra Influenza', 'Campaña de vacunación contra influenza estacional', '2024-03-01', '2024-03-31', 2, 'activa', 400),
('Detección Temprana de Diabetes', 'Screening de diabetes en población de riesgo', '2024-02-15', '2024-03-15', 3, 'activa', 250),
('Nutrición Infantil', 'Programa de educación nutricional para madres', '2024-01-01', '2024-04-30', 3, 'activa', 150);

-- Insertar participantes
INSERT INTO [dbo].[Participante] (nombre, apellido, dni, edad, sexo, direccion, telefono, correo) VALUES
('Juan', 'Pérez', '12345678', 44, 'Masculino', 'Av. Principal 123', '555-0101', 'juan@email.com'),
('María', 'González', '23456789', 39, 'Femenino', 'Calle Secundaria 456', '555-0102', 'maria@email.com'),
('Carlos', 'Rodríguez', '34567890', 49, 'Masculino', 'Jr. Lima 789', '555-0103', 'carlos@email.com'),
('Ana', 'Martínez', '45678901', 34, 'Femenino', 'Av. Los Olivos 321', '555-0104', 'ana@email.com'),
('Pedro', 'Sánchez', '56789012', 42, 'Masculino', 'Calle Real 654', '555-0105', 'pedro@email.com'),
('Laura', 'Díaz', '67890123', 36, 'Femenino', 'Av. San Martín 987', '555-0106', 'laura@email.com'),
('Miguel', 'López', '78901234', 46, 'Masculino', 'Jr. Tacna 147', '555-0107', 'miguel@email.com'),
('Sofía', 'Hernández', '89012345', 32, 'Femenino', 'Calle Arequipa 258', '555-0108', 'sofia@email.com'),
('Roberto', 'Torres', '90123456', 39, 'Masculino', 'Av. La Marina 369', '555-0109', 'roberto@email.com'),
('Carmen', 'Vargas', '01234567', 29, 'Femenino', 'Jr. Callao 741', '555-0110', 'carmen@email.com');

-- Insertar actividades
INSERT INTO [dbo].[Actividad] (nombre, descripcion, fecha, hora, idCampana, capacidad, estado) VALUES
('Vacunación Centro Comercial', 'Punto de vacunación en centro comercial', '2024-01-15', '09:00 - 17:00', 1, 500, 'finalizada'),
('Vacunación Hospital Central', 'Punto de vacunación en hospital', '2024-01-20', '08:00 - 16:00', 1, 300, 'finalizada'),
('Vacunación Parque Municipal', 'Punto de vacunación móvil', '2024-02-01', '09:00 - 17:00', 1, 400, 'en_curso'),
('Chequeo Médico Centro de Salud', 'Chequeo médico general', '2024-02-05', '08:00 - 16:00', 2, 200, 'finalizada'),
('Chequeo Médico Club Social', 'Chequeo médico en club social', '2024-02-10', '09:00 - 17:00', 2, 150, 'finalizada'),
('Taller Escolar Primaria', 'Taller de higiene bucal', '2024-01-20', '10:00 - 12:00', 3, 100, 'finalizada'),
('Taller Escolar Secundaria', 'Taller de higiene bucal', '2024-01-25', '10:00 - 12:00', 3, 120, 'en_curso'),
('Vacunación Centro de Salud', 'Punto de vacunación influenza', '2024-03-05', '08:00 - 16:00', 4, 300, 'planificada'),
('Screening Diabetes Centro', 'Detección temprana de diabetes', '2024-02-20', '09:00 - 17:00', 5, 250, 'en_curso'),
('Taller Nutrición Comedor', 'Taller de nutrición infantil', '2024-01-10', '10:00 - 12:00', 6, 50, 'finalizada');

-- Insertar registros de participación
INSERT INTO [dbo].[RegistroParticipacion] (idParticipante, idActividad, resultado, observaciones) VALUES
(1, 1, 'completado', 'Primera dosis'),
(2, 1, 'completado', 'Primera dosis'),
(3, 1, 'completado', 'Primera dosis'),
(4, 2, 'completado', 'Primera dosis'),
(5, 2, 'completado', 'Primera dosis'),
(6, 3, 'completado', 'Primera dosis'),
(7, 3, 'completado', 'Primera dosis'),
(8, 4, 'completado', 'Chequeo completo'),
(9, 4, 'completado', 'Chequeo completo'),
(10, 5, 'completado', 'Chequeo completo'),
(1, 6, 'completado', 'Taller completado'),
(2, 6, 'completado', 'Taller completado'),
(3, 7, 'completado', 'Taller en curso'),
(4, 7, 'completado', 'Taller en curso'),
(5, 9, 'completado', 'Screening inicial'),
(6, 9, 'completado', 'Screening inicial'),
(7, 10, 'completado', 'Taller completado'),
(8, 10, 'completado', 'Taller completado'),
(9, 10, 'completado', 'Taller completado'),
(10, 10, 'completado', 'Taller completado');

-- Insertar predicciones
INSERT INTO [dbo].[Prediccion] (idCampana, fechaPrediccion, participacionEstimada, nivelConfianza, notas) VALUES
(1, '2024-01-01', 450, 0.85, 'Predicción inicial'),
(2, '2024-02-01', 180, 0.90, 'Predicción inicial'),
(3, '2024-01-15', 280, 0.88, 'Predicción inicial'),
(4, '2024-03-01', 350, 0.87, 'Predicción inicial'),
(5, '2024-02-15', 220, 0.86, 'Predicción inicial'),
(6, '2024-01-01', 130, 0.89, 'Predicción inicial'); 