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
     * @param hora Activity time (format: HH:mm)
     * @param idCampana Campaign ID
     * @param cupoMaximo Maximum capacity
     * @param estado Activity status
     * @return true if successful, false otherwise
     */
    public boolean registrarActividad(String nombre, String descripcion, Date fecha, String hora, int idCampana, int cupoMaximo, String estado) {
        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setFecha(fecha);
        actividad.setHora(hora);
        actividad.setIdCampana(idCampana);
        actividad.setCupoMaximo(cupoMaximo);
        actividad.setEstado(estado);
        actividad.setParticipantesRegistrados(0);
        
        return actividadDAO.insert(actividad);
    }
    
    /**
     * Update an existing activity
     * @param idActividad Activity ID
     * @param nombre Activity name
     * @param descripcion Activity description
     * @param fecha Activity date
     * @param hora Activity time (format: HH:mm)
     * @param idCampana Campaign ID
     * @param cupoMaximo Maximum capacity
     * @param estado Activity status
     * @return true if successful, false otherwise
     */
    public boolean actualizarActividad(int idActividad, String nombre, String descripcion, Date fecha, String hora, int idCampana, int cupoMaximo, String estado) {
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
        actividad.setCupoMaximo(cupoMaximo);
        actividad.setEstado(estado);
        
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
     * Search activities by name
     * @param nombre Name to search for
     * @return A list of matching activities
     */
    public List<Actividad> buscarActividadesPorNombre(String nombre) {
        return actividadDAO.findByName(nombre);
    }
    
    /**
     * Get activities by date range
     * @param fechaInicio Start date
     * @param fechaFin End date
     * @return A list of activities in the date range
     */
    public List<Actividad> buscarActividadesPorRangoFechas(Date fechaInicio, Date fechaFin) {
        return actividadDAO.findByDateRange(fechaInicio, fechaFin);
    }
    
    /**
     * Get activities by status
     * @param estado Status to filter by
     * @return A list of activities with the specified status
     */
    public List<Actividad> buscarActividadesPorEstado(String estado) {
        return actividadDAO.findByStatus(estado);
    }
    
    /**
     * Get activities by campaign
     * @param idCampana Campaign ID
     * @return A list of activities for the campaign
     */
    public List<Actividad> buscarActividadesPorCampana(int idCampana) {
        return actividadDAO.findByCampana(idCampana);
    }
} 