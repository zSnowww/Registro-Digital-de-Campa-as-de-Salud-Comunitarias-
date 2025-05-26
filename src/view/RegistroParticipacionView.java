package view;

import controller.ActividadController;
import controller.ParticipanteController;
import controller.RegistroParticipacionController;
import model.Participante;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import model.Actividad;

public class RegistroParticipacionView extends JFrame {
    private JTextField txtDni;
    private JComboBox<String> cmbActividad;
    private JComboBox<String> cmbResultado;
    private JTextArea txtObservaciones;
    private JButton btnRegistrar;
    
    // Campos para nuevo participante
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEdad;
    private JComboBox<String> cmbSexo;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JDialog dialogParticipante;
    
    private final ParticipanteController participanteController;
    private final RegistroParticipacionController registroController;

    public RegistroParticipacionView() {
        participanteController = new ParticipanteController();
        registroController = new RegistroParticipacionController();

        setTitle("Registro de Participación");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        cargarActividades();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // DNI
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("DNI Participante:"), gbc);
        gbc.gridx = 1;
        txtDni = new JTextField(15);
        panel.add(txtDni, gbc);

        // Actividad
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Actividad:"), gbc);
        gbc.gridx = 1;
        cmbActividad = new JComboBox<>();
        panel.add(cmbActividad, gbc);

        // Resultado
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Resultado:"), gbc);
        gbc.gridx = 1;
        String[] resultados = {
            "Completado",
            "Pendiente",
            "Cancelado",
            "No asistió",
            "Vacunado",
            "Examen realizado",
            "Consulta completada",
            "Tratamiento iniciado",
            "Participación total",
            "Participación parcial"
        };
        cmbResultado = new JComboBox<>(resultados);
        panel.add(cmbResultado, gbc);

        // Observaciones
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        txtObservaciones = new JTextArea(3, 15);
        JScrollPane scroll = new JScrollPane(txtObservaciones);
        panel.add(scroll, gbc);

        // Botón
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        btnRegistrar = new JButton("Registrar Participación");
        btnRegistrar.addActionListener(this::registrarParticipacion);
        panel.add(btnRegistrar, gbc);

        add(panel);
    }

    private void crearDialogParticipante() {
        dialogParticipante = new JDialog(this, "Datos del Participante", true);
        dialogParticipante.setSize(400, 500);
        dialogParticipante.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);
        
        // Apellido
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        panel.add(txtApellido, gbc);
        
        // Edad
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        txtEdad = new JTextField(20);
        panel.add(txtEdad, gbc);
        
        // Sexo
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        String[] sexos = {"Masculino", "Femenino", "Otro"};
        cmbSexo = new JComboBox<>(sexos);
        panel.add(cmbSexo, gbc);
        
        // Dirección
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(20);
        panel.add(txtDireccion, gbc);
        
        // Teléfono
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        panel.add(txtTelefono, gbc);
        
        // Correo
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        txtCorreo = new JTextField(20);
        panel.add(txtCorreo, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> {
            if (validarDatosParticipante()) {
                dialogParticipante.dispose();
            }
        });
        
        btnCancelar.addActionListener(e -> {
            dialogParticipante.dispose();
            txtDni.setText("");
        });
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        dialogParticipante.add(panel);
    }
    
    private boolean validarDatosParticipante() {
        if (txtNombre.getText().trim().isEmpty() ||
            txtApellido.getText().trim().isEmpty() ||
            txtEdad.getText().trim().isEmpty() ||
            txtDireccion.getText().trim().isEmpty() ||
            txtTelefono.getText().trim().isEmpty() ||
            txtCorreo.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(dialogParticipante,
                "Por favor complete todos los campos.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            if (edad < 0 || edad > 120) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialogParticipante,
                "La edad debe ser un número válido entre 0 y 120.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void cargarActividades() {
        ActividadController actividadController = new ActividadController();
        java.util.List<Actividad> actividades = actividadController.listarActividades();
        
        cmbActividad.removeAllItems();
        for (Actividad actividad : actividades) {
            cmbActividad.addItem(actividad.getIdActividad() + " - " + actividad.getNombre());
        }
    }

    private void registrarParticipacion(ActionEvent event) {
        try {
            String dni = txtDni.getText().trim();
            String actividadSeleccionada = (String) cmbActividad.getSelectedItem();
            
            if (dni.isEmpty() || actividadSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese el DNI y seleccione una actividad",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar participante por DNI
            Participante participante = participanteController.buscarPorDni(dni);
            if (participante == null) {
                JOptionPane.showMessageDialog(this,
                    "No se encontró un participante con ese DNI",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int idActividad = Integer.parseInt(actividadSeleccionada.split(" - ")[0]);
            String resultado = (String) cmbResultado.getSelectedItem();
            String observaciones = txtObservaciones.getText();
            
            if (registroController.registrarParticipacion(participante.getIdParticipante(), idActividad, resultado, observaciones)) {
                JOptionPane.showMessageDialog(this,
                    "Registro de participación guardado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarActividades();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el registro. El participante ya está registrado en esta actividad.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al registrar participación: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtDni.setText("");
        cmbActividad.setSelectedIndex(0);
        cmbResultado.setSelectedIndex(0);
        txtObservaciones.setText("");
    }
}