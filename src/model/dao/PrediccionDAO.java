package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Prediccion;
import model.database.DatabaseConnection;

/**
 * Objeto de Acceso a Datos para la entidad Prediccion
 */
public class PrediccionDAO {
    private Connection conn;
    
    public PrediccionDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }
    
    /**
     * Inserta una nueva predicción en la base de datos
     * @param prediccion La predicción a insertar
     * @return true si es exitoso, false en caso contrario
     */
    public boolean insert(Prediccion prediccion) {
        String sql = "INSERT INTO Prediccion (idCampana, fechaPrediccion, participacionEstimada, nivelConfianza, notas) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, prediccion.getIdCampana());
            stmt.setDate(2, new java.sql.Date(prediccion.getFechaPrediccion().getTime()));
            stmt.setInt(3, prediccion.getParticipacionEstimada());
            stmt.setDouble(4, prediccion.getNivelConfianza());
            stmt.setString(5, prediccion.getNotas());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar predicción: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing prediction in the database
     * @param prediccion The prediction to update
     * @return true if successful, false otherwise
     */
    public boolean update(Prediccion prediccion) {
        String sql = "UPDATE Prediccion SET idCampana = ?, fechaPrediccion = ?, participacionEstimada = ?, nivelConfianza = ?, notas = ? WHERE idPrediccion = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, prediccion.getIdCampana());
            stmt.setDate(2, new java.sql.Date(prediccion.getFechaPrediccion().getTime()));
            stmt.setInt(3, prediccion.getParticipacionEstimada());
            stmt.setDouble(4, prediccion.getNivelConfianza());
            stmt.setString(5, prediccion.getNotas());
            stmt.setInt(6, prediccion.getIdPrediccion());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating prediction: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a prediction from the database
     * @param idPrediccion The ID of the prediction to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idPrediccion) {
        String sql = "DELETE FROM Prediccion WHERE idPrediccion = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPrediccion);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting prediction: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find a prediction by its ID
     * @param idPrediccion The ID of the prediction to find
     * @return The prediction if found, null otherwise
     */
    public Prediccion findById(int idPrediccion) {
        String sql = "SELECT * FROM Prediccion WHERE idPrediccion = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPrediccion);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPrediccionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding prediction by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Find a prediction by campaign ID
     * @param idCampana The ID of the campaign
     * @return The prediction if found, null otherwise
     */
    public Prediccion findByCampana(int idCampana) {
        String sql = "SELECT * FROM Prediccion WHERE idCampana = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCampana);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPrediccionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding prediction by campaign ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all predictions from the database
     * @return A list of all predictions
     */
    public List<Prediccion> findAll() {
        List<Prediccion> predicciones = new ArrayList<>();
        String sql = "SELECT * FROM Prediccion";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                predicciones.add(extractPrediccionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all predictions: " + e.getMessage());
        }
        return predicciones;
    }
    
    /**
     * Extract a prediction from a result set
     * @param rs The result set to extract from
     * @return The extracted prediction
     * @throws SQLException If an error occurs
     */
    private Prediccion extractPrediccionFromResultSet(ResultSet rs) throws SQLException {
        Prediccion prediccion = new Prediccion();
        prediccion.setIdPrediccion(rs.getInt("idPrediccion"));
        prediccion.setIdCampana(rs.getInt("idCampana"));
        prediccion.setFechaPrediccion(rs.getDate("fechaPrediccion"));
        prediccion.setParticipacionEstimada(rs.getInt("participacionEstimada"));
        prediccion.setNivelConfianza(rs.getDouble("nivelConfianza"));
        prediccion.setNotas(rs.getString("notas"));
        return prediccion;
    }
} 