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
    private Connection conn;
    
    public ActividadDAO() {
        this.conn = DatabaseConnection.getConnection();
    }
    
    /**
     * Insert a new activity into the database
     * @param actividad The activity to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(Actividad actividad) {
        String sql = "INSERT INTO Actividad (nombre, descripcion, fecha, hora, idCampana) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, actividad.getNombre());
            stmt.setString(2, actividad.getDescripcion());
            stmt.setDate(3, new java.sql.Date(actividad.getFecha().getTime()));
            stmt.setString(4, actividad.getHora());
            stmt.setInt(5, actividad.getIdCampana());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting activity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing activity in the database
     * @param actividad The activity to update
     * @return true if successful, false otherwise
     */
    public boolean update(Actividad actividad) {
        String sql = "UPDATE Actividad SET nombre = ?, descripcion = ?, fecha = ?, hora = ?, idCampana = ? WHERE idActividad = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, actividad.getNombre());
            stmt.setString(2, actividad.getDescripcion());
            stmt.setDate(3, new java.sql.Date(actividad.getFecha().getTime()));
            stmt.setString(4, actividad.getHora());
            stmt.setInt(5, actividad.getIdCampana());
            stmt.setInt(6, actividad.getIdActividad());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating activity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete an activity from the database
     * @param idActividad The ID of the activity to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idActividad) {
        String sql = "DELETE FROM Actividad WHERE idActividad = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idActividad);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting activity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find an activity by its ID
     * @param idActividad The ID of the activity to find
     * @return The activity if found, null otherwise
     */
    public Actividad findById(int idActividad) {
        String sql = "SELECT * FROM Actividad WHERE idActividad = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idActividad);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractActividadFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding activity by ID: " + e.getMessage());
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
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all activities: " + e.getMessage());
        }
        return actividades;
    }
    
    /**
     * Get activities by campaign ID
     * @param idCampana The ID of the campaign
     * @return A list of activities for the campaign
     */
    public List<Actividad> findByCampana(int idCampana) {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM Actividad WHERE idCampana = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCampana);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                actividades.add(extractActividadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by campaign: " + e.getMessage());
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
        return actividad;
    }
} 