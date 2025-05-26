package view;

import controller.UsuarioController;
import model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroUsuario extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDNI;
    private JTextField txtCorreo;
    private JComboBox<String> cboRol;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    
    private final UsuarioController usuarioController;
    
    public RegistroUsuario() {
        usuarioController = new UsuarioController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);
        
        // Apellido
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);
        
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        panel.add(txtApellido, gbc);
        
        // DNI
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("DNI:"), gbc);
        
        gbc.gridx = 1;
        txtDNI = new JTextField(20);
        panel.add(txtDNI, gbc);
        
        // Correo
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Correo:"), gbc);
        
        gbc.gridx = 1;
        txtCorreo = new JTextField(20);
        panel.add(txtCorreo, gbc);
        
        // Rol
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Rol:"), gbc);
        
        gbc.gridx = 1;
        String[] roles = {"Administrador", "Usuario"};
        cboRol = new JComboBox<>(roles);
        panel.add(cboRol, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtContrasena = new JPasswordField(20);
        panel.add(txtContrasena, gbc);
        
        // Confirmar Contraseña
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Confirmar Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtConfirmarContrasena = new JPasswordField(20);
        panel.add(txtConfirmarContrasena, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
        
        add(panel);
    }
    
    private void registrarUsuario() {
        // Validar campos
        if (txtNombre.getText().trim().isEmpty() ||
            txtApellido.getText().trim().isEmpty() ||
            txtDNI.getText().trim().isEmpty() ||
            txtCorreo.getText().trim().isEmpty() ||
            txtContrasena.getPassword().length == 0 ||
            txtConfirmarContrasena.getPassword().length == 0) {
            
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar contraseñas
        String contrasena = new String(txtContrasena.getPassword());
        String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
        
        if (!contrasena.equals(confirmarContrasena)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(txtNombre.getText().trim());
            usuario.setApellido(txtApellido.getText().trim());
            usuario.setDni(txtDNI.getText().trim());
            usuario.setCorreo(txtCorreo.getText().trim());
            usuario.setRol(cboRol.getSelectedItem().toString());
            usuario.setContrasena(contrasena);
            
            // Registrar usuario
            if (usuarioController.registrarUsuario(usuario)) {
                JOptionPane.showMessageDialog(this,
                    "Usuario registrado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al registrar el usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistroUsuario().setVisible(true);
            }
        });
    }
} 