package dao;

import model.Usuario;
import model.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final DatabaseConnection conexion;
    
    public UsuarioDAO() {
        conexion = new DatabaseConnection();
    }
    
    public List<Usuario> listarTodos() throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE activo = 1";
        
        try (Connection conn = conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setDni(rs.getString("dni"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setRol(rs.getString("rol"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                usuario.setUltimoAcceso(rs.getTimestamp("ultimoAcceso"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuarios.add(usuario);
            }
        }
        
        return usuarios;
    }
    
    public Usuario buscarPorId(int idUsuario) throws Exception {
        String sql = "SELECT * FROM Usuario WHERE idUsuario = ? AND activo = 1";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setDni(rs.getString("dni"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setRol(rs.getString("rol"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                usuario.setUltimoAcceso(rs.getTimestamp("ultimoAcceso"));
                usuario.setActivo(rs.getBoolean("activo"));
                return usuario;
            }
        }
        
        return null;
    }
    
    public Usuario buscarPorDNI(String dni) throws Exception {
        String sql = "SELECT * FROM Usuario WHERE dni = ? AND activo = 1";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setDni(rs.getString("dni"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setRol(rs.getString("rol"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                usuario.setUltimoAcceso(rs.getTimestamp("ultimoAcceso"));
                usuario.setActivo(rs.getBoolean("activo"));
                return usuario;
            }
        }
        
        return null;
    }
    
    public List<Usuario> buscarPorDNIParcial(String dni) throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE dni LIKE ? AND activo = 1";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + dni + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setDni(rs.getString("dni"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setRol(rs.getString("rol"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                usuario.setUltimoAcceso(rs.getTimestamp("ultimoAcceso"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuarios.add(usuario);
            }
        }
        
        return usuarios;
    }
    
    public boolean insertar(Usuario usuario) throws Exception {
        String sql = "INSERT INTO Usuario (nombre, apellido, dni, correo, rol, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getDni());
            pstmt.setString(4, usuario.getCorreo());
            pstmt.setString(5, usuario.getRol());
            pstmt.setString(6, usuario.getContrasena());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(Usuario usuario) throws Exception {
        String sql = "UPDATE Usuario SET nombre = ?, apellido = ?, dni = ?, correo = ?, rol = ? WHERE idUsuario = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getDni());
            pstmt.setString(4, usuario.getCorreo());
            pstmt.setString(5, usuario.getRol());
            pstmt.setInt(6, usuario.getIdUsuario());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(int idUsuario) throws Exception {
        String sql = "UPDATE Usuario SET activo = 0 WHERE idUsuario = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public void actualizarUltimoAcceso(int idUsuario) throws Exception {
        String sql = "UPDATE Usuario SET ultimoAcceso = GETDATE() WHERE idUsuario = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        }
    }
} 