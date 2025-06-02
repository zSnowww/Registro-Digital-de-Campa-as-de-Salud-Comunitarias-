package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import model.database.DatabaseConnection;

/**
 * Data Access Object for Usuario entity
 */
public class UsuarioDAO {
    private Connection conn;
    
    public UsuarioDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }
    
    /**
     * Insert a new user into the database
     * @param usuario The user to insert
     * @return true if successful, false otherwise
     */
    public boolean insert(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nombre, apellido, correo, dni, rol, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getDni());
            stmt.setString(5, usuario.getRol());
            stmt.setString(6, usuario.getContrasena());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing user in the database
     * @param usuario The user to update
     * @return true if successful, false otherwise
     */
    public boolean update(Usuario usuario) {
        String sql = "UPDATE Usuario SET nombre = ?, apellido = ?, correo = ?, dni = ?, rol = ?, contrasena = ? WHERE idUsuario = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getDni());
            stmt.setString(5, usuario.getRol());
            stmt.setString(6, usuario.getContrasena());
            stmt.setInt(7, usuario.getIdUsuario());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a user from the database
     * @param idUsuario The ID of the user to delete
     * @return true if successful, false otherwise
     */
    public boolean delete(int idUsuario) {
        String sql = "DELETE FROM Usuario WHERE idUsuario = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find a user by their ID
     * @param idUsuario The ID of the user to find
     * @return The user if found, null otherwise
     */
    public Usuario findById(int idUsuario) {
        String sql = "SELECT * FROM Usuario WHERE idUsuario = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Find a user by their DNI
     * @param dni The DNI of the user to find
     * @return The user if found, null otherwise
     */
    public Usuario findByDni(String dni) {
        String sql = "SELECT * FROM Usuario WHERE dni = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by DNI: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Authenticate a user with DNI and password
     * @param dni The DNI of the user
     * @param contrasena The password of the user
     * @return The user if authenticated, null otherwise
     */
    public Usuario authenticate(String dni, String contrasena) {
        String sql = "SELECT * FROM Usuario WHERE dni = ? AND contrasena = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            stmt.setString(2, contrasena);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = extractUserFromResultSet(rs);
                // Actualizar último login
                updateLastLogin(usuario.getIdUsuario());
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update the last login timestamp for a user
     * @param idUsuario User's ID
     * @return true if successful, false otherwise
     */
    public boolean updateLastLogin(int idUsuario) {
        String sql = "UPDATE Usuario SET UltimoAcceso = GETDATE() WHERE idUsuario = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all users from the database
     * @return A list of all users
     */
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
        }
        return usuarios;
    }
    
    /**
     * Extract a user from a result set
     * @param rs The result set to extract from
     * @return The extracted user
     * @throws SQLException If an error occurs
     */
    private Usuario extractUserFromResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setDni(rs.getString("dni"));
        usuario.setRol(rs.getString("rol"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setUltimoAcceso(rs.getTimestamp("UltimoAcceso"));
        return usuario;
    }
} 