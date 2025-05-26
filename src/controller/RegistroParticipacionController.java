package controller;

import java.util.List;
import model.RegistroParticipacion;
import model.dao.RegistroParticipacionDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Participante;
import model.database.DatabaseConnection;

/**
 * Controller class for handling RegistroParticipacion business logic
 */
public class RegistroParticipacionController {
    private final RegistroParticipacionDAO registroDAO;
    
    public RegistroParticipacionController() {
        this.registroDAO = new RegistroParticipacionDAO();
    }
    
    /**
     * Register a new participation record
     * @param idParticipante Participant ID
     * @param idActividad Activity ID
     * @param resultado Result
     * @param observaciones Observations
     * @return true if successful, false otherwise
     */
    public boolean registrarParticipacion(int idParticipante, int idActividad, String resultado, String observaciones) {
        // Primero verificar si ya existe un registro
        if (existeRegistro(idParticipante, idActividad)) {
            System.err.println("El participante ya está registrado en esta actividad");
            return false;
        }
        
        String sql = "INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idParticipante);
            stmt.setInt(2, idActividad);
            stmt.setString(3, resultado);
            stmt.setString(4, observaciones);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering participation: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Check if a participation record already exists
     * @param idParticipante Participant ID
     * @param idActividad Activity ID
     * @return true if exists, false otherwise
     */
    private boolean existeRegistro(int idParticipante, int idActividad) {
        String sql = "SELECT COUNT(*) FROM RegistroParticipacion WHERE idParticipante = ? AND idActividad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idParticipante);
            stmt.setInt(2, idActividad);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing registration: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * Update an existing participation record
     * @param idRegistro Record ID
     * @param idParticipante Participant ID
     * @param idActividad Activity ID
     * @param resultado Result of the participation
     * @param observaciones Observations
     * @return true if successful, false otherwise
     */
    public boolean actualizarRegistro(int idRegistro, int idParticipante, int idActividad, String resultado, String observaciones) {
        // Check if record exists
        RegistroParticipacion registro = registroDAO.findById(idRegistro);
        if (registro == null) {
            return false; // Record doesn't exist
        }
        
        registro.setIdParticipante(idParticipante);
        registro.setIdActividad(idActividad);
        registro.setResultado(resultado);
        registro.setObservaciones(observaciones);
        
        return registroDAO.update(registro);
    }
    
    /**
     * Delete a participation record
     * @param idRegistro Record ID
     * @return true if successful, false otherwise
     */
    public boolean eliminarRegistro(int idRegistro) {
        return registroDAO.delete(idRegistro);
    }
    
    /**
     * Get a record by ID
     * @param idRegistro Record ID
     * @return The record if found, null otherwise
     */
    public RegistroParticipacion obtenerRegistroPorId(int idRegistro) {
        return registroDAO.findById(idRegistro);
    }
    
    /**
     * Get all records
     * @return A list of all records
     */
    public List<RegistroParticipacion> listarRegistros() {
        return registroDAO.findAll();
    }
    
    /**
     * Get records by participant
     * @param idParticipante Participant ID
     * @return A list of records for the participant
     */
    public List<RegistroParticipacion> listarRegistrosPorParticipante(int idParticipante) {
        return registroDAO.findByParticipante(idParticipante);
    }
    
    /**
     * Get records by activity
     * @param idActividad Activity ID
     * @return A list of records for the activity
     */
    public List<RegistroParticipacion> listarRegistrosPorActividad(int idActividad) {
        List<RegistroParticipacion> registros = new ArrayList<>();
        String sql = "SELECT r.*, p.* FROM RegistroParticipacion r " +
                    "INNER JOIN Participante p ON r.idParticipante = p.idParticipante " +
                    "WHERE r.idActividad = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idActividad);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RegistroParticipacion registro = new RegistroParticipacion();
                registro.setIdRegistro(rs.getInt("idRegistro"));
                registro.setIdParticipante(rs.getInt("idParticipante"));
                registro.setIdActividad(rs.getInt("idActividad"));
                registro.setResultado(rs.getString("resultado"));
                registro.setObservaciones(rs.getString("observaciones"));
                registro.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                
                // Crear y establecer el participante
                Participante participante = new Participante();
                participante.setIdParticipante(rs.getInt("idParticipante"));
                participante.setNombre(rs.getString("nombre"));
                participante.setApellido(rs.getString("apellido"));
                participante.setDni(rs.getString("dni"));
                participante.setEdad(rs.getInt("edad"));
                participante.setSexo(rs.getString("sexo"));
                participante.setDireccion(rs.getString("direccion"));
                participante.setTelefono(rs.getString("telefono"));
                participante.setCorreo(rs.getString("correo"));
                
                registro.setParticipante(participante);
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return registros;
    }
    
    /**
     * Count participants for an activity
     * @param idActividad Activity ID
     * @return Number of participants
     */
    public int contarParticipantesPorActividad(int idActividad) {
        return registroDAO.countParticipantsByActividad(idActividad);
    }
    
    /**
     * Count activities for a participant
     * @param idParticipante Participant ID
     * @return Number of activities
     */
    public int contarActividadesPorParticipante(int idParticipante) {
        return registroDAO.countActividadesByParticipante(idParticipante);
    }
} 