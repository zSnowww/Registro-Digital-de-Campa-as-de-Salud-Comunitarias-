package view;

import controller.CampanaController;
import controller.ParticipanteController;
import controller.PrediccionController;
import controller.UsuarioController;
import controller.ActividadController;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import model.Usuario;
import model.Actividad;
import model.Campana;
import model.Prediccion;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista principal de la aplicación
 */
public class MainView extends BaseForm {
    private Usuario usuarioActual;
    private final UsuarioController usuarioController;
    private final CampanaController campanaController;
    private final ParticipanteController participanteController;
    private final PrediccionController prediccionController;
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Nombres de paneles
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
        
        // Inicializar diseño después de configurar todos los campos
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
        
        // Inicio menu (combines dashboard and exit functionality)
        JMenu menuInicio = new JMenu("Inicio");
        JMenuItem itemDashboard = new JMenuItem("Ver Dashboard");
        itemDashboard.addActionListener(e -> cardLayout.show(cardPanel, PANEL_DASHBOARD));
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> {
            if (showConfirm("¿Está seguro que desea salir?")) {
                System.exit(0);
            }
        });
        menuInicio.add(itemDashboard);
        menuInicio.addSeparator(); // Add separator between dashboard and exit
        menuInicio.add(itemSalir);
        
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
        JMenuItem itemVerActividades = new JMenuItem("Gestionar Actividades");
        itemVerActividades.addActionListener(e -> cardLayout.show(cardPanel, PANEL_ACTIVIDADES));
        JMenuItem itemRegistrarParticipacion = new JMenuItem("Registrar Participación");
        itemRegistrarParticipacion.addActionListener(e -> {
            RegistroParticipacionView registroView = new RegistroParticipacionView();
            registroView.setVisible(true);
        });
        JMenuItem itemVerCobertura = new JMenuItem("Ver Cobertura y Resultados");
        itemVerCobertura.addActionListener(e -> {
            CoberturaActividadView coberturaView = new CoberturaActividadView();
            coberturaView.setVisible(true);
        });
        menuActividades.add(itemVerActividades);
        menuActividades.add(itemRegistrarParticipacion);
        menuActividades.add(itemVerCobertura);
        
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
        itemGenerarPrediccion.addActionListener(e -> {
            PrediccionFormView formView = new PrediccionFormView(null);
            formView.setVisible(true);
        });
        menuPredicciones.add(itemVerPredicciones);
        menuPredicciones.add(itemGenerarPrediccion);
        
        // Admin menu (only for admin users)
        JMenu menuAdmin = new JMenu("Administración");
        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        itemUsuarios.addActionListener(e -> cardLayout.show(cardPanel, PANEL_USUARIOS));
        menuAdmin.add(itemUsuarios);
        
        // Add menus to menu bar
        menuBar.add(menuInicio);
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
        DashboardView dashboardView = new DashboardView();
        return dashboardView.getMainPanel();
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
        
        // Add title
        JLabel titleLabel = new JLabel("Gestión de Actividades");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Create table with data from database
        String[] columnNames = {"ID", "Nombre", "Descripción", "Fecha", "Hora", "Campaña", "Capacidad", "Registrados", "Estado"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Load data from database
        loadActividadesData(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons with functionality
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnNueva = new JButton("Nueva Actividad");
        btnNueva.addActionListener(e -> {
            ActividadFormView formView = new ActividadFormView(null);
            formView.setVisible(true);
            formView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadActividadesData(tableModel); // Refresh table after form closes
                }
            });
        });
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                showError("Por favor, seleccione una actividad para editar.");
                return;
            }
            
            int actividadId = (Integer) tableModel.getValueAt(selectedRow, 0);
            ActividadController controller = new ActividadController();
            Actividad actividad = controller.obtenerActividadPorId(actividadId);
            
            if (actividad != null) {
                ActividadFormView formView = new ActividadFormView(actividad);
                formView.setVisible(true);
                formView.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        loadActividadesData(tableModel); // Refresh table after form closes
                    }
                });
            }
        });
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                showError("Por favor, seleccione una actividad para eliminar.");
                return;
            }
            
            if (showConfirm("¿Está seguro que desea eliminar esta actividad?")) {
                int actividadId = (Integer) tableModel.getValueAt(selectedRow, 0);
                ActividadController controller = new ActividadController();
                
                if (controller.eliminarActividad(actividadId)) {
                    showInfo("Actividad eliminada exitosamente.");
                    loadActividadesData(tableModel); // Refresh table
                } else {
                    showError("Error al eliminar la actividad.");
                }
            }
        });
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> loadActividadesData(tableModel));
        
        buttonPanel.add(btnNueva);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnRefrescar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadActividadesData(DefaultTableModel tableModel) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            ActividadController actividadController = new ActividadController();
            CampanaController campanaController = new CampanaController();
            List<Actividad> actividades = actividadController.listarActividades();
            
            for (Actividad actividad : actividades) {
                // Get campaign name instead of just ID
                String nombreCampana = "Campaña " + actividad.getIdCampana(); // Default fallback
                try {
                    Campana campana = campanaController.obtenerCampanaPorId(actividad.getIdCampana());
                    if (campana != null) {
                        nombreCampana = campana.getNombre();
                    }
                } catch (Exception e) {
                    // Keep default if campaign not found
                }
                
                Object[] row = {
                    actividad.getIdActividad(),
                    actividad.getNombre(),
                    actividad.getDescripcion(),
                    actividad.getFecha(),
                    actividad.getHora(),
                    nombreCampana,
                    actividad.getCupoMaximo(),
                    actividad.getParticipantesRegistrados(),
                    actividad.getEstado()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar las actividades: " + e.getMessage());
        }
    }
    
    private void loadPrediccionesData(DefaultTableModel tableModel) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            List<model.Prediccion> predicciones = prediccionController.listarPredicciones();
            
            for (model.Prediccion prediccion : predicciones) {
                // Get campaign name instead of just ID
                String nombreCampana = "Campaña " + prediccion.getIdCampana(); // Default fallback
                try {
                    Campana campana = campanaController.obtenerCampanaPorId(prediccion.getIdCampana());
                    if (campana != null) {
                        nombreCampana = campana.getNombre();
                    }
                } catch (Exception e) {
                    // Keep default if campaign not found
                }
                
                // Format confidence level as percentage
                String nivelConfianza = String.format("%.1f%%", prediccion.getNivelConfianza());
                
                Object[] row = {
                    prediccion.getIdPrediccion(),
                    nombreCampana,
                    prediccion.getFechaPrediccion(),
                    prediccion.getParticipacionEstimada(),
                    nivelConfianza,
                    prediccion.getNotas()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar las predicciones: " + e.getMessage());
        }
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
        
        // Add title
        JLabel titleLabel = new JLabel("Gestión de Predicciones");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Create table with data from database
        String[] columnNames = {"ID", "Campaña", "Fecha Predicción", "Participación Estimada", "Nivel Confianza", "Notas"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Load data from database
        loadPrediccionesData(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons with functionality
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnNueva = new JButton("Generar Predicción");
        btnNueva.addActionListener(e -> {
            PrediccionFormView formView = new PrediccionFormView(null);
            formView.setVisible(true);
            formView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadPrediccionesData(tableModel); // Refresh table after form closes
                }
            });
        });
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                showError("Por favor, seleccione una predicción para editar.");
                return;
            }
            
            int prediccionId = (Integer) tableModel.getValueAt(selectedRow, 0);
            model.Prediccion prediccion = prediccionController.obtenerPrediccionPorId(prediccionId);
            
            if (prediccion != null) {
                PrediccionFormView formView = new PrediccionFormView(prediccion);
                formView.setVisible(true);
                formView.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        loadPrediccionesData(tableModel); // Refresh table after form closes
                    }
                });
            }
        });
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                showError("Por favor, seleccione una predicción para eliminar.");
                return;
            }
            
            if (showConfirm("¿Está seguro que desea eliminar esta predicción?")) {
                int prediccionId = (Integer) tableModel.getValueAt(selectedRow, 0);
                
                if (prediccionController.eliminarPrediccion(prediccionId)) {
                    showInfo("Predicción eliminada exitosamente.");
                    loadPrediccionesData(tableModel); // Refresh table
                } else {
                    showError("Error al eliminar la predicción.");
                }
            }
        });
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> loadPrediccionesData(tableModel));
        
        buttonPanel.add(btnNueva);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnRefrescar);
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
    
    // Add getter for mainPanel in DashboardView
    public JPanel getMainPanel() {
        return mainPanel;
    }
} 