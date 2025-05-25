package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Participante;
import model.database.DatabaseConnection;

/**
 * Data Access Object for Participante entity
 */
public class ParticipanteDAO {
    private Connection conn;
    
    public ParticipanteDAO() {
        this.conn = DatabaseConnection.getConnection();
    }
    
    /**
     * Insert a new participant into the database
     * @param participante The participant to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(Participante participante) {
        String sql = "INSERT INTO Participante (nombre, apellido, dni, edad, sexo, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, participante.getNombre());
            stmt.setString(2, participante.getApellido());
            stmt.setString(3, participante.getDni());
            stmt.setInt(4, participante.getEdad());
            stmt.setString(5, participante.getSexo());
            stmt.setString(6, participante.getDireccion());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting participant: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing participant in the database
     * @param participante The participant to update
     * @return true if successful, false otherwise
     */
    public boolean update(Participante participante) {
        String sql = "UPDATE Participante SET nombre = ?, apellido = ?, dni = ?, edad = ?, sexo = ?, direccion = ? WHERE idParticipante = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, participante.getNombre());
            stmt.setString(2, participante.getApellido());
            stmt.setString(3, participante.getDni());
            stmt.setInt(4, participante.getEdad());
            stmt.setString(5, participante.getSexo());
            stmt.setString(6, participante.getDireccion());
            stmt.setInt(7, participante.getIdParticipante());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating participant: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a participant from the database
     * @param idParticipante The ID of the participant to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idParticipante) {
        String sql = "DELETE FROM Participante WHERE idParticipante = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idParticipante);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting participant: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find a participant by their ID
     * @param idParticipante The ID of the participant to find
     * @return The participant if found, null otherwise
     */
    public Participante findById(int idParticipante) {
        String sql = "SELECT * FROM Participante WHERE idParticipante = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idParticipante);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractParticipanteFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding participant by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Find a participant by their DNI
     * @param dni The DNI of the participant to find
     * @return The participant if found, null otherwise
     */
    public Participante findByDni(String dni) {
        String sql = "SELECT * FROM Participante WHERE dni = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractParticipanteFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding participant by DNI: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all participants from the database
     * @return A list of all participants
     */
    public List<Participante> findAll() {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT * FROM Participante";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                participantes.add(extractParticipanteFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all participants: " + e.getMessage());
        }
        return participantes;
    }
    
    /**
     * Find participants by name (first name or last name)
     * @param nombre The name to search for
     * @return A list of matching participants
     */
    public List<Participante> findByName(String nombre) {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT * FROM Participante WHERE nombre LIKE ? OR apellido LIKE ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nombre + "%");
            stmt.setString(2, "%" + nombre + "%");
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                participantes.add(extractParticipanteFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding participants by name: " + e.getMessage());
        }
        return participantes;
    }
    
    /**
     * Extract a participant from a result set
     * @param rs The result set to extract from
     * @return The extracted participant
     * @throws SQLException If an error occurs
     */
    private Participante extractParticipanteFromResultSet(ResultSet rs) throws SQLException {
        Participante participante = new Participante();
        participante.setIdParticipante(rs.getInt("idParticipante"));
        participante.setNombre(rs.getString("nombre"));
        participante.setApellido(rs.getString("apellido"));
        participante.setDni(rs.getString("dni"));
        participante.setEdad(rs.getInt("edad"));
        participante.setSexo(rs.getString("sexo"));
        participante.setDireccion(rs.getString("direccion"));
        return participante;
    }
} 