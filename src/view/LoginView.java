package view;

import controller.UsuarioController;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginView extends BaseForm {
    private JTextField txtDni;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnCancelar;
    
    private final UsuarioController usuarioController;
    
    public LoginView() {
        super("Registro Digital de Campañas de Salud Comunitarias - Login");
        System.out.println("LoginView: Constructor iniciado");
        
        this.usuarioController = new UsuarioController();
        System.out.println("LoginView: UsuarioController creado");
        
        setupLayout();
        System.out.println("LoginView: Layout configurado");
    }
    
    @Override
    protected void setupLayout() {
        System.out.println("LoginView: Configurando layout...");
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("DNI:"), gbc);
        
        gbc.gridx = 1;
        txtDni = createTextField(15);
        formPanel.add(txtDni, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        txtContrasena = new JPasswordField(15);
        txtContrasena.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtContrasena, gbc);
        
        // Add form panel to content panel
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add buttons to button panel
        btnIngresar = createButton("Ingresar");
        btnCancelar = createButton("Cancelar");
        
        buttonPanel.add(btnIngresar);
        buttonPanel.add(btnCancelar);
        
        // Add action listeners
        btnIngresar.addActionListener(e -> login());
        btnCancelar.addActionListener(e -> System.exit(0));
        
        // Add enter key listener to password field
        txtContrasena.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        
        System.out.println("LoginView: Layout configurado correctamente");
    }
    
    private void login() {
        String dni = txtDni.getText();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (dni.isEmpty() || contrasena.isEmpty()) {
            showError("Por favor ingrese su DNI y contraseña");
            return;
        }
        
        try {
            Usuario usuario = usuarioController.autenticarUsuario(dni, contrasena);
            
            if (usuario != null) {
                handleLoginSuccess(usuario);
            } else {
                showError("DNI o contraseña incorrectos");
                txtContrasena.setText("");
                txtContrasena.requestFocus();
            }
        } catch (Exception e) {
            showError("Error al intentar iniciar sesión: " + e.getMessage());
        }
    }
    
    private void handleLoginSuccess(Usuario usuario) {
        System.out.println("Login exitoso para: " + usuario.getNombre() + " " + usuario.getApellido());
        
        // Mostrar información del usuario
        String mensaje = String.format(
            "Bienvenido %s %s\n" +
            "Rol: %s\n" +
            "Último acceso: %s",
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getRol(),
            usuario.getUltimoAcceso() != null ? usuario.getUltimoAcceso().toString() : "Primer acceso"
        );
        
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Inicio de Sesión Exitoso",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Abrir la vista principal
        MainView mainView = new MainView(usuario);
        mainView.setVisible(true);
        this.dispose();
    }
} 