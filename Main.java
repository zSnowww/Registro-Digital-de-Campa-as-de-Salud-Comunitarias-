import view.LoginView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando aplicación...");
        
        try {
            // Set system look and feel
            System.out.println("Configurando look and feel...");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Look and feel configurado correctamente");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Error al configurar look and feel: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Creando LoginView...");
        SwingUtilities.invokeLater(() -> {
            try {
                LoginView loginView = new LoginView();
                System.out.println("LoginView creada correctamente");
                loginView.setVisible(true);
                System.out.println("LoginView visible");
            } catch (Exception e) {
                System.err.println("Error al crear LoginView: " + e.getMessage());
                e.printStackTrace();
            }
        });
        System.out.println("Aplicación iniciada");
    }
} 