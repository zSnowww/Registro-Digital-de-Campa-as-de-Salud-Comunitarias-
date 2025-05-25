package controller;

import java.util.Date;
import java.util.List;
import model.Actividad;
import model.dao.ActividadDAO;

/**
 * Controller class for handling Actividad business logic
 */
public class ActividadController {
    private final ActividadDAO actividadDAO;
    
    public ActividadController() {
        this.actividadDAO = new ActividadDAO();
    }
    
    /**
     * Register a new activity
     * @param nombre Activity name
     * @param descripcion Activity description
     * @param fecha Activity date
     * @param hora Activity time
     * @param idCampana Campaign ID
     * @return true if successful, false otherwise
     */
    public boolean registrarActividad(String nombre, String descripcion, Date fecha, String hora, int idCampana) {
        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setFecha(fecha);
        actividad.setHora(hora);
        actividad.setIdCampana(idCampana);
        
        return actividadDAO.insert(actividad);
    }
    
    /**
     * Update an existing activity
     * @param idActividad Activity ID
     * @param nombre Activity name
     * @param descripcion Activity description
     * @param fecha Activity date
     * @param hora Activity time
     * @param idCampana Campaign ID
     * @return true if successful, false otherwise
     */
    public boolean actualizarActividad(int idActividad, String nombre, String descripcion, Date fecha, String hora, int idCampana) {
        // Check if activity exists
        Actividad actividad = actividadDAO.findById(idActividad);
        if (actividad == null) {
            return false; // Activity doesn't exist
        }
        
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setFecha(fecha);
        actividad.setHora(hora);
        actividad.setIdCampana(idCampana);
        
        return actividadDAO.update(actividad);
    }
    
    /**
     * Delete an activity
     * @param idActividad Activity ID
     * @return true if successful, false otherwise
     */
    public boolean eliminarActividad(int idActividad) {
        return actividadDAO.delete(idActividad);
    }
    
    /**
     * Get an activity by ID
     * @param idActividad Activity ID
     * @return The activity if found, null otherwise
     */
    public Actividad obtenerActividadPorId(int idActividad) {
        return actividadDAO.findById(idActividad);
    }
    
    /**
     * Get all activities
     * @return A list of all activities
     */
    public List<Actividad> listarActividades() {
        return actividadDAO.findAll();
    }
    
    /**
     * Get activities by campaign
     * @param idCampana Campaign ID
     * @return A list of activities for the campaign
     */
    public List<Actividad> listarActividadesPorCampana(int idCampana) {
        return actividadDAO.findByCampana(idCampana);
    }
} 