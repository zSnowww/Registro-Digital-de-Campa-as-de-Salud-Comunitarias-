package view;

import controller.CampanaController;
import controller.ParticipanteController;
import controller.PrediccionController;
import controller.UsuarioController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.Usuario;

/**
 * Main view for the application
 */
public class MainView extends JFrame {
    private final Usuario usuarioActual;
    private final UsuarioController usuarioController;
    private final CampanaController campanaController;
    private final ParticipanteController participanteController;
    private final PrediccionController prediccionController;
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Panel names
    private static final String PANEL_DASHBOARD = "dashboard";
    private static final String PANEL_CAMPANAS = "campanas";
    private static final String PANEL_ACTIVIDADES = "actividades";
    private static final String PANEL_PARTICIPANTES = "participantes";
    private static final String PANEL_PREDICCIONES = "predicciones";
    private static final String PANEL_USUARIOS = "usuarios";
    
    public MainView(Usuario usuario) {
        this.usuarioActual = usuario;
        this.usuarioController = new UsuarioController();
        this.campanaController = new CampanaController();
        this.participanteController = new ParticipanteController();
        this.prediccionController = new PrediccionController();
        
        setTitle("Registro Digital de Campañas de Salud Comunitarias");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(MainView.this, 
                    "¿Está seguro que desea salir?", "Confirmar salida", 
                    JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        menuArchivo.add(itemSalir);
        
        // Campaign menu
        JMenu menuCampanas = new JMenu("Campañas");
        JMenuItem itemVerCampanas = new JMenuItem("Ver Campañas");
        itemVerCampanas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PANEL_CAMPANAS);
            }
        });
        JMenuItem itemNuevaCampana = new JMenuItem("Nueva Campaña");
        itemNuevaCampana.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show new campaign dialog
                JOptionPane.showMessageDialog(MainView.this, 
                    "Funcionalidad para crear nueva campaña no implementada aún", 
                    "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuCampanas.add(itemVerCampanas);
        menuCampanas.add(itemNuevaCampana);
        
        // Activity menu
        JMenu menuActividades = new JMenu("Actividades");
        JMenuItem itemVerActividades = new JMenuItem("Ver Actividades");
        itemVerActividades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PANEL_ACTIVIDADES);
            }
        });
        menuActividades.add(itemVerActividades);
        
        // Participant menu
        JMenu menuParticipantes = new JMenu("Participantes");
        JMenuItem itemVerParticipantes = new JMenuItem("Ver Participantes");
        itemVerParticipantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PANEL_PARTICIPANTES);
            }
        });
        JMenuItem itemRegistrarParticipante = new JMenuItem("Registrar Participante");
        itemRegistrarParticipante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show register participant dialog
                JOptionPane.showMessageDialog(MainView.this, 
                    "Funcionalidad para registrar participante no implementada aún", 
                    "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuParticipantes.add(itemVerParticipantes);
        menuParticipantes.add(itemRegistrarParticipante);
        
        // Prediction menu
        JMenu menuPredicciones = new JMenu("Predicciones");
        JMenuItem itemVerPredicciones = new JMenuItem("Ver Predicciones");
        itemVerPredicciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PANEL_PREDICCIONES);
            }
        });
        JMenuItem itemGenerarPrediccion = new JMenuItem("Generar Predicción");
        itemGenerarPrediccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show generate prediction dialog
                JOptionPane.showMessageDialog(MainView.this, 
                    "Funcionalidad para generar predicción no implementada aún", 
                    "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuPredicciones.add(itemVerPredicciones);
        menuPredicciones.add(itemGenerarPrediccion);
        
        // Admin menu (only for admin users)
        JMenu menuAdmin = new JMenu("Administración");
        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        itemUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, PANEL_USUARIOS);
            }
        });
        menuAdmin.add(itemUsuarios);
        
        // Add menus to menu bar
        menuBar.add(menuArchivo);
        menuBar.add(menuCampanas);
        menuBar.add(menuActividades);
        menuBar.add(menuParticipantes);
        menuBar.add(menuPredicciones);
        
        // Add admin menu only for admin users
        if ("Administrador".equals(usuarioActual.getRol())) {
            menuBar.add(menuAdmin);
        }
        
        // Set menu bar
        setJMenuBar(menuBar);
        
        // Card panel to switch between views
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        
        // Create and add panels
        // For now, we'll use placeholder panels
        JPanel dashboardPanel = new DashboardPanel();
        JPanel campanasPanel = new JPanel();
        campanasPanel.setPreferredSize(new Dimension(800, 600));
        JPanel actividadesPanel = new JPanel();
        actividadesPanel.setPreferredSize(new Dimension(800, 600));
        JPanel participantesPanel = new JPanel();
        participantesPanel.setPreferredSize(new Dimension(800, 600));
        JPanel prediccionesPanel = new JPanel();
        prediccionesPanel.setPreferredSize(new Dimension(800, 600));
        JPanel usuariosPanel = new JPanel();
        usuariosPanel.setPreferredSize(new Dimension(800, 600));
        
        // Add panels to card panel
        cardPanel.add(dashboardPanel, PANEL_DASHBOARD);
        cardPanel.add(campanasPanel, PANEL_CAMPANAS);
        cardPanel.add(actividadesPanel, PANEL_ACTIVIDADES);
        cardPanel.add(participantesPanel, PANEL_PARTICIPANTES);
        cardPanel.add(prediccionesPanel, PANEL_PREDICCIONES);
        cardPanel.add(usuariosPanel, PANEL_USUARIOS);
        
        // Show dashboard by default
        cardLayout.show(cardPanel, PANEL_DASHBOARD);
        
        // Add card panel to frame
        add(cardPanel, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // For testing only - create a dummy user
                Usuario dummyUser = new Usuario();
                dummyUser.setIdUsuario(1);
                dummyUser.setNombre("Admin");
                dummyUser.setApellido("Test");
                dummyUser.setDni("12345678");
                dummyUser.setRol("Administrador");
                
                new MainView(dummyUser).setVisible(true);
            }
        });
    }
    
    // Inner class for the dashboard panel
    private class DashboardPanel extends JPanel {
        public DashboardPanel() {
            setLayout(new BorderLayout(10, 10));
            setPreferredSize(new Dimension(800, 600));
            
            // Add dashboard components here
            // This is just a placeholder for now
            add(new javax.swing.JLabel("Bienvenido al Sistema de Registro Digital de Campañas de Salud Comunitarias", javax.swing.JLabel.CENTER), BorderLayout.CENTER);
        }
    }
} 