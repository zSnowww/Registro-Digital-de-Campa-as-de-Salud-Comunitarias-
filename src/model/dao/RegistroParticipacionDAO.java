package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.RegistroParticipacion;
import model.database.DatabaseConnection;

/**
 * Data Access Object for RegistroParticipacion entity
 */
public class RegistroParticipacionDAO {
    
    /**
     * Insert a new participation record into the database
     * @param registro The record to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(RegistroParticipacion registro) {
        String sql = "INSERT INTO RegistroParticipacion (idParticipante, idActividad, resultado, observaciones) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, registro.getIdParticipante());
            stmt.setInt(2, registro.getIdActividad());
            stmt.setString(3, registro.getResultado());
            stmt.setString(4, registro.getObservaciones());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting participation record: " + e.getMessage());
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
     * Update an existing participation record in the database
     * @param registro The record to update
     * @return true if successful, false otherwise
     */
    public boolean update(RegistroParticipacion registro) {
        String sql = "UPDATE RegistroParticipacion SET idParticipante = ?, idActividad = ?, resultado = ?, observaciones = ? WHERE idRegistro = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, registro.getIdParticipante());
            stmt.setInt(2, registro.getIdActividad());
            stmt.setString(3, registro.getResultado());
            stmt.setString(4, registro.getObservaciones());
            stmt.setInt(5, registro.getIdRegistro());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating participation record: " + e.getMessage());
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
     * Delete a participation record from the database
     * @param idRegistro The ID of the record to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idRegistro) {
        String sql = "DELETE FROM RegistroParticipacion WHERE idRegistro = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idRegistro);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting participation record: " + e.getMessage());
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
     * Find a participation record by its ID
     * @param idRegistro The ID of the record to find
     * @return The record if found, null otherwise
     */
    public RegistroParticipacion findById(int idRegistro) {
        String sql = "SELECT * FROM RegistroParticipacion WHERE idRegistro = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idRegistro);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractRegistroFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding participation record by ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Get all participation records from the database
     * @return A list of all participation records
     */
    public List<RegistroParticipacion> findAll() {
        List<RegistroParticipacion> registros = new ArrayList<>();
        String sql = "SELECT * FROM RegistroParticipacion";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                registros.add(extractRegistroFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all participation records: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return registros;
    }
    
    /**
     * Get participation records by participant ID
     * @param idParticipante The ID of the participant
     * @return A list of records for the participant
     */
    public List<RegistroParticipacion> findByParticipante(int idParticipante) {
        List<RegistroParticipacion> registros = new ArrayList<>();
        String sql = "SELECT * FROM RegistroParticipacion WHERE idParticipante = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idParticipante);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                registros.add(extractRegistroFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding participation records by participant: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return registros;
    }
    
    /**
     * Get participation records by activity ID
     * @param idActividad The ID of the activity
     * @return A list of records for the activity
     */
    public List<RegistroParticipacion> findByActividad(int idActividad) {
        List<RegistroParticipacion> registros = new ArrayList<>();
        String sql = "SELECT * FROM RegistroParticipacion WHERE idActividad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idActividad);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                registros.add(extractRegistroFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding participation records by activity: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return registros;
    }
    
    /**
     * Count participants for a specific activity
     * @param idActividad The ID of the activity
     * @return The number of participants
     */
    public int countParticipantsByActividad(int idActividad) {
        String sql = "SELECT COUNT(*) as total FROM RegistroParticipacion WHERE idActividad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idActividad);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error counting participants by activity: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    
    /**
     * Count activities for a specific participant
     * @param idParticipante The ID of the participant
     * @return The number of activities
     */
    public int countActividadesByParticipante(int idParticipante) {
        String sql = "SELECT COUNT(*) as total FROM RegistroParticipacion WHERE idParticipante = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idParticipante);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error counting activities by participant: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    
    /**
     * Extract a participation record from a result set
     * @param rs The result set to extract from
     * @return The extracted record
     * @throws SQLException If an error occurs
     */
    private RegistroParticipacion extractRegistroFromResultSet(ResultSet rs) throws SQLException {
        RegistroParticipacion registro = new RegistroParticipacion();
        registro.setIdRegistro(rs.getInt("idRegistro"));
        registro.setIdParticipante(rs.getInt("idParticipante"));
        registro.setIdActividad(rs.getInt("idActividad"));
        registro.setResultado(rs.getString("resultado"));
        registro.setObservaciones(rs.getString("observaciones"));
        registro.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
        return registro;
    }
} 