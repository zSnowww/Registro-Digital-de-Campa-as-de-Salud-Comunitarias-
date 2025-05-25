package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Campana;
import model.database.DatabaseConnection;

/**
 * Data Access Object for Campana entity
 */
public class CampanaDAO {
    private Connection conn;
    
    public CampanaDAO() {
        this.conn = DatabaseConnection.getConnection();
    }
    
    /**
     * Insert a new campaign into the database
     * @param campana The campaign to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(Campana campana) {
        String sql = "INSERT INTO Campana (nombre, descripcion, fechaInicio, fechaFin, idResponsable) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, campana.getNombre());
            stmt.setString(2, campana.getDescripcion());
            stmt.setDate(3, new java.sql.Date(campana.getFechaInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(campana.getFechaFin().getTime()));
            stmt.setInt(5, campana.getIdResponsable());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting campaign: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing campaign in the database
     * @param campana The campaign to update
     * @return true if successful, false otherwise
     */
    public boolean update(Campana campana) {
        String sql = "UPDATE Campana SET nombre = ?, descripcion = ?, fechaInicio = ?, fechaFin = ?, idResponsable = ? WHERE idCampana = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, campana.getNombre());
            stmt.setString(2, campana.getDescripcion());
            stmt.setDate(3, new java.sql.Date(campana.getFechaInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(campana.getFechaFin().getTime()));
            stmt.setInt(5, campana.getIdResponsable());
            stmt.setInt(6, campana.getIdCampana());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating campaign: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a campaign from the database
     * @param idCampana The ID of the campaign to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idCampana) {
        String sql = "DELETE FROM Campana WHERE idCampana = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCampana);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting campaign: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find a campaign by its ID
     * @param idCampana The ID of the campaign to find
     * @return The campaign if found, null otherwise
     */
    public Campana findById(int idCampana) {
        String sql = "SELECT * FROM Campana WHERE idCampana = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCampana);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCampanaFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding campaign by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all campaigns from the database
     * @return A list of all campaigns
     */
    public List<Campana> findAll() {
        List<Campana> campanas = new ArrayList<>();
        String sql = "SELECT * FROM Campana";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                campanas.add(extractCampanaFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all campaigns: " + e.getMessage());
        }
        return campanas;
    }
    
    /**
     * Get all campaigns by a responsible user
     * @param idResponsable The ID of the responsible user
     * @return A list of campaigns by the user
     */
    public List<Campana> findByResponsable(int idResponsable) {
        List<Campana> campanas = new ArrayList<>();
        String sql = "SELECT * FROM Campana WHERE idResponsable = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idResponsable);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                campanas.add(extractCampanaFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding campaigns by responsible: " + e.getMessage());
        }
        return campanas;
    }
    
    /**
     * Extract a campaign from a result set
     * @param rs The result set to extract from
     * @return The extracted campaign
     * @throws SQLException If an error occurs
     */
    private Campana extractCampanaFromResultSet(ResultSet rs) throws SQLException {
        Campana campana = new Campana();
        campana.setIdCampana(rs.getInt("idCampana"));
        campana.setNombre(rs.getString("nombre"));
        campana.setDescripcion(rs.getString("descripcion"));
        campana.setFechaInicio(rs.getDate("fechaInicio"));
        campana.setFechaFin(rs.getDate("fechaFin"));
        campana.setIdResponsable(rs.getInt("idResponsable"));
        return campana;
    }
} 