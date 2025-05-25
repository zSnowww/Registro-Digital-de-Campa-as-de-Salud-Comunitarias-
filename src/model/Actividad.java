package model;

import java.util.Date;

/**
 * Class representing an activity within a health campaign
 */
public class Actividad {
    private int idActividad;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private String hora;
    private int idCampana;
    
    // Constructor
    public Actividad() {
    }
    
    public Actividad(int idActividad, String nombre, String descripcion, Date fecha, String hora, int idCampana) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.idCampana = idCampana;
    }
    
    // Getters and Setters
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
    
    @Override
    public String toString() {
        return nombre;
    }
} 