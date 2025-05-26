package model;

import java.util.Date;

/**
 * Clase que representa las predicciones de participación en campañas
 */
public class Prediccion {
    private int idPrediccion;
    private int idCampana;
    private Date fechaPrediccion;
    private int participacionEstimada;
    private double nivelConfianza;
    private String notas;
    
    // Constructores
    public Prediccion() {
    }
    
    public Prediccion(int idPrediccion, int idCampana, Date fechaPrediccion, int participacionEstimada, double nivelConfianza, String notas) {
        this.idPrediccion = idPrediccion;
        this.idCampana = idCampana;
        this.fechaPrediccion = fechaPrediccion;
        this.participacionEstimada = participacionEstimada;
        this.nivelConfianza = nivelConfianza;
        this.notas = notas;
    }
    
    // Métodos getter y setter
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

    public double getNivelConfianza() {
        return nivelConfianza;
    }

    public void setNivelConfianza(double nivelConfianza) {
        this.nivelConfianza = nivelConfianza;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
} 