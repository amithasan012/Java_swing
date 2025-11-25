// LoginFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    
    public LoginFrame() {
        setTitle("Bank of UOB - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(20,27,50)); // Light cyan background
        
        // Title
        JLabel titleLabel = new JLabel("Bank of UOB - Datiara Branch", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255,255,255)); // Dark blue
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.setBackground(new Color(20,27,50)); // Match main panel background
        
        JLabel usernameLabel = new JLabel("Username/Email:");
        usernameLabel.setForeground(new Color(255,255,255)); // Dark blue
        usernameField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(255,255,255)); // Dark blue
        passwordField = new JPasswordField();
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(20,27,50)); // Match main panel background
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        
        loginButton.setBackground(new Color(0, 100, 0));
        loginButton.setForeground(Color.BLACK);
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.BLACK);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        formPanel.add(new JLabel()); // Empty cell
        formPanel.add(buttonPanel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Event listeners
        loginButton.addActionListener(new LoginListener());
        registerButton.addActionListener(e ->{
            openRegistration();
            dispose();
        } );
        
        // Enter key listener for login
        passwordField.addActionListener(new LoginListener());
    }
    
    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter both username and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for admin login
            if (username.equals("admin") && password.equals("admin123")) {
                new AdminDashboard().setVisible(true);
                dispose();
                return;
            }
            
            User user = BankDAO.authenticateUser(username, password);
            if (user != null) {
                new UserDashboard(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Invalid username/email or password", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openRegistration() {
        new RegistrationFrame().setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            // Corrected line - use getSystemLookAndFeelClassName()
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}