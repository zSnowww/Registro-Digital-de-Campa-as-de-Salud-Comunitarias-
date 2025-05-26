package model;

import java.util.Date;

/**
 * Class representing an Activity
 */
public class Actividad {
    private int idActividad;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private String hora;
    private int idCampana;
    private int cupoMaximo;
    private int participantesRegistrados;
    private String estado;
    
    public Actividad() {
    }
    
    public Actividad(int idActividad, String nombre, String descripcion, Date fecha, String hora, int idCampana, int cupoMaximo, int participantesRegistrados, String estado) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.idCampana = idCampana;
        this.cupoMaximo = cupoMaximo;
        this.participantesRegistrados = participantesRegistrados;
        this.estado = estado;
    }
    
    public int getIdActividad() {
        return idActividad;
    }
    
    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getHora() {
        return hora;
    }
    
    public void setHora(String hora) {
        this.hora = hora;
    }
    
    public int getIdCampana() {
        return idCampana;
    }
    
    public void setIdCampana(int idCampana) {
        this.idCampana = idCampana;
    }
    
    public int getCupoMaximo() {
        return cupoMaximo;
    }
    
    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
    
    public int getParticipantesRegistrados() {
        return participantesRegistrados;
    }
    
    public void setParticipantesRegistrados(int participantesRegistrados) {
        this.participantesRegistrados = participantesRegistrados;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Actividad{" +
                "idActividad=" + idActividad +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", hora='" + hora + '\'' +
                ", idCampana=" + idCampana +
                ", cupoMaximo=" + cupoMaximo +
                ", participantesRegistrados=" + participantesRegistrados +
                ", estado='" + estado + '\'' +
                '}';
    }
} 