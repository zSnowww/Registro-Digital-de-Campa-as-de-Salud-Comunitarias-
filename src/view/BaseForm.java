package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseForm extends JFrame {
    protected JPanel mainPanel;
    protected JPanel contentPanel;
    protected JPanel buttonPanel;
    
    public BaseForm(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        
        // Configurar para que se abra en el monitor secundario
        setLocationForSecondaryMonitor();
        
        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
    }
    
    /**
     * Configura la ventana para que se abra en el monitor donde está activo NetBeans
     */
    private void setLocationForSecondaryMonitor() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        
        if (screens.length > 1) {
            // Detectar en qué monitor está NetBeans buscando ventanas de Java
            GraphicsDevice targetMonitor = detectNetBeansMonitor(screens);
            
            if (targetMonitor != null) {
                Rectangle bounds = targetMonitor.getDefaultConfiguration().getBounds();
                
                // Centrar la ventana en el monitor donde está NetBeans
                int x = bounds.x + (bounds.width - getWidth()) / 2;
                int y = bounds.y + (bounds.height - getHeight()) / 2;
                setLocation(x, y);
            } else {
                // Si no se puede detectar NetBeans, usar el segundo monitor por defecto
                GraphicsDevice secondMonitor = screens[1];
                Rectangle bounds = secondMonitor.getDefaultConfiguration().getBounds();
                
                int x = bounds.x + (bounds.width - getWidth()) / 2;
                int y = bounds.y + (bounds.height - getHeight()) / 2;
                setLocation(x, y);
            }
        } else {
            // Si solo hay un monitor, centrar normalmente
            setLocationRelativeTo(null);
        }
    }
    
    /**
     * Detecta en qué monitor está ejecutándose NetBeans
     */
    private GraphicsDevice detectNetBeansMonitor(GraphicsDevice[] screens) {
        try {
            // Obtener todas las ventanas abiertas
            Window[] windows = Window.getWindows();
            
            // Buscar ventanas que puedan ser NetBeans
            for (Window window : windows) {
                if (window.isVisible() && window instanceof Frame) {
                    Frame frame = (Frame) window;
                    String title = frame.getTitle();
                    
                    // Buscar ventanas que contengan "NetBeans" en el título
                    if (title != null && (title.toLowerCase().contains("netbeans") || 
                                        title.toLowerCase().contains("apache netbeans") ||
                                        title.toLowerCase().contains("appinforme"))) {
                        
                        // Determinar en qué monitor está esta ventana
                        Point location = frame.getLocation();
                        for (GraphicsDevice screen : screens) {
                            Rectangle bounds = screen.getDefaultConfiguration().getBounds();
                            if (bounds.contains(location)) {
                                return screen;
                            }
                        }
                    }
                }
            }
            
            // Si no encuentra NetBeans, buscar cualquier ventana de Java IDE
            for (Window window : windows) {
                if (window.isVisible() && window instanceof Frame) {
                    Frame frame = (Frame) window;
                    String className = frame.getClass().getName();
                    
                    // Buscar por nombres de clase típicos de IDEs
                    if (className.contains("netbeans") || className.contains("ide") || 
                        className.contains("MainFrame") || className.contains("ProjectFrame")) {
                        
                        Point location = frame.getLocation();
                        for (GraphicsDevice screen : screens) {
                            Rectangle bounds = screen.getDefaultConfiguration().getBounds();
                            if (bounds.contains(location)) {
                                return screen;
                            }
                        }
                    }
                }
            }
            
            // Como último recurso, usar el cursor del mouse para detectar el monitor activo
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            for (GraphicsDevice screen : screens) {
                Rectangle bounds = screen.getDefaultConfiguration().getBounds();
                if (bounds.contains(mouseLocation)) {
                    return screen;
                }
            }
            
        } catch (Exception e) {
            // Si hay algún error, continuar con el comportamiento por defecto
            System.out.println("No se pudo detectar el monitor de NetBeans: " + e.getMessage());
        }
        
        return null; // No se pudo detectar
    }
    
    private void initComponents() {
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Content panel (center)
        contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Button panel (south)
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add panels to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    protected abstract void setupLayout();
    
    protected void onWindowClosing() {
        // Override this method in subclasses if needed
    }
    
    protected JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }
    
    protected JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }
    
    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(150, 30));
        return label;
    }
    
    protected JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }
    
    protected JTable createTable(String[] columnNames) {
        JTable table = new JTable(new javax.swing.table.DefaultTableModel(columnNames, 0));
        table.setFillsViewportHeight(true);
        return table;
    }
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected boolean showConfirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirmar", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
} 