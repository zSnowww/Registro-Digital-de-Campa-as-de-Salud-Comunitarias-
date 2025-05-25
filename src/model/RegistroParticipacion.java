package model;

/**
 * Class representing the record of participation in activities
 */
public class RegistroParticipacion {
    private int idRegistro;
    private int idParticipante;
    private int idActividad;
    private String resultado;
    private String observaciones;
    
    // Constructor
    public RegistroParticipacion() {
    }
    
    public RegistroParticipacion(int idRegistro, int idParticipante, int idActividad, String resultado, String observaciones) {
        this.idRegistro = idRegistro;
        this.idParticipante = idParticipante;
        this.idActividad = idActividad;
        this.resultado = resultado;
        this.observaciones = observaciones;
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
} 