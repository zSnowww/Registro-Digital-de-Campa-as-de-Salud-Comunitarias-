package controller;

import java.util.Date;
import java.util.List;
import model.Campana;
import model.dao.CampanaDAO;

/**
 * Controller class for handling Campana business logic
 */
public class CampanaController {
    private final CampanaDAO campanaDAO;
    
    public CampanaController() {
        this.campanaDAO = new CampanaDAO();
    }
    
    /**
     * Register a new campaign
     * @param nombre Campaign name
     * @param descripcion Campaign description
     * @param fechaInicio Start date
     * @param fechaFin End date
     * @param idResponsable Responsible user ID
     * @return true if successful, false otherwise
     */
    public boolean registrarCampana(String nombre, String descripcion, Date fechaInicio, Date fechaFin, int idResponsable) {
        // Basic validation
        if (fechaInicio.after(fechaFin)) {
            return false; // Start date can't be after end date
        }
        
        Campana campana = new Campana();
        campana.setNombre(nombre);
        campana.setDescripcion(descripcion);
        campana.setFechaInicio(fechaInicio);
        campana.setFechaFin(fechaFin);
        campana.setIdResponsable(idResponsable);
        
        return campanaDAO.insert(campana);
    }
    
    /**
     * Update an existing campaign
     * @param idCampana Campaign ID
     * @param nombre Campaign name
     * @param descripcion Campaign description
     * @param fechaInicio Start date
     * @param fechaFin End date
     * @param idResponsable Responsible user ID
     * @return true if successful, false otherwise
     */
    public boolean actualizarCampana(int idCampana, String nombre, String descripcion, Date fechaInicio, Date fechaFin, int idResponsable) {
        // Check if campaign exists
        Campana campana = campanaDAO.findById(idCampana);
        if (campana == null) {
            return false; // Campaign doesn't exist
        }
        
        // Basic validation
        if (fechaInicio.after(fechaFin)) {
            return false; // Start date can't be after end date
        }
        
        campana.setNombre(nombre);
        campana.setDescripcion(descripcion);
        campana.setFechaInicio(fechaInicio);
        campana.setFechaFin(fechaFin);
        campana.setIdResponsable(idResponsable);
        
        return campanaDAO.update(campana);
    }
    
    /**
     * Delete a campaign
     * @param idCampana Campaign ID
     * @return true if successful, false otherwise
     */
    public boolean eliminarCampana(int idCampana) {
        return campanaDAO.delete(idCampana);
    }
    
    /**
     * Get a campaign by ID
     * @param idCampana Campaign ID
     * @return The campaign if found, null otherwise
     */
    public Campana obtenerCampanaPorId(int idCampana) {
        return campanaDAO.findById(idCampana);
    }
    
    /**
     * Get all campaigns
     * @return A list of all campaigns
     */
    public List<Campana> listarCampanas() {
        return campanaDAO.findAll();
    }
    
    /**
     * Get campaigns by responsible user
     * @param idResponsable Responsible user ID
     * @return A list of campaigns by the user
     */
    public List<Campana> listarCampanasPorResponsable(int idResponsable) {
        return campanaDAO.findByResponsable(idResponsable);
    }
    
    /**
     * Get active campaigns (current date is between start and end dates)
     * @return A list of active campaigns
     */
    public List<Campana> listarCampanasActivas() {
        List<Campana> todasCampanas = campanaDAO.findAll();
        List<Campana> campanasActivas = new java.util.ArrayList<>();
        
        Date fechaActual = new Date();
        
        for (Campana campana : todasCampanas) {
            if ((fechaActual.after(campana.getFechaInicio()) || fechaActual.equals(campana.getFechaInicio())) 
                && (fechaActual.before(campana.getFechaFin()) || fechaActual.equals(campana.getFechaFin()))) {
                campanasActivas.add(campana);
            }
        }
        
        return campanasActivas;
    }
} 