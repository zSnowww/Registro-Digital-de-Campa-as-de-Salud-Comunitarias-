package model;

import java.util.Date;

/**
 * Class representing an Activity
 */
public class Actividad {
    private int idActividad;
    private int idCampana;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private String hora;
    private int capacidad;
    private int participantesRegistrados;
    private String estado;
    
    public Actividad() {
    }
    
    public Actividad(int idActividad, int idCampana, String nombre, String descripcion, Date fecha, 
                    String hora, int capacidad, int participantesRegistrados, String estado) {
        this.idActividad = idActividad;
        this.idCampana = idCampana;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.capacidad = capacidad;
        this.participantesRegistrados = participantesRegistrados;
        this.estado = estado;
    }
    
    public int getIdActividad() {return idActividad;}
    
    public void setIdActividad(int idActividad) {this.idActividad = idActividad;}
    
    public int getIdCampana() {return idCampana;}
    
    public void setIdCampana(int idCampana) {this.idCampana = idCampana;}
    
    public String getNombre() {return nombre;}
    
    public void setNombre(String nombre) {this.nombre = nombre;}
    
    public String getDescripcion() {return descripcion;}
    
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    
    public Date getFecha() {return fecha;}
    
    public void setFecha(Date fecha) {this.fecha = fecha;}
    
    public String getHora() {return hora;}
    
    public void setHora(String hora) {this.hora = hora;}

    public int getCapacidad() {return capacidad;}    
    
    public void setCapacidad(int capacidad) {this.capacidad = capacidad;}
    
    
    public int getParticipantesRegistrados() {return participantesRegistrados;}
    
    public void setParticipantesRegistrados(int participantesRegistrados) {this.participantesRegistrados = participantesRegistrados;}
    
    public String getEstado() {return estado;}
    
    public void setEstado(String estado) {this.estado = estado;}
    
    // Alias methods for cupoMaximo (maps to capacidad)
    public int getCupoMaximo() {
        return capacidad;
    }
    
    public void setCupoMaximo(int cupoMaximo) {
        this.capacidad = cupoMaximo;
    }
    
    public double getPorcentajeCompletitud() {return (participantesRegistrados * 100.0) / capacidad;}
    
    @Override
    public String toString() {
        return "Actividad{" +
                "idActividad=" + idActividad +
                ", idCampana=" + idCampana +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", hora='" + hora + '\'' +
                ", capacidad=" + capacidad +
                ", participantesRegistrados=" + participantesRegistrados +
                ", estado='" + estado + '\'' +
                '}';
    }
} 