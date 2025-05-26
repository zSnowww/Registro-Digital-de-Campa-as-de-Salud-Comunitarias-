package view;

import controller.PrediccionController;
import controller.CampanaController;
import model.Prediccion;
import model.Campana;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrediccionFormView extends JDialog {
    private JComboBox<Campana> cmbCampana;
    private JSpinner spnParticipacionEstimada;
    private JLabel lblNivelConfianza;
    private JTextArea txtNotas;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnGenerarAutomatico;
    
    private Prediccion prediccionActual;
    private final PrediccionController prediccionController;
    private final CampanaController campanaController;
    
    public PrediccionFormView(Prediccion prediccion) {
        this.prediccionActual = prediccion;
        this.prediccionController = new PrediccionController();
        this.campanaController = new CampanaController();
        
        setTitle(prediccion == null ? "Generar Nueva Predicción" : "Editar Predicción");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        setupLayout();
        setupEvents();
        loadData();
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Campaign selection
        cmbCampana = new JComboBox<>();
        cmbCampana.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Campana) {
                    Campana campana = (Campana) value;
                    setText(campana.getNombre());
                }
                return this;
            }
        });
        
        // Estimated participation
        spnParticipacionEstimada = new JSpinner(new SpinnerNumberModel(50, 0, 10000, 1));
        
        // Confidence level (read-only)
        lblNivelConfianza = new JLabel("Calculando...");
        lblNivelConfianza.setFont(lblNivelConfianza.getFont().deriveFont(Font.BOLD));
        
        // Notes
        txtNotas = new JTextArea(4, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        
        // Buttons
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        btnGenerarAutomatico = new JButton("Generar Automático");
        btnGenerarAutomatico.setToolTipText("Genera predicción basada en datos históricos");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campaign selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Campaña:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cmbCampana, gbc);
        
        // Generate automatic button (next to campaign)
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(btnGenerarAutomatico, gbc);
        
        // Estimated participation
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Participación Estimada:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(spnParticipacionEstimada, gbc);
        
        // Confidence level
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nivel de Confianza:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(lblNivelConfianza, gbc);
        
        // Notes
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Notas:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(txtNotas), gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        btnGuardar.addActionListener(this::guardarPrediccion);
        btnCancelar.addActionListener(e -> dispose());
        btnGenerarAutomatico.addActionListener(this::generarPrediccionAutomatica);
        
        // Update estimated participation when campaign changes
        cmbCampana.addActionListener(e -> {
            if (btnGenerarAutomatico.isEnabled()) {
                // Auto-update prediction when campaign changes (optional)
            }
        });
    }
    
    private void loadData() {
        // Load campaigns
        try {
            List<Campana> campanas = campanaController.listarCampanas();
            for (Campana campana : campanas) {
                cmbCampana.addItem(campana);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las campañas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        // Load prediction data if editing
        if (prediccionActual != null) {
            spnParticipacionEstimada.setValue(prediccionActual.getParticipacionEstimada());
            lblNivelConfianza.setText(String.format("%.1f%%", prediccionActual.getNivelConfianza()));
            txtNotas.setText(prediccionActual.getNotas());
            btnGenerarAutomatico.setEnabled(false); // Disable auto-generation when editing
            
            // Select the appropriate campaign
            for (int i = 0; i < cmbCampana.getItemCount(); i++) {
                Campana campana = cmbCampana.getItemAt(i);
                if (campana.getIdCampana() == prediccionActual.getIdCampana()) {
                    cmbCampana.setSelectedItem(campana);
                    break;
                }
            }
            
            // Disable campaign selection when editing (to prevent conflicts)
            cmbCampana.setEnabled(false);
        }
    }
    
    private void guardarPrediccion(ActionEvent e) {
        if (!validarDatos()) {
            return;
        }
        
        try {
            Campana campanaSeleccionada = (Campana) cmbCampana.getSelectedItem();
            int participacionEstimada = (Integer) spnParticipacionEstimada.getValue();
            String notas = txtNotas.getText().trim();
            
            boolean exito;
            if (prediccionActual == null) {
                // Create new prediction
                exito = prediccionController.generarPrediccion(
                    campanaSeleccionada.getIdCampana(), 
                    notas
                );
                
                // If auto-generation was used, update with manual value
                if (exito && participacionEstimada != 50) { // 50 is default from controller
                    Prediccion nuevaPrediccion = prediccionController.obtenerPrediccionPorCampana(
                        campanaSeleccionada.getIdCampana()
                    );
                    if (nuevaPrediccion != null) {
                        prediccionController.actualizarPrediccion(
                            nuevaPrediccion.getIdPrediccion(),
                            participacionEstimada,
                            notas
                        );
                    }
                }
            } else {
                // Update existing prediction
                exito = prediccionController.actualizarPrediccion(
                    prediccionActual.getIdPrediccion(),
                    participacionEstimada,
                    notas
                );
            }
            
            if (exito) {
                JOptionPane.showMessageDialog(this,
                    "Predicción guardada exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar la predicción.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la predicción: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generarPrediccionAutomatica(ActionEvent e) {
        Campana campanaSeleccionada = (Campana) cmbCampana.getSelectedItem();
        if (campanaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione una campaña primero.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Generate automatic prediction using the controller's algorithm
            boolean exito = prediccionController.generarPrediccion(
                campanaSeleccionada.getIdCampana(),
                "Predicción generada automáticamente basada en datos históricos"
            );
            
            if (exito) {
                // Get the generated prediction and update form
                Prediccion prediccionGenerada = prediccionController.obtenerPrediccionPorCampana(
                    campanaSeleccionada.getIdCampana()
                );
                
                if (prediccionGenerada != null) {
                    spnParticipacionEstimada.setValue(prediccionGenerada.getParticipacionEstimada());
                    lblNivelConfianza.setText(String.format("%.1f%%", prediccionGenerada.getNivelConfianza()));
                    txtNotas.setText(prediccionGenerada.getNotas());
                    
                    JOptionPane.showMessageDialog(this,
                        "Predicción generada automáticamente.\n" +
                        "Participación estimada: " + prediccionGenerada.getParticipacionEstimada() + " personas\n" +
                        "Nivel de confianza: " + String.format("%.1f%%", prediccionGenerada.getNivelConfianza()) + "\n" +
                        "Puede modificar los valores antes de guardar.",
                        "Predicción Generada",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al generar la predicción automática.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al generar la predicción: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarDatos() {
        // Validate campaign selection
        if (cmbCampana.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione una campaña.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE);
            cmbCampana.requestFocus();
            return false;
        }
        
        // Validate estimated participation
        int participacionEstimada = (Integer) spnParticipacionEstimada.getValue();
        if (participacionEstimada <= 0) {
            JOptionPane.showMessageDialog(this,
                "La participación estimada debe ser mayor a 0.",
                "Datos inválidos",
                JOptionPane.WARNING_MESSAGE);
            spnParticipacionEstimada.requestFocus();
            return false;
        }
        
        return true;
    }
} 