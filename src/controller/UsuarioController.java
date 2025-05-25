package controller;

import java.util.List;
import model.Usuario;
import model.dao.UsuarioDAO;

/**
 * Controller class for handling Usuario business logic
 */
public class UsuarioController {
    private final UsuarioDAO usuarioDAO;
    
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Register a new user
     * @param nombre User's first name
     * @param apellido User's last name
     * @param correo User's email
     * @param dni User's DNI
     * @param rol User's role
     * @param contrasena User's password
     * @return true if successful, false otherwise
     */
    public boolean registrarUsuario(String nombre, String apellido, String correo, String dni, String rol, String contrasena) {
        // Check if user with the same DNI already exists
        if (usuarioDAO.findByDni(dni) != null) {
            return false; // User already exists
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setDni(dni);
        usuario.setRol(rol);
        usuario.setContrasena(contrasena);
        
        return usuarioDAO.insert(usuario);
    }
    
    /**
     * Authenticate a user
     * @param dni User's DNI
     * @param contrasena User's password
     * @return The authenticated user or null if authentication fails
     */
    public Usuario autenticarUsuario(String dni, String contrasena) {
        return usuarioDAO.authenticate(dni, contrasena);
    }
    
    /**
     * Update a user's information
     * @param idUsuario User's ID
     * @param nombre User's first name
     * @param apellido User's last name
     * @param correo User's email
     * @param dni User's DNI
     * @param rol User's role
     * @param contrasena User's password
     * @return true if successful, false otherwise
     */
    public boolean actualizarUsuario(int idUsuario, String nombre, String apellido, String correo, String dni, String rol, String contrasena) {
        Usuario usuario = usuarioDAO.findById(idUsuario);
        if (usuario == null) {
            return false; // User doesn't exist
        }
        
        // Check if DNI is being changed and if the new DNI is already in use
        if (!dni.equals(usuario.getDni()) && usuarioDAO.findByDni(dni) != null) {
            return false; // New DNI is already in use
        }
        
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setDni(dni);
        usuario.setRol(rol);
        
        // Only update password if a new one is provided
        if (contrasena != null && !contrasena.isEmpty()) {
            usuario.setContrasena(contrasena);
        }
        
        return usuarioDAO.update(usuario);
    }
    
    /**
     * Delete a user
     * @param idUsuario User's ID
     * @return true if successful, false otherwise
     */
    public boolean eliminarUsuario(int idUsuario) {
        return usuarioDAO.delete(idUsuario);
    }
    
    /**
     * Get a user by ID
     * @param idUsuario User's ID
     * @return The user if found, null otherwise
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        return usuarioDAO.findById(idUsuario);
    }
    
    /**
     * Get a user by DNI
     * @param dni User's DNI
     * @return The user if found, null otherwise
     */
    public Usuario obtenerUsuarioPorDni(String dni) {
        return usuarioDAO.findByDni(dni);
    }
    
    /**
     * Get all users
     * @return A list of all users
     */
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.findAll();
    }
} 