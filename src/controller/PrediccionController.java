package controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import model.Campana;
import model.Prediccion;
import model.dao.CampanaDAO;
import model.dao.PrediccionDAO;
import model.dao.RegistroParticipacionDAO;

/**
 * Controlador para manejar la lógica de negocio de Prediccion y algoritmos de predicción
 */
public class PrediccionController {
    private final PrediccionDAO prediccionDAO;
    private final CampanaDAO campanaDAO;
    private final RegistroParticipacionDAO registroDAO;
    
    public PrediccionController() {
        try {
            this.prediccionDAO = new PrediccionDAO();
            this.campanaDAO = new CampanaDAO();
            this.registroDAO = new RegistroParticipacionDAO();
        } catch (SQLException e) {
            System.err.println("Error initializing DAOs: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }
    
    /**
     * Genera una predicción para una campaña
     * @param idCampana ID de la campaña
     * @param notas Notas sobre la predicción
     * @return true si es exitoso, false en caso contrario
     */
    public boolean generarPrediccion(int idCampana, String notas) {
        // Verificar si la campaña existe
        Campana campana = campanaDAO.findById(idCampana);
        if (campana == null) {
            return false; // La campaña no existe
        }
        
        // Calcular participación estimada basada en datos históricos
        int participacionEstimada = calcularParticipacionEstimada(idCampana);
        
        // Calcular nivel de confianza basado en consistencia de datos históricos
        double nivelConfianza = calcularNivelConfianza(idCampana);
        
        // Crear nueva predicción
        Prediccion prediccion = new Prediccion();
        prediccion.setIdCampana(idCampana);
        prediccion.setFechaPrediccion(new Date());
        prediccion.setParticipacionEstimada(participacionEstimada);
        prediccion.setNivelConfianza(nivelConfianza);
        prediccion.setNotas(notas);
        
        // Check if prediction already exists for the campaign
        Prediccion prediccionExistente = prediccionDAO.findByCampana(idCampana);
        if (prediccionExistente != null) {
            // Update existing prediction
            prediccionExistente.setFechaPrediccion(new Date());
            prediccionExistente.setParticipacionEstimada(participacionEstimada);
            prediccionExistente.setNivelConfianza(nivelConfianza);
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

        // Recalculate confidence level when updating manually
        double nivelConfianza = calcularNivelConfianza(prediccion.getIdCampana());

        prediccion.setFechaPrediccion(new Date());
        prediccion.setParticipacionEstimada(participacionEstimada);
        prediccion.setNivelConfianza(nivelConfianza);
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
    
    /**
     * Calculate confidence level for a prediction based on historical data consistency
     * @param idCampana Campaign ID
     * @return Confidence level as a percentage (0-100)
     */
    private double calcularNivelConfianza(int idCampana) {
        // Get campaign information
        Campana campanaNueva = campanaDAO.findById(idCampana);
        if (campanaNueva == null) {
            return 50.0; // Default confidence for unknown campaigns
        }
        
        // Get campaigns by the same responsible
        List<Campana> campanasAnteriores = campanaDAO.findByResponsable(campanaNueva.getIdResponsable());
        
        // If this is the first campaign, confidence is moderate
        if (campanasAnteriores.size() <= 1) {
            return 60.0; // Moderate confidence for first campaign
        }
        
        // Calculate consistency of historical data
        double baseConfidence = 70.0; // Base confidence level
        
        // Factors that increase confidence:
        // 1. More historical campaigns = higher confidence
        int numCampanasHistoricas = campanasAnteriores.size() - 1; // Exclude current campaign
        double factorExperiencia = Math.min(numCampanasHistoricas * 5.0, 20.0); // Max 20% boost
        
        // 2. Recent campaigns = higher confidence (simulate recency factor)
        double factorRecencia = 5.0; // 5% boost for recent data
        
        // 3. Consistency factor (simulate based on campaign ID for demo)
        // In real implementation, this would analyze variance in historical participation
        double factorConsistencia = 100.0 - (idCampana % 30); // Simulate variance (0-30% reduction)
        factorConsistencia = Math.max(factorConsistencia, 70.0); // Minimum 70% consistency
        factorConsistencia = (factorConsistencia - 70.0) / 3.0; // Convert to 0-10% boost
        
        // Calculate final confidence level
        double nivelConfianza = baseConfidence + factorExperiencia + factorRecencia + factorConsistencia;
        
        // Ensure confidence is within valid range (0-100)
        nivelConfianza = Math.max(0.0, Math.min(100.0, nivelConfianza));
        
        // Round to 2 decimal places
        return Math.round(nivelConfianza * 100.0) / 100.0;
    }
} 