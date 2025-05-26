package view;

import controller.ActividadController;
import controller.CampanaController;
import model.Actividad;
import model.Campana;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActividadFormView extends JDialog {
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JSpinner spnFecha;
    private JTextField txtHora;
    private JComboBox<Campana> cmbCampana;
    private JSpinner spnCapacidad;
    private JComboBox<String> cmbEstado;
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    private Actividad actividadActual;
    private final ActividadController actividadController;
    private final CampanaController campanaController;
    
    public ActividadFormView(Actividad actividad) {
        this.actividadActual = actividad;
        this.actividadController = new ActividadController();
        this.campanaController = new CampanaController();
        
        setTitle(actividad == null ? "Nueva Actividad" : "Editar Actividad");
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
        txtNombre = new JTextField(30);
        txtDescripcion = new JTextArea(4, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        
        // Date spinner
        spnFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
        spnFecha.setEditor(dateEditor);
        
        txtHora = new JTextField(10);
        txtHora.setText("09:00"); // Default time
        
        // Campaign combo
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
        
        // Capacity spinner
        spnCapacidad = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        
        // Status combo
        cmbEstado = new JComboBox<>(new String[]{"planificada", "en_progreso", "finalizada", "cancelada"});
        
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0: Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNombre, gbc);
        
        // Row 1: Descripción
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(txtDescripcion), gbc);
        
        // Row 2: Fecha
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spnFecha, gbc);
        
        // Row 3: Hora
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtHora, gbc);
        
        // Row 4: Campaña
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Campaña:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(cmbCampana, gbc);
        
        // Row 5: Capacidad
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Capacidad:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spnCapacidad, gbc);
        
        // Row 6: Estado
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(cmbEstado, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarActividad();
            }
        });
        
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
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
        
        // Load activity data if editing
        if (actividadActual != null) {
            txtNombre.setText(actividadActual.getNombre());
            txtDescripcion.setText(actividadActual.getDescripcion());
            spnFecha.setValue(actividadActual.getFecha());
            txtHora.setText(actividadActual.getHora());
            spnCapacidad.setValue(actividadActual.getCupoMaximo());
            cmbEstado.setSelectedItem(actividadActual.getEstado());
            
            // Select the appropriate campaign
            for (int i = 0; i < cmbCampana.getItemCount(); i++) {
                Campana campana = cmbCampana.getItemAt(i);
                if (campana.getIdCampana() == actividadActual.getIdCampana()) {
                    cmbCampana.setSelectedItem(campana);
                    break;
                }
            }
        }
    }
    
    private void guardarActividad() {
        try {
            // Validate inputs
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                txtNombre.requestFocus();
                return;
            }
            
            if (txtDescripcion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
                txtDescripcion.requestFocus();
                return;
            }
            
            if (cmbCampana.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una campaña.", "Error", JOptionPane.ERROR_MESSAGE);
                cmbCampana.requestFocus();
                return;
            }
            
            if (!isValidTimeFormat(txtHora.getText())) {
                JOptionPane.showMessageDialog(this, "El formato de hora debe ser HH:mm (ejemplo: 09:30).", "Error", JOptionPane.ERROR_MESSAGE);
                txtHora.requestFocus();
                return;
            }
            
            // Get values
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            Date fecha = (Date) spnFecha.getValue();
            String hora = txtHora.getText().trim();
            Campana campanaSeleccionada = (Campana) cmbCampana.getSelectedItem();
            int capacidad = (Integer) spnCapacidad.getValue();
            String estado = (String) cmbEstado.getSelectedItem();
            
            boolean success;
            if (actividadActual == null) {
                // Create new activity
                success = actividadController.registrarActividad(
                    nombre, descripcion, fecha, hora, 
                    campanaSeleccionada.getIdCampana(), capacidad, estado
                );
            } else {
                // Update existing activity
                success = actividadController.actualizarActividad(
                    actividadActual.getIdActividad(), nombre, descripcion, fecha, hora,
                    campanaSeleccionada.getIdCampana(), capacidad, estado
                );
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    actividadActual == null ? "Actividad creada exitosamente." : "Actividad actualizada exitosamente.",
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al " + (actividadActual == null ? "crear" : "actualizar") + " la actividad.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isValidTimeFormat(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            sdf.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    

} 