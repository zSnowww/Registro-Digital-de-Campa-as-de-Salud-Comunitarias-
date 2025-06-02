package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import model.dao.UsuarioDAO;

/**
 * Controller class for handling Usuario business logic
 */
public class UsuarioController {
    private final UsuarioDAO usuarioDAO;
    
    public UsuarioController() {
        try {
            usuarioDAO = new UsuarioDAO();
        } catch (SQLException e) {
            System.err.println("Error initializing UsuarioDAO: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database connection", e);
        }
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
    public boolean registrarUsuario(String nombre, String apellido, String correo, String dni, String rol, String contrasena) throws Exception {
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
    public Usuario autenticarUsuario(String dni, String contrasena) throws Exception {
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
    public boolean actualizarUsuario(int idUsuario, String nombre, String apellido, String correo, String dni, String rol, String contrasena) throws Exception {
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
    public boolean eliminarUsuario(int idUsuario) throws Exception {
        return usuarioDAO.delete(idUsuario);
    }
    
    /**
     * Get a user by ID
     * @param idUsuario User's ID
     * @return The user if found, null otherwise
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) throws Exception {
        return usuarioDAO.findById(idUsuario);
    }
    
    /**
     * Get a user by DNI
     * @param dni User's DNI
     * @return The user if found, null otherwise
     */
    public Usuario obtenerUsuarioPorDni(String dni) throws Exception {
        return usuarioDAO.findByDni(dni);
    }
    
    /**
     * Get all users
     * @return A list of all users
     */
    public List<Usuario> listarUsuarios() throws Exception {
        return usuarioDAO.findAll();
    }
    
    /**
     * Search users by partial DNI
     * @param dni Partial DNI to search for
     * @return List of users matching the search
     */
    public List<Usuario> buscarUsuariosPorDNI(String dni) throws Exception {
        // Since findByDni doesn't support partial search, we'll implement a simple version
        List<Usuario> allUsers = usuarioDAO.findAll();
        List<Usuario> matchingUsers = new ArrayList<>();
        for (Usuario user : allUsers) {
            if (user.getDni().contains(dni)) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }
    
    /**
     * Register a new user
     * @param usuario User object to register
     * @return true if successful, false otherwise
     */
    public boolean registrarUsuario(Usuario usuario) throws Exception {
        return usuarioDAO.insert(usuario);
    }
    
    /**
     * Update a user
     * @param usuario User object to update
     * @return true if successful, false otherwise
     */
    public boolean actualizarUsuario(Usuario usuario) throws Exception {
        return usuarioDAO.update(usuario);
    }
    
    /**
     * Validate user credentials
     * @param dni User's DNI
     * @param contrasena User's password
     * @return true if credentials are valid, false otherwise
     */
    public boolean validarCredenciales(String dni, String contrasena) throws Exception {
        Usuario usuario = usuarioDAO.authenticate(dni, contrasena);
        return usuario != null;
    }
    
    /**
     * Update user's last access timestamp
     * @param idUsuario User's ID
     */
    public void actualizarUltimoAcceso(int idUsuario) throws Exception {
        usuarioDAO.updateLastLogin(idUsuario);
    }
    
    /**
     * Search for a user by DNI (alias method)
     * @param dni User's DNI
     * @return The user if found, null otherwise
     */
    public Usuario buscarPorDNI(String dni) throws Exception {
        return usuarioDAO.findByDni(dni);
    }
    
    /**
     * Search for a user by ID (alias method)
     * @param idUsuario User's ID
     * @return The user if found, null otherwise
     */
    public Usuario buscarPorId(int idUsuario) throws Exception {
        return usuarioDAO.findById(idUsuario);
    }
} 