package model;

import java.util.Date;

/**
 * Class representing the record of participation in activities
 */
public class RegistroParticipacion {
    private int idRegistro;
    private int idParticipante;
    private int idActividad;
    private String resultado;
    private String observaciones;
    private Date fechaRegistro;
    private Participante participante; // Referencia al participante
    
    // Constructor
    public RegistroParticipacion() {
        this.fechaRegistro = new Date(); // Fecha actual por defecto
    }
    
    public RegistroParticipacion(int idRegistro, int idParticipante, int idActividad, String resultado, String observaciones) {
        this.idRegistro = idRegistro;
        this.idParticipante = idParticipante;
        this.idActividad = idActividad;
        this.resultado = resultado;
        this.observaciones = observaciones;
        this.fechaRegistro = new Date(); // Fecha actual por defecto
    }
    
    // Getters and Setters
    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }
} 