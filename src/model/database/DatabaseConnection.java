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
            String baseUrl = props.getProperty("db.url");
            URL = baseUrl + ";trustServerCertificate=true;connectRetryCount=3;connectRetryInterval=10;connectionTimeout=30";
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
            
            // Cargar el driver
            Class.forName(DRIVER);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
        }
    }
    
    /**
     * Get a new database connection
     * @return Connection object
     */
    public static Connection getConnection() {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                Connection newConnection = DriverManager.getConnection(URL, USER, PASSWORD);
                if (newConnection != null && !newConnection.isClosed()) {
                    return newConnection;
                }
            } catch (SQLException e) {
                System.err.println("Database Connection Error (Attempt " + (retryCount + 1) + "): " + e.getMessage());
                retryCount++;
                if (retryCount < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        throw new RuntimeException("Failed to establish database connection after " + MAX_RETRIES + " attempts");
    }
    
    /**
     * Close a database connection
     * @param conn The connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
} 