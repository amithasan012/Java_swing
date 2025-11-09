import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    private JTextField txtUser, txtEmail, txtPhone, txtAddress;
    private JPasswordField txtPass, txtConfirmPass;
    private JButton btnRegister, btnBack;

    public RegisterForm() {
        setTitle("User Registration");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Create Account", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        formPanel.add(new JLabel("Username:"));
        txtUser = new JTextField();
        formPanel.add(txtUser);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        formPanel.add(txtAddress);

        formPanel.add(new JLabel("Password:"));
        txtPass = new JPasswordField();
        formPanel.add(txtPass);

        formPanel.add(new JLabel("Confirm Password:"));
        txtConfirmPass = new JPasswordField();
        formPanel.add(txtConfirmPass);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnRegister = new JButton("Register");
        btnBack = new JButton("Back to Login");
        btnRegister.setBackground(new Color(0, 153, 76));
        btnRegister.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(102, 102, 102));
        btnBack.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnBack.setFocusPainted(false);
        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);
        add(btnPanel, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> register());
        btnBack.addActionListener(e -> {
            new LoginForm();
            dispose();
        });

        setVisible(true);
    }

    private void register() {
        String username = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        String password = new String(txtPass.getPassword());
        String confirmPass = new String(txtConfirmPass.getPassword());

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() ||
            address.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users(username, password, email, phone, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, phone);
            pst.setString(5, address);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginForm();
            dispose();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
