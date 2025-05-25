package view;

import controller.UsuarioController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import model.Usuario;

/**
 * Login view for the application
 */
public class LoginView extends JFrame {
    private JTextField txtDni;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnCancelar;
    
    private final UsuarioController usuarioController;
    
    public LoginView() {
        this.usuarioController = new UsuarioController();
        
        setTitle("Registro Digital de Campañas de Salud Comunitarias - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        // Form fields
        JLabel lblDni = new JLabel("DNI:");
        txtDni = new JTextField(15);
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField(15);
        
        formPanel.add(lblDni);
        formPanel.add(txtDni);
        formPanel.add(lblContrasena);
        formPanel.add(txtContrasena);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnIngresar = new JButton("Ingresar");
        btnCancelar = new JButton("Cancelar");
        
        buttonPanel.add(btnIngresar);
        buttonPanel.add(btnCancelar);
        
        // Add to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
        
        // Add action listeners
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void login() {
        String dni = txtDni.getText();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (dni.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese su DNI y contraseña", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarioController.autenticarUsuario(dni, contrasena);
        
        if (usuario != null) {
            // Successful login
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario.getNombre() + " " + usuario.getApellido(), "Login exitoso", JOptionPane.INFORMATION_MESSAGE);
            
            // Open main menu
            MainView mainView = new MainView(usuario);
            mainView.setVisible(true);
            
            // Close login window
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "DNI o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }
} 