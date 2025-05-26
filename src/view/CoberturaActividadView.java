package view;

import controller.ActividadController;
import controller.RegistroParticipacionController;
import model.Actividad;
import model.RegistroParticipacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoberturaActividadView extends JFrame {
    private JTable tblCobertura;
    private JComboBox<String> cmbActividad;
    private JLabel lblTotalParticipantes;
    private JLabel lblCobertura;
    
    private final ActividadController actividadController;
    private final RegistroParticipacionController registroController;
    
    public CoberturaActividadView() {
        actividadController = new ActividadController();
        registroController = new RegistroParticipacionController();
        
        setTitle("Cobertura y Resultados por Actividad");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        cargarActividades();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con combobox y etiquetas
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Seleccione una actividad:"));
        cmbActividad = new JComboBox<>();
        panelSuperior.add(cmbActividad);
        
        lblTotalParticipantes = new JLabel("Total participantes: 0");
        panelSuperior.add(lblTotalParticipantes);
        
        lblCobertura = new JLabel("Cobertura: 0%");
        panelSuperior.add(lblCobertura);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de cobertura
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Resultado", "Observaciones", "Fecha Registro"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCobertura = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tblCobertura);
        add(scrollPane, BorderLayout.CENTER);
        
        // Agregar listener al combobox
        cmbActividad.addActionListener(e -> cargarDatosCobertura());
    }
    
    private void cargarActividades() {
        try {
            List<Actividad> actividades = actividadController.listarActividades();
            cmbActividad.removeAllItems();
            for (Actividad actividad : actividades) {
                cmbActividad.addItem(actividad.getIdActividad() + " - " + actividad.getNombre());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar las actividades: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarDatosCobertura() {
        try {
            String actividadSeleccionada = (String) cmbActividad.getSelectedItem();
            if (actividadSeleccionada == null) return;
            
            int idActividad = Integer.parseInt(actividadSeleccionada.split(" - ")[0]);
            List<RegistroParticipacion> registros = registroController.listarRegistrosPorActividad(idActividad);
            
            // Limpiar tabla
            DefaultTableModel model = (DefaultTableModel) tblCobertura.getModel();
            model.setRowCount(0);
            
            // Llenar tabla
            for (RegistroParticipacion registro : registros) {
                if (registro.getParticipante() != null) {
                    model.addRow(new Object[]{
                        registro.getIdParticipante(),
                        registro.getParticipante().getNombre(),
                        registro.getParticipante().getApellido(),
                        registro.getParticipante().getDni(),
                        registro.getResultado(),
                        registro.getObservaciones(),
                        registro.getFechaRegistro()
                    });
                }
            }
            
            // Actualizar estadísticas
            Actividad actividad = actividadController.obtenerActividadPorId(idActividad);
            if (actividad != null) {
                int totalParticipantes = registros.size();
                int cupoMaximo = actividad.getCupoMaximo();
                double cobertura = cupoMaximo > 0 ? (totalParticipantes * 100.0 / cupoMaximo) : 0;
                
                lblTotalParticipantes.setText("Total participantes: " + totalParticipantes);
                lblCobertura.setText(String.format("Cobertura: %.1f%%", cobertura));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los datos de cobertura: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 