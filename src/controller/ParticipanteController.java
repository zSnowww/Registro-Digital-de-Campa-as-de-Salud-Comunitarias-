package controller;

import java.util.List;
import model.Participante;
import model.dao.ParticipanteDAO;

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
     * @return true if successful, false otherwise
     */
    public boolean registrarParticipante(String nombre, String apellido, String dni, int edad, String sexo, String direccion) {
        // Check if participant with the same DNI already exists
        if (participanteDAO.findByDni(dni) != null) {
            return false; // Participant already exists
        }
        
        Participante participante = new Participante();
        participante.setNombre(nombre);
        participante.setApellido(apellido);
        participante.setDni(dni);
        participante.setEdad(edad);
        participante.setSexo(sexo);
        participante.setDireccion(direccion);
        
        return participanteDAO.insert(participante);
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
} 