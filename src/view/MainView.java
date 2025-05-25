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
import javax.swing.*;
import java.awt.*;

/**
 * Main view for the application
 */
public class MainView extends BaseForm {
    private Usuario usuarioActual;
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
        super("Registro Digital de Campañas de Salud Comunitarias");
        
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        this.usuarioActual = usuario;
        this.usuarioController = new UsuarioController();
        this.campanaController = new CampanaController();
        this.participanteController = new ParticipanteController();
        this.prediccionController = new PrediccionController();
        
        // Initialize layout after all fields are set
        setupLayout();
    }
    
    @Override
    protected void setupLayout() {
        if (usuarioActual == null) {
            throw new IllegalStateException("usuarioActual no puede ser null");
        }
        
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Card panel to switch between views
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        
        // Create and add panels
        cardPanel.add(createDashboardPanel(), PANEL_DASHBOARD);
        cardPanel.add(createCampanasPanel(), PANEL_CAMPANAS);
        cardPanel.add(createActividadesPanel(), PANEL_ACTIVIDADES);
        cardPanel.add(createParticipantesPanel(), PANEL_PARTICIPANTES);
        cardPanel.add(createPrediccionesPanel(), PANEL_PREDICCIONES);
        cardPanel.add(createUsuariosPanel(), PANEL_USUARIOS);
        
        // Add card panel to content panel
        contentPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Show dashboard by default
        cardLayout.show(cardPanel, PANEL_DASHBOARD);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> {
            if (showConfirm("¿Está seguro que desea salir?")) {
                System.exit(0);
            }
        });
        menuArchivo.add(itemSalir);
        
        // Campaign menu
        JMenu menuCampanas = new JMenu("Campañas");
        JMenuItem itemVerCampanas = new JMenuItem("Ver Campañas");
        itemVerCampanas.addActionListener(e -> cardLayout.show(cardPanel, PANEL_CAMPANAS));
        JMenuItem itemNuevaCampana = new JMenuItem("Nueva Campaña");
        itemNuevaCampana.addActionListener(e -> showInfo("Funcionalidad para crear nueva campaña no implementada aún"));
        menuCampanas.add(itemVerCampanas);
        menuCampanas.add(itemNuevaCampana);
        
        // Activity menu
        JMenu menuActividades = new JMenu("Actividades");
        JMenuItem itemVerActividades = new JMenuItem("Ver Actividades");
        itemVerActividades.addActionListener(e -> cardLayout.show(cardPanel, PANEL_ACTIVIDADES));
        menuActividades.add(itemVerActividades);
        
        // Participant menu
        JMenu menuParticipantes = new JMenu("Participantes");
        JMenuItem itemVerParticipantes = new JMenuItem("Ver Participantes");
        itemVerParticipantes.addActionListener(e -> cardLayout.show(cardPanel, PANEL_PARTICIPANTES));
        JMenuItem itemRegistrarParticipante = new JMenuItem("Registrar Participante");
        itemRegistrarParticipante.addActionListener(e -> showInfo("Funcionalidad para registrar participante no implementada aún"));
        menuParticipantes.add(itemVerParticipantes);
        menuParticipantes.add(itemRegistrarParticipante);
        
        // Prediction menu
        JMenu menuPredicciones = new JMenu("Predicciones");
        JMenuItem itemVerPredicciones = new JMenuItem("Ver Predicciones");
        itemVerPredicciones.addActionListener(e -> cardLayout.show(cardPanel, PANEL_PREDICCIONES));
        JMenuItem itemGenerarPrediccion = new JMenuItem("Generar Predicción");
        itemGenerarPrediccion.addActionListener(e -> showInfo("Funcionalidad para generar predicción no implementada aún"));
        menuPredicciones.add(itemVerPredicciones);
        menuPredicciones.add(itemGenerarPrediccion);
        
        // Admin menu (only for admin users)
        JMenu menuAdmin = new JMenu("Administración");
        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        itemUsuarios.addActionListener(e -> cardLayout.show(cardPanel, PANEL_USUARIOS));
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
        
        return menuBar;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add welcome message
        JLabel welcomeLabel = new JLabel(
            "Bienvenido " + usuarioActual.getNombre() + " " + usuarioActual.getApellido(),
            SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCampanasPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add table
        String[] columnNames = {"ID", "Nombre", "Descripción", "Fecha Inicio", "Fecha Fin", "Responsable"};
        JTable table = createTable(columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton("Nueva Campaña"));
        buttonPanel.add(createButton("Editar"));
        buttonPanel.add(createButton("Eliminar"));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createActividadesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add table
        String[] columnNames = {"ID", "Nombre", "Descripción", "Fecha", "Hora", "Campaña"};
        JTable table = createTable(columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton("Nueva Actividad"));
        buttonPanel.add(createButton("Editar"));
        buttonPanel.add(createButton("Eliminar"));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createParticipantesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add table
        String[] columnNames = {"ID", "Nombre", "Apellido", "DNI", "Edad", "Sexo", "Dirección"};
        JTable table = createTable(columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton("Nuevo Participante"));
        buttonPanel.add(createButton("Editar"));
        buttonPanel.add(createButton("Eliminar"));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPrediccionesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add table
        String[] columnNames = {"ID", "Campaña", "Fecha Predicción", "Participación Estimada", "Notas"};
        JTable table = createTable(columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton("Nueva Predicción"));
        buttonPanel.add(createButton("Editar"));
        buttonPanel.add(createButton("Eliminar"));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createUsuariosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add table
        String[] columnNames = {"ID", "Nombre", "Apellido", "Correo", "DNI", "Rol"};
        JTable table = createTable(columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton("Nuevo Usuario"));
        buttonPanel.add(createButton("Editar"));
        buttonPanel.add(createButton("Eliminar"));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
} 