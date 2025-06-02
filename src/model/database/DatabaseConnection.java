package model.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for handling database connections to SQL Server
 */
public class DatabaseConnection {
    private static final String CONFIG_FILE = "src/config/DatabaseConfig.properties";
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;
    
    static {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(CONFIG_FILE));
            
            DRIVER = props.getProperty("db.driver");
            URL = props.getProperty("db.url"); // Usar la URL completa del archivo de configuración
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
            
            // Cargar el driver
            Class.forName(DRIVER);
            System.out.println("✓ Database configuration loaded successfully");
            System.out.println("URL: " + URL);
            System.out.println("User: " + USER);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a connection to the database with retry logic
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        SQLException lastException = null;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                System.out.println("🔄 Attempting database connection (attempt " + attempt + "/" + MAX_RETRIES + ")");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connection successful!");
                return connection;
            } catch (SQLException e) {
                lastException = e;
                System.err.println("❌ Connection attempt " + attempt + " failed: " + e.getMessage());
                
                if (attempt < MAX_RETRIES) {
                    try {
                        System.out.println("⏳ Waiting " + RETRY_DELAY_MS + "ms before retry...");
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection interrupted", ie);
                    }
                }
            }
        }
        
        throw new SQLException("Failed to connect after " + MAX_RETRIES + " attempts", lastException);
    }
    
    /**
     * Tests the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("✅ Database connection test successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Closes a connection safely
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Database connection closed successfully");
            } catch (SQLException e) {
                System.err.println("❌ Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gets the database URL for debugging purposes
     * @return the database URL
     */
    public static String getDatabaseURL() {
        return URL;
    }
    
    /**
     * Gets the database user for debugging purposes
     * @return the database user
     */
    public static String getDatabaseUser() {
        return USER;
    }
}