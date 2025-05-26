package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.table.*;
import model.*;
import controller.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.database.DatabaseConnection;

public class DashboardView extends BaseForm {
    private JPanel mainPanel;
    private JPanel statsPanel;
    private JPanel chartsPanel;
    private JTable indicatorsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> timeRangeCombo;
    private JComboBox<String> campaignTypeCombo;
    
    private Connection connection;
    
    public DashboardView() {
        super("Dashboard - Registro Digital de Campañas de Salud");
        connection = DatabaseConnection.getConnection();
        
        setupLayout();
        initializeComponents();
        loadData();
    }
    
    @Override
    protected void setupLayout() {
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top controls panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeRangeCombo = new JComboBox<>(new String[]{"Último mes", "Últimos 3 meses", "Último año", "Todo"});
        campaignTypeCombo = new JComboBox<>(new String[]{"Todas las campañas", "Vacunación", "Chequeo médico", "Educación sanitaria"});
        
        controlsPanel.add(new JLabel("Período:"));
        controlsPanel.add(timeRangeCombo);
        controlsPanel.add(new JLabel("Tipo de campaña:"));
        controlsPanel.add(campaignTypeCombo);
        
        // Stats panel with key indicators
        statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        addStatCard("Total Campañas", "0");
        addStatCard("Total Participantes", "0");
        addStatCard("Cobertura Promedio", "0%");
        addStatCard("Actividades Completadas", "0");
        
        // Charts panel - smaller size
        chartsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        chartsPanel.setPreferredSize(new Dimension(800, 400));
        
        // Create a container for stats and charts with proper sizing
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        statsPanel.setPreferredSize(new Dimension(800, 150));
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(chartsPanel, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(controlsPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to form
        add(mainPanel);
        
        // Add event listeners
        timeRangeCombo.addActionListener(e -> updateDashboard());
        campaignTypeCombo.addActionListener(e -> updateDashboard());
    }
    
    private void addStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        statsPanel.add(card);
    }
    
    private void loadData() {
        // Load initial data
        updateDashboard();
    }
    
    private void updateDashboard() {
        // Update statistics
        updateStatistics();
        
        // Update charts
        updateCharts();
    }
    
    private void updateStatistics() {
        // Get data based on selected filters
        String timeRange = (String) timeRangeCombo.getSelectedItem();
        String campaignType = (String) campaignTypeCombo.getSelectedItem();
        
        // Update statistics cards with real data
        int totalCampanas = getTotalCampanas();
        int totalParticipantes = getTotalParticipantes();
        double coberturaPromedio = getCoberturaPromedio();
        int actividadesCompletadas = getActividadesCompletadas();
        
        // Update the stat cards
        updateStatCard(0, String.valueOf(totalCampanas));
        updateStatCard(1, String.valueOf(totalParticipantes));
        updateStatCard(2, String.format("%.1f%%", coberturaPromedio));
        updateStatCard(3, String.valueOf(actividadesCompletadas));
    }
    
    private void updateStatCard(int index, String value) {
        JPanel card = (JPanel) statsPanel.getComponent(index);
        JLabel valueLabel = (JLabel) card.getComponent(1);
        valueLabel.setText(value);
    }
    
    private void updateCharts() {
        // Clear existing charts
        chartsPanel.removeAll();
        
        // Create new charts
        createParticipationTrendChart();
        createCampaignTypeChart();
        createCoverageChart();
        createCompletionRateChart();
        
        // Refresh the panel
        chartsPanel.revalidate();
        chartsPanel.repaint();
    }
    
    private void createParticipationTrendChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Tendencia de Participación"));
        
        // Obtener datos de participación por mes
        Map<String, Integer> participacionPorMes = new TreeMap<>();
        String query = """
            SELECT YEAR(a.fecha) * 100 + MONTH(a.fecha) as mes_num, 
                   CAST(YEAR(a.fecha) AS VARCHAR) + '-' + 
                   RIGHT('0' + CAST(MONTH(a.fecha) AS VARCHAR), 2) as mes, 
                   COUNT(rp.idParticipante) as total
            FROM Actividad a
            LEFT JOIN RegistroParticipacion rp ON a.idActividad = rp.idActividad
            GROUP BY YEAR(a.fecha), MONTH(a.fecha)
            ORDER BY mes_num
        """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                participacionPorMes.put(rs.getString("mes"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
            "Participación por Mes",
            "Mes",
            "Total Participantes",
            createDataset(participacionPorMes),
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        ChartPanel chartPanelComponent = new ChartPanel(chart);
        chartPanelComponent.setPreferredSize(new Dimension(350, 180));
        chartPanelComponent.setMaximumSize(new Dimension(350, 180));
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
        chartsPanel.add(chartPanel);
    }
    
    private void createCampaignTypeChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Campañas por Estado"));
        
        // Obtener datos de campañas por estado
        Map<String, Integer> campanasPorTipo = new HashMap<>();
        String query = "SELECT estado, COUNT(*) as total FROM Campana GROUP BY estado";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                campanasPorTipo.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Crear gráfico circular
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribución de Campañas por Estado",
            createPieDataset(campanasPorTipo),
            true,
            true,
            false
        );
        
        ChartPanel chartPanelComponent = new ChartPanel(chart);
        chartPanelComponent.setPreferredSize(new Dimension(350, 180));
        chartPanelComponent.setMaximumSize(new Dimension(350, 180));
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
        chartsPanel.add(chartPanel);
    }
    
    private void createCoverageChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Cobertura por Actividad"));
        
        // Obtener los filtros seleccionados
        String timeRange = (String) timeRangeCombo.getSelectedItem();
        String campaignType = (String) campaignTypeCombo.getSelectedItem();
        
        // Obtener datos de cobertura con filtros
        Map<String, Double> coberturaPorActividad = new LinkedHashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("""
            SELECT a.nombre, 
                   CAST(a.participantesRegistrados AS FLOAT) / NULLIF(a.capacidad, 0) * 100 as cobertura
            FROM Actividad a
            INNER JOIN Campana c ON a.idCampana = c.idCampana
            WHERE 1=1
        """);
        
        // Aplicar filtro de período
        if (timeRange != null && !timeRange.equals("Todo")) {
            switch (timeRange) {
                case "Último mes":
                    queryBuilder.append(" AND a.fecha >= DATEADD(MONTH, -1, GETDATE())");
                    break;
                case "Últimos 3 meses":
                    queryBuilder.append(" AND a.fecha >= DATEADD(MONTH, -3, GETDATE())");
                    break;
                case "Último año":
                    queryBuilder.append(" AND a.fecha >= DATEADD(YEAR, -1, GETDATE())");
                    break;
            }
        }
        
        // Aplicar filtro de tipo de campaña (por estado de campaña)
        if (campaignType != null && !campaignType.equals("Todas las campañas")) {
            switch (campaignType) {
                case "Vacunación":
                    queryBuilder.append(" AND (a.nombre LIKE '%vacun%' OR a.descripcion LIKE '%vacun%')");
                    break;
                case "Chequeo médico":
                    queryBuilder.append(" AND (a.nombre LIKE '%chequeo%' OR a.nombre LIKE '%medico%' OR a.descripcion LIKE '%chequeo%' OR a.descripcion LIKE '%medico%')");
                    break;
                case "Educación sanitaria":
                    queryBuilder.append(" AND (a.nombre LIKE '%educacion%' OR a.nombre LIKE '%sanitaria%' OR a.descripcion LIKE '%educacion%' OR a.descripcion LIKE '%sanitaria%')");
                    break;
            }
        }
        
        queryBuilder.append(" ORDER BY a.nombre");
        String query = queryBuilder.toString();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                coberturaPorActividad.put(rs.getString("nombre"), rs.getDouble("cobertura"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
            "Cobertura por Actividad",
            "Actividad",
            "Cobertura (%)",
            createDataset(coberturaPorActividad),
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        ChartPanel chartPanelComponent = new ChartPanel(chart);
        chartPanelComponent.setPreferredSize(new Dimension(350, 180));
        chartPanelComponent.setMaximumSize(new Dimension(350, 180));
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
        chartsPanel.add(chartPanel);
    }
    
    private void createCompletionRateChart() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Tasa de Completitud"));
        
        // Obtener datos de completitud
        Map<String, Integer> completitudPorEstado = new HashMap<>();
        String query = "SELECT estado, COUNT(*) as total FROM Actividad GROUP BY estado";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                completitudPorEstado.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Crear gráfico circular
        JFreeChart chart = ChartFactory.createPieChart(
            "Estado de Actividades",
            createPieDataset(completitudPorEstado),
            true,
            true,
            false
        );
        
        ChartPanel chartPanelComponent = new ChartPanel(chart);
        chartPanelComponent.setPreferredSize(new Dimension(350, 180));
        chartPanelComponent.setMaximumSize(new Dimension(350, 180));
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
        chartsPanel.add(chartPanel);
    }
    
    private DefaultCategoryDataset createDataset(Map<String, ? extends Number> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Valor", entry.getKey());
        }
        return dataset;
    }
    
    private DefaultPieDataset createPieDataset(Map<String, ? extends Number> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return dataset;
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    // Statistics methods
    private int getTotalCampanas() {
        String query = "SELECT COUNT(*) as total FROM Campana";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private int getTotalParticipantes() {
        String query = "SELECT COUNT(*) as total FROM Participante";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double getCoberturaPromedio() {
        String query = """
            SELECT AVG(CAST(a.participantesRegistrados AS FLOAT) / NULLIF(a.capacidad, 0)) * 100 as cobertura
            FROM Actividad a
            JOIN Campana c ON a.idCampana = c.idCampana
            WHERE a.estado = 'finalizada'
        """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getDouble("cobertura");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    private int getActividadesCompletadas() {
        String query = "SELECT COUNT(*) as total FROM Actividad WHERE estado = 'finalizada'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
} 