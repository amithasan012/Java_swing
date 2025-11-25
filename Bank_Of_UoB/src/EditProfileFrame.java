// EditProfileFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditProfileFrame extends JFrame {
    private User currentUser;
    private JTextField usernameField, emailField, phoneField, addressField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton updateButton;
    
    public EditProfileFrame(User user) {
        this.currentUser = user;
        setTitle("Edit Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Edit Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Populate fields with current data
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(currentUser.getUsername());
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(currentUser.getEmail());
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(currentUser.getPhone());
        formPanel.add(phoneField);
        
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField(currentUser.getAddress());
        formPanel.add(addressField);
        
        formPanel.add(new JLabel("New Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        updateButton = new JButton("Update Profile");
        updateButton.setBackground(new Color(0, 100, 0));
        updateButton.setForeground(Color.BLACK);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(Color.BLACK);
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        updateButton.addActionListener(e -> updateProfile());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void updateProfile() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if password is being changed
        if (!password.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(this, 
                    "Password must be at least 4 characters long", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // Keep old password if not changed
            password = currentUser.getPassword();
        }
        
        // Check if username or email already exists (excluding current user)
        if (!username.equals(currentUser.getUsername()) || !email.equals(currentUser.getEmail())) {
            String checkSql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND account_no != ?";
            try (java.sql.Connection conn = DBConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, currentUser.getAccountNo());
                
                java.sql.ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "Username or email already exists", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Update user object
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);
        currentUser.setPassword(password);
        
        if (BankDAO.updateUserProfile(currentUser)) {
            JOptionPane.showMessageDialog(this, 
                "Profile updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Profile update failed. Please try again.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}