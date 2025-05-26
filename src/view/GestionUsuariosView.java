package view;

import controller.UsuarioController;
import model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class GestionUsuariosView extends JFrame {
    private JTable tblUsuarios;
    private JComboBox<String> cmbRoles;
    private JTextField txtBuscar;
    private JButton btnNuevoUsuario;
    private JButton btnEditar;
    private JButton btnEliminar;
    
    private final UsuarioController usuarioController;
    
    public GestionUsuariosView() {
        usuarioController = new UsuarioController();
        
        setTitle("Gestión de Usuarios");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        cargarUsuarios();
    }
    
    private void initComponents() {
        // Panel superior con búsqueda y botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(20);
        panelSuperior.add(txtBuscar);
        
        btnNuevoUsuario = new JButton("Nuevo Usuario");
        panelSuperior.add(btnNuevoUsuario);
        
        btnEditar = new JButton("Editar");
        panelSuperior.add(btnEditar);
        
        btnEliminar = new JButton("Eliminar");
        panelSuperior.add(btnEliminar);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de usuarios
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI/Usuario", "Correo", "Rol", "Último Acceso"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblUsuarios = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tblUsuarios);
        add(scrollPane, BorderLayout.CENTER);
        
        // Configurar búsqueda
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tblUsuarios.setRowSorter(sorter);
        
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabla();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabla();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabla();
            }
        });
        
        // Agregar listeners a los botones
        btnNuevoUsuario.addActionListener(e -> mostrarFormularioNuevoUsuario());
        btnEditar.addActionListener(e -> editarUsuarioSeleccionado());
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());
    }
    
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioController.listarUsuarios();
            DefaultTableModel model = (DefaultTableModel) tblUsuarios.getModel();
            model.setRowCount(0);
            
            for (Usuario usuario : usuarios) {
                if (usuario.getRol().equals("Administrador") || 
                    usuario.getRol().equals("Desarrollador") || 
                    usuario.getRol().equals("Supervisor")) {
                    model.addRow(new Object[]{
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getCorreo(),
                        usuario.getRol(),
                        usuario.getUltimoAcceso()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar usuarios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filtrarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tblUsuarios.getRowSorter();
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }
    
    private void mostrarFormularioNuevoUsuario() {
        JDialog dialog = new JDialog(this, "Nuevo Usuario", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Opción para seleccionar usuario existente o nuevo
        JComboBox<String> cmbTipoUsuario = new JComboBox<>(new String[]{"Usuario Nuevo", "Usuario Existente"});
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Tipo de Usuario:"), gbc);
        gbc.gridy = 1;
        panel.add(cmbTipoUsuario, gbc);
        
        // Panel para usuario existente
        JPanel panelExistente = new JPanel(new GridBagLayout());
        JTextField txtBuscarDNI = new JTextField(20);
        JComboBox<String> cmbUsuariosExistentes = new JComboBox<>();
        cmbUsuariosExistentes.setEditable(true);
        
        gbc.gridy = 0;
        panelExistente.add(new JLabel("Buscar por DNI:"), gbc);
        gbc.gridy = 1;
        panelExistente.add(txtBuscarDNI, gbc);
        gbc.gridy = 2;
        panelExistente.add(new JLabel("Seleccionar Usuario:"), gbc);
        gbc.gridy = 3;
        panelExistente.add(cmbUsuariosExistentes, gbc);
        
        // Panel para usuario nuevo
        JPanel panelNuevo = new JPanel(new GridBagLayout());
        JTextField txtNombre = new JTextField(20);
        JTextField txtApellido = new JTextField(20);
        JTextField txtDNI = new JTextField(20);
        JTextField txtCorreo = new JTextField(20);
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Administrador", "Desarrollador", "Supervisor"});
        
        gbc.gridy = 0;
        panelNuevo.add(new JLabel("Nombre:"), gbc);
        gbc.gridy = 1;
        panelNuevo.add(txtNombre, gbc);
        gbc.gridy = 2;
        panelNuevo.add(new JLabel("Apellido:"), gbc);
        gbc.gridy = 3;
        panelNuevo.add(txtApellido, gbc);
        gbc.gridy = 4;
        panelNuevo.add(new JLabel("DNI/Usuario:"), gbc);
        gbc.gridy = 5;
        panelNuevo.add(txtDNI, gbc);
        gbc.gridy = 6;
        panelNuevo.add(new JLabel("Correo:"), gbc);
        gbc.gridy = 7;
        panelNuevo.add(txtCorreo, gbc);
        gbc.gridy = 8;
        panelNuevo.add(new JLabel("Rol:"), gbc);
        gbc.gridy = 9;
        panelNuevo.add(cmbRol, gbc);
        
        // Agregar autocomplete para búsqueda de DNI
        txtBuscarDNI.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboUsuarios(txtBuscarDNI.getText());
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboUsuarios(txtBuscarDNI.getText());
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboUsuarios(txtBuscarDNI.getText());
            }
            
            private void actualizarComboUsuarios(String texto) {
                try {
                    List<Usuario> usuarios = usuarioController.buscarUsuariosPorDNI(texto);
                    cmbUsuariosExistentes.removeAllItems();
                    for (Usuario usuario : usuarios) {
                        cmbUsuariosExistentes.addItem(usuario.getDni() + " - " + usuario.getNombre() + " " + usuario.getApellido());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Error al buscar usuarios: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Cambiar panel según selección
        cmbTipoUsuario.addActionListener(e -> {
            panel.removeAll();
            if (cmbTipoUsuario.getSelectedIndex() == 0) {
                panel.add(panelNuevo, gbc);
            } else {
                panel.add(panelExistente, gbc);
            }
            panel.revalidate();
            panel.repaint();
        });
        
        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> {
            try {
                if (cmbTipoUsuario.getSelectedIndex() == 0) {
                    // Crear usuario nuevo
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setNombre(txtNombre.getText());
                    nuevoUsuario.setApellido(txtApellido.getText());
                    nuevoUsuario.setDni(txtDNI.getText());
                    nuevoUsuario.setCorreo(txtCorreo.getText());
                    nuevoUsuario.setRol((String) cmbRol.getSelectedItem());
                    nuevoUsuario.setContrasena("1"); // Contraseña por defecto
                    
                    if (usuarioController.registrarUsuario(nuevoUsuario)) {
                        JOptionPane.showMessageDialog(dialog,
                            "Usuario registrado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        cargarUsuarios();
                    }
                } else {
                    // Asignar rol a usuario existente
                    String seleccion = (String) cmbUsuariosExistentes.getSelectedItem();
                    if (seleccion != null) {
                        String dni = seleccion.split(" - ")[0];
                        Usuario usuario = usuarioController.buscarPorDNI(dni);
                        if (usuario != null) {
                            usuario.setRol((String) cmbRol.getSelectedItem());
                            if (usuarioController.actualizarUsuario(usuario)) {
                                JOptionPane.showMessageDialog(dialog,
                                    "Rol asignado exitosamente",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                                dialog.dispose();
                                cargarUsuarios();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error al guardar usuario: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void editarUsuarioSeleccionado() {
        int fila = tblUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un usuario",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idUsuario = (int) tblUsuarios.getValueAt(fila, 0);
        try {
            Usuario usuario = usuarioController.buscarPorId(idUsuario);
            if (usuario != null) {
                mostrarFormularioEdicion(usuario);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar usuario: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarFormularioEdicion(Usuario usuario) {
        JDialog dialog = new JDialog(this, "Editar Usuario", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField txtNombre = new JTextField(usuario.getNombre(), 20);
        JTextField txtApellido = new JTextField(usuario.getApellido(), 20);
        JTextField txtDNI = new JTextField(usuario.getDni(), 20);
        JTextField txtCorreo = new JTextField(usuario.getCorreo(), 20);
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Administrador", "Desarrollador", "Supervisor"});
        cmbRol.setSelectedItem(usuario.getRol());
        
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridy = 1;
        panel.add(txtNombre, gbc);
        gbc.gridy = 2;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridy = 3;
        panel.add(txtApellido, gbc);
        gbc.gridy = 4;
        panel.add(new JLabel("DNI/Usuario:"), gbc);
        gbc.gridy = 5;
        panel.add(txtDNI, gbc);
        gbc.gridy = 6;
        panel.add(new JLabel("Correo:"), gbc);
        gbc.gridy = 7;
        panel.add(txtCorreo, gbc);
        gbc.gridy = 8;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridy = 9;
        panel.add(cmbRol, gbc);
        
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> {
            try {
                usuario.setNombre(txtNombre.getText());
                usuario.setApellido(txtApellido.getText());
                usuario.setDni(txtDNI.getText());
                usuario.setCorreo(txtCorreo.getText());
                usuario.setRol((String) cmbRol.getSelectedItem());
                
                if (usuarioController.actualizarUsuario(usuario)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Usuario actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    cargarUsuarios();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error al actualizar usuario: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        gbc.gridy = 10;
        panel.add(panelBotones, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void eliminarUsuarioSeleccionado() {
        int fila = tblUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un usuario",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este usuario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int idUsuario = (int) tblUsuarios.getValueAt(fila, 0);
                if (usuarioController.eliminarUsuario(idUsuario)) {
                    JOptionPane.showMessageDialog(this,
                        "Usuario eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarUsuarios();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar usuario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 