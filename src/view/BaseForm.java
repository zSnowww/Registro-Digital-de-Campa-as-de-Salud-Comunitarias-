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
        setLocationRelativeTo(null);
        
        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
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