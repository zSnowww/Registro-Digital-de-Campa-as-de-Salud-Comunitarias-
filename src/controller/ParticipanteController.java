package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.Participante;
import model.dao.ParticipanteDAO;
import model.database.DatabaseConnection;

/**
 * Controller class for handling Participante business logic
 */
public class ParticipanteController {
    private final ParticipanteDAO participanteDAO;
    
    public ParticipanteController() {
        this.participanteDAO = new ParticipanteDAO();
    }
    
    /**
     * Register a new participant
     * @param nombre Participant's first name
     * @param apellido Participant's last name
     * @param dni Participant's DNI
     * @param edad Participant's age
     * @param sexo Participant's gender
     * @param direccion Participant's address
     * @param telefono Participant's phone number
     * @param correo Participant's email
     * @return true if successful, false otherwise
     */
    public boolean registrarParticipante(String nombre, String apellido, String dni, int edad, String sexo, String direccion, String telefono, String correo) {
        String sql = "INSERT INTO Participante (nombre, apellido, dni, edad, sexo, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, dni);
            pstmt.setInt(4, edad);
            pstmt.setString(5, sexo);
            pstmt.setString(6, direccion);
            pstmt.setString(7, telefono);
            pstmt.setString(8, correo);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Update an existing participant
     * @param idParticipante Participant's ID
     * @param nombre Participant's first name
     * @param apellido Participant's last name
     * @param dni Participant's DNI
     * @param edad Participant's age
     * @param sexo Participant's gender
     * @param direccion Participant's address
     * @return true if successful, false otherwise
     */
    public boolean actualizarParticipante(int idParticipante, String nombre, String apellido, String dni, int edad, String sexo, String direccion) {
        // Check if participant exists
        Participante participante = participanteDAO.findById(idParticipante);
        if (participante == null) {
            return false; // Participant doesn't exist
        }
        
        // Check if DNI is being changed and if the new DNI is already in use
        if (!dni.equals(participante.getDni()) && participanteDAO.findByDni(dni) != null) {
            return false; // New DNI is already in use
        }
        
        participante.setNombre(nombre);
        participante.setApellido(apellido);
        participante.setDni(dni);
        participante.setEdad(edad);
        participante.setSexo(sexo);
        participante.setDireccion(direccion);
        
        return participanteDAO.update(participante);
    }
    
    /**
     * Delete a participant
     * @param idParticipante Participant's ID
     * @return true if successful, false otherwise
     */
    public boolean eliminarParticipante(int idParticipante) {
        return participanteDAO.delete(idParticipante);
    }
    
    /**
     * Get a participant by ID
     * @param idParticipante Participant's ID
     * @return The participant if found, null otherwise
     */
    public Participante obtenerParticipantePorId(int idParticipante) {
        return participanteDAO.findById(idParticipante);
    }
    
    /**
     * Get a participant by DNI
     * @param dni Participant's DNI
     * @return The participant if found, null otherwise
     */
    public Participante obtenerParticipantePorDni(String dni) {
        return participanteDAO.findByDni(dni);
    }
    
    /**
     * Get all participants
     * @return A list of all participants
     */
    public List<Participante> listarParticipantes() {
        return participanteDAO.findAll();
    }
    
    /**
     * Search participants by name
     * @param nombre Name to search for
     * @return A list of matching participants
     */
    public List<Participante> buscarParticipantesPorNombre(String nombre) {
        return participanteDAO.findByName(nombre);
    }
    /**
    * Busca un participante por su DNI
    * @param dni El DNI del participante a buscar
    * @return El participante si se encuentra, null si no existe
    */
   public Participante buscarPorDni(String dni) {
       return participanteDAO.findByDni(dni);
   }
} 