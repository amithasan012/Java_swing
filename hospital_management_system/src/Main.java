// Main.java
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // initialize DB connection (lazy)
        DB.getConnection();
        SwingUtilities.invokeLater(() -> {
            new LoginRegisterFrame().setVisible(true);
        });
    }
}
