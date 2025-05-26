package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Actividad;
import model.database.DatabaseConnection;

/**
 * Data Access Object for Actividad entity
 */
public class ActividadDAO {
    
    /**
     * Insert a new activity into the database
     * @param actividad The activity to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(Actividad actividad) {
        String sql = "INSERT INTO Actividad (nombre, descripcion, fecha, hora, idCampana, capacidad, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, actividad.getNombre());
            stmt.setString(2, actividad.getDescripcion());
            stmt.setDate(3, new java.sql.Date(actividad.getFecha().getTime()));
            stmt.setString(4, actividad.getHora());
            stmt.setInt(5, actividad.getIdCampana());
            stmt.setInt(6, actividad.getCupoMaximo());
            stmt.setString(7, actividad.getEstado());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting activity: " + e.getMessage());
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
     * Update an existing activity in the database
     * @param actividad The activity to update
     * @return true if successful, false otherwise
     */
    public boolean update(Actividad actividad) {
        String sql = "UPDATE Actividad SET nombre = ?, descripcion = ?, fecha = ?, hora = ?, idCampana = ?, capacidad = ?, estado = ? WHERE idActividad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, actividad.getNombre());
            stmt.setString(2, actividad.getDescripcion());
            stmt.setDate(3, new java.sql.Date(actividad.getFecha().getTime()));
            stmt.setString(4, actividad.getHora());
            stmt.setInt(5, actividad.getIdCampana());
            stmt.setInt(6, actividad.getCupoMaximo());
            stmt.setString(7, actividad.getEstado());
            stmt.setInt(8, actividad.getIdActividad());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating activity: " + e.getMessage());
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
     * Delete an activity from the database
     * @param idActividad The ID of the activity to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idActividad) {
        String sql = "DELETE FROM Actividad WHERE idActividad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idActividad);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting activity: " + e.getMessage());
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
     * Find an activity by its ID
     * @param idActividad The ID of the activity to find
     * @return The activity if found, null otherwise
     */
    public Actividad findById(int idActividad) {
        String sql = "SELECT * FROM Actividad WHERE idActividad = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idActividad);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractActividadFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding activity by ID: " + e.getMessage());
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
     * Get all activities from the database
     * @return A list of all activities
     */
    public List<Actividad> findAll() {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM Actividad";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all activities: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return actividades;
    }
    
    /**
     * Search activities by name
     * @param nombre Name to search for
     * @return A list of matching activities
     */
    public List<Actividad> findByName(String nombre) {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM Actividad WHERE nombre LIKE ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nombre + "%");
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching activities by name: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return actividades;
    }
    
    /**
     * Get activities by date range
     * @param fechaInicio Start date
     * @param fechaFin End date
     * @return A list of activities in the date range
     */
    public List<Actividad> findByDateRange(java.util.Date fechaInicio, java.util.Date fechaFin) {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM Actividad WHERE fecha BETWEEN ? AND ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by date range: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return actividades;
    }
    
    /**
     * Get activities by status
     * @param estado Status to filter by
     * @return A list of activities with the specified status
     */
    public List<Actividad> findByStatus(String estado) {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM Actividad WHERE estado = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, estado);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by status: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return actividades;
    }
    
    /**
     * Get activities by campaign
     * @param idCampana Campaign ID
     * @return A list of activities for the campaign
     */
    public List<Actividad> findByCampana(int idCampana) {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM Actividad WHERE idCampana = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCampana);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by campaign: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return actividades;
    }
    
    /**
     * Extract an activity from a result set
     * @param rs The result set to extract from
     * @return The extracted activity
     * @throws SQLException If an error occurs
     */
    private Actividad extractActividadFromResultSet(ResultSet rs) throws SQLException {
        Actividad actividad = new Actividad();
        actividad.setIdActividad(rs.getInt("idActividad"));
        actividad.setNombre(rs.getString("nombre"));
        actividad.setDescripcion(rs.getString("descripcion"));
        actividad.setFecha(rs.getDate("fecha"));
        actividad.setHora(rs.getString("hora"));
        actividad.setIdCampana(rs.getInt("idCampana"));
        actividad.setCupoMaximo(rs.getInt("capacidad"));
        actividad.setEstado(rs.getString("estado"));
        return actividad;
    }
} 