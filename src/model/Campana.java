package model;

import java.util.Date;

/**
 * Class representing a health campaign
 */
public class Campana {
    private int idCampana;
    private String nombre;
    private String categoria;
    private Date fechaInicio;
    private Date fechaFin;
    private String descripcion;
    private String estado;
    private int idResponsable;
    
    // Constructor
    public Campana() {
    }
    
    public Campana(int idCampana, String nombre, String categoria, Date fechaInicio, Date fechaFin, String descripcion, String estado) {
        this.idCampana = idCampana;
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.estado = estado;
    }
    
    public Campana(int idCampana, String nombre, String categoria, Date fechaInicio, Date fechaFin, String descripcion, String estado, int idResponsable) {
        this.idCampana = idCampana;
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idResponsable = idResponsable;
    }
    
    // Getters
    public int getIdCampana() { return idCampana; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public Date getFechaInicio() { return fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public int getIdResponsable() { return idResponsable; }
    
    // Setters
    public void setIdCampana(int idCampana) { this.idCampana = idCampana; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setIdResponsable(int idResponsable) { this.idResponsable = idResponsable; }
    
    @Override
    public String toString() {
        return nombre;
    }
} 