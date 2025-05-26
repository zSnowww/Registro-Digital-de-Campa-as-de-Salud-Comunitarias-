package model;

import java.util.Date;

/**
 * Class representing a participant in health campaigns
 */
public class Participante {
    private int id;
    private String dni;
    private String nombres;
    private String apellidos;
    private Date fechaNacimiento;
    private String genero;
    private String direccion;
    private String telefono;
    private String email;
    
    // Constructor
    public Participante() {
    }
    
    public Participante(int id, String dni, String nombres, String apellidos, Date fechaNacimiento, 
                       String genero, String direccion, String telefono, String email) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Alias methods to match controller/DAO naming conventions
    public int getIdParticipante() {
        return id;
    }
    
    public void setIdParticipante(int idParticipante) {
        this.id = idParticipante;
    }
    
    public String getNombre() {
        return nombres;
    }
    
    public void setNombre(String nombre) {
        this.nombres = nombre;
    }
    
    public String getApellido() {
        return apellidos;
    }
    
    public void setApellido(String apellido) {
        this.apellidos = apellido;
    }
    
    public int getEdad() {
        // Calculate age from fechaNacimiento if needed
        if (fechaNacimiento != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int currentYear = cal.get(java.util.Calendar.YEAR);
            cal.setTime(fechaNacimiento);
            int birthYear = cal.get(java.util.Calendar.YEAR);
            return currentYear - birthYear;
        }
        return 0;
    }
    
    public void setEdad(int edad) {
        // Calculate fechaNacimiento from age
        if (edad > 0) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.YEAR, -edad);
            this.fechaNacimiento = cal.getTime();
        }
    }
    
    public String getSexo() {
        return genero;
    }
    
    public void setSexo(String sexo) {
        this.genero = sexo;
    }
    
    public String getCorreo() {
        return email;
    }
    
    public void setCorreo(String correo) {
        this.email = correo;
    }
    
    @Override
    public String toString() {
        return nombres + " " + apellidos;
    }
} 