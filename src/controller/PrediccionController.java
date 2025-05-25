package controller;

import java.util.Date;
import java.util.List;
import model.Campana;
import model.Prediccion;
import model.dao.CampanaDAO;
import model.dao.PrediccionDAO;
import model.dao.RegistroParticipacionDAO;

/**
 * Controller class for handling Prediccion business logic and prediction algorithms
 */
public class PrediccionController {
    private final PrediccionDAO prediccionDAO;
    private final CampanaDAO campanaDAO;
    private final RegistroParticipacionDAO registroDAO;
    
    public PrediccionController() {
        this.prediccionDAO = new PrediccionDAO();
        this.campanaDAO = new CampanaDAO();
        this.registroDAO = new RegistroParticipacionDAO();
    }
    
    /**
     * Generate a prediction for a campaign
     * @param idCampana Campaign ID
     * @param notas Notes about the prediction
     * @return true if successful, false otherwise
     */
    public boolean generarPrediccion(int idCampana, String notas) {
        // Check if campaign exists
        Campana campana = campanaDAO.findById(idCampana);
        if (campana == null) {
            return false; // Campaign doesn't exist
        }
        
        // Calculate estimated participation based on historical data
        int participacionEstimada = calcularParticipacionEstimada(idCampana);
        
        // Create new prediction
        Prediccion prediccion = new Prediccion();
        prediccion.setIdCampana(idCampana);
        prediccion.setFechaPrediccion(new Date());
        prediccion.setParticipacionEstimada(participacionEstimada);
        prediccion.setNotas(notas);
        
        // Check if prediction already exists for the campaign
        Prediccion prediccionExistente = prediccionDAO.findByCampana(idCampana);
        if (prediccionExistente != null) {
            // Update existing prediction
            prediccionExistente.setFechaPrediccion(new Date());
            prediccionExistente.setParticipacionEstimada(participacionEstimada);
            prediccionExistente.setNotas(notas);
            return prediccionDAO.update(prediccionExistente);
        } else {
            // Insert new prediction
            return prediccionDAO.insert(prediccion);
        }
    }
    
    /**
     * Update an existing prediction
     * @param idPrediccion Prediction ID
     * @param participacionEstimada Estimated participation
     * @param notas Notes about the prediction
     * @return true if successful, false otherwise
     */
    public boolean actualizarPrediccion(int idPrediccion, int participacionEstimada, String notas) {
        // Check if prediction exists
        Prediccion prediccion = prediccionDAO.findById(idPrediccion);
        if (prediccion == null) {
            return false; // Prediction doesn't exist
        }
        
        prediccion.setFechaPrediccion(new Date());
        prediccion.setParticipacionEstimada(participacionEstimada);
        prediccion.setNotas(notas);
        
        return prediccionDAO.update(prediccion);
    }
    
    /**
     * Delete a prediction
     * @param idPrediccion Prediction ID
     * @return true if successful, false otherwise
     */
    public boolean eliminarPrediccion(int idPrediccion) {
        return prediccionDAO.delete(idPrediccion);
    }
    
    /**
     * Get a prediction by ID
     * @param idPrediccion Prediction ID
     * @return The prediction if found, null otherwise
     */
    public Prediccion obtenerPrediccionPorId(int idPrediccion) {
        return prediccionDAO.findById(idPrediccion);
    }
    
    /**
     * Get a prediction by campaign ID
     * @param idCampana Campaign ID
     * @return The prediction if found, null otherwise
     */
    public Prediccion obtenerPrediccionPorCampana(int idCampana) {
        return prediccionDAO.findByCampana(idCampana);
    }
    
    /**
     * Get all predictions
     * @return A list of all predictions
     */
    public List<Prediccion> listarPredicciones() {
        return prediccionDAO.findAll();
    }
    
    /**
     * Calculate estimated participation for a campaign based on historical data
     * @param idCampana Campaign ID
     * @return Estimated participation count
     */
    private int calcularParticipacionEstimada(int idCampana) {
        // Get all campaigns by the same responsible
        Campana campanaNueva = campanaDAO.findById(idCampana);
        if (campanaNueva == null) {
            return 0;
        }
        
        List<Campana> campanasAnteriores = campanaDAO.findByResponsable(campanaNueva.getIdResponsable());
        
        // If this is the first campaign, use a default value
        if (campanasAnteriores.size() <= 1) {
            return 50; // Default value for first campaign
        }
        
        // Calculate average participation from previous campaigns
        int totalParticipacion = 0;
        int contadorCampanas = 0;
        
        for (Campana campana : campanasAnteriores) {
            // Skip current campaign
            if (campana.getIdCampana() == idCampana) {
                continue;
            }
            
            // Count participation in activities of this campaign
            int participacionCampana = contarParticipacionCampana(campana.getIdCampana());
            if (participacionCampana > 0) {
                totalParticipacion += participacionCampana;
                contadorCampanas++;
            }
        }
        
        // Calculate average and apply a growth factor
        if (contadorCampanas > 0) {
            double promedio = (double) totalParticipacion / contadorCampanas;
            // Apply a 10% growth factor
            return (int) Math.round(promedio * 1.1);
        } else {
            return 50; // Default value if no historical data
        }
    }
    
    /**
     * Count total participation in a campaign
     * @param idCampana Campaign ID
     * @return Total participation count
     */
    private int contarParticipacionCampana(int idCampana) {
        // This would involve getting all activities in the campaign and summing their participants
        // For simplicity, we'll simulate this with a basic algorithm
        return idCampana * 10; // Simplified formula for demo purposes
    }
} 