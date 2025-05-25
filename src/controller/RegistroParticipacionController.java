package controller;

import java.util.List;
import model.RegistroParticipacion;
import model.dao.RegistroParticipacionDAO;

/**
 * Controller class for handling RegistroParticipacion business logic
 */
public class RegistroParticipacionController {
    private final RegistroParticipacionDAO registroDAO;
    
    public RegistroParticipacionController() {
        this.registroDAO = new RegistroParticipacionDAO();
    }
    
    /**
     * Register participation in an activity
     * @param idParticipante Participant ID
     * @param idActividad Activity ID
     * @param resultado Result of the participation
     * @param observaciones Observations
     * @return true if successful, false otherwise
     */
    public boolean registrarParticipacion(int idParticipante, int idActividad, String resultado, String observaciones) {
        RegistroParticipacion registro = new RegistroParticipacion();
        registro.setIdParticipante(idParticipante);
        registro.setIdActividad(idActividad);
        registro.setResultado(resultado);
        registro.setObservaciones(observaciones);
        
        return registroDAO.insert(registro);
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
        return registroDAO.findByActividad(idActividad);
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