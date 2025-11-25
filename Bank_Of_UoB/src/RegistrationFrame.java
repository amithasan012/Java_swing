// RegistrationFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField, emailField, phoneField, addressField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, backButton;
    
    public RegistrationFrame() {
        setTitle("Bank of UOB - Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(20,27,50));
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account - Bank of UOB", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255,255,255));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.setBackground(new Color(20,27,50));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(255,255,255));
        formPanel.add(usernameLabel);
        //formPanel.add(new JLabel("Username:"));

        usernameField = new JTextField();
        formPanel.add(usernameField);
        
        //formPanel.add(new JLabel("Email:"));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(new Color(255,255,255));
        formPanel.add(emailLabel);
        emailField = new JTextField();
        formPanel.add(emailField);
        
        //formPanel.add(new JLabel("Phone:"));
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(new Color(255,255,255));
        formPanel.add(phoneLabel);
        phoneField = new JTextField();
        formPanel.add(phoneField);
        
        //formPanel.add(new JLabel("Address:"));
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(new Color(255,255,255));
        formPanel.add(addressLabel);
        addressField = new JTextField();
        formPanel.add(addressField);
        
        //formPanel.add(new JLabel("Password:"));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(255,255,255));
        formPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        //formPanel.add(new JLabel("Confirm Password:"));
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(new Color(255,255,255));
        formPanel.add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(20,27,50));
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");
        
        registerButton.setBackground(new Color(0, 100, 0));
        registerButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.BLACK);
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        registerButton.addActionListener(new RegisterListener());
        backButton.addActionListener(e ->{
            new LoginFrame().setVisible(true);
            dispose();

        } );
    }
    
    private class RegisterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validation
            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || 
                address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationFrame.this, 
                    "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegistrationFrame.this, 
                    "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(RegistrationFrame.this, 
                    "Password must be at least 4 characters long", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if user already exists
            if (BankDAO.userExists(username, email)) {
                JOptionPane.showMessageDialog(RegistrationFrame.this, 
                    "Username or email already exists", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new user
            String accountNo = BankDAO.generateAccountNo();
            User newUser = new User(accountNo, username, email, phone, address, 
                                   password, 1000.00, "Datiara");
            
            if (BankDAO.registerUser(newUser)) {
                JOptionPane.showMessageDialog(RegistrationFrame.this, 
                    "Registration successful!\nYour Account Number: " + accountNo + 
                    "\nInitial Balance: ₹1000.00", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(RegistrationFrame.this, 
                    "Registration failed. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}