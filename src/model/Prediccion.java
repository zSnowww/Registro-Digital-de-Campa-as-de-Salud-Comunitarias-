package model;

import java.util.Date;

/**
 * Class representing predictions for campaign participation
 */
public class Prediccion {
    private int idPrediccion;
    private int idCampana;
    private Date fechaPrediccion;
    private int participacionEstimada;
    private String notas;
    
    // Constructor
    public Prediccion() {
    }
    
    public Prediccion(int idPrediccion, int idCampana, Date fechaPrediccion, int participacionEstimada, String notas) {
        this.idPrediccion = idPrediccion;
        this.idCampana = idCampana;
        this.fechaPrediccion = fechaPrediccion;
        this.participacionEstimada = participacionEstimada;
        this.notas = notas;
    }
    
    // Getters and Setters
    public int getIdPrediccion() {
        return idPrediccion;
    }

    public void setIdPrediccion(int idPrediccion) {
        this.idPrediccion = idPrediccion;
    }

    public int getIdCampana() {
        return idCampana;
    }

    public void setIdCampana(int idCampana) {
        this.idCampana = idCampana;
    }

    public Date getFechaPrediccion() {
        return fechaPrediccion;
    }

    public void setFechaPrediccion(Date fechaPrediccion) {
        this.fechaPrediccion = fechaPrediccion;
    }

    public int getParticipacionEstimada() {
        return participacionEstimada;
    }

    public void setParticipacionEstimada(int participacionEstimada) {
        this.participacionEstimada = participacionEstimada;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
} 