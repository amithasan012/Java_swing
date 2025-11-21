// LoginRegisterFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginRegisterFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // login components
    private JTextField loginUser = new JTextField(20);
    private JPasswordField loginPass = new JPasswordField(20);
    private JComboBox<String> loginRole = new JComboBox<>(new String[] {"ADMIN","DOCTOR","STAFF","USER"});

    // register components
    private JTextField regUser = new JTextField(20);
    private JPasswordField regPass = new JPasswordField(20);
    private JComboBox<String> regRole = new JComboBox<>(new String[] {"DOCTOR","STAFF","USER"});
    private JTextField regName = new JTextField(20);

    public LoginRegisterFrame() {
        setTitle("Hospital Management System - Login / Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 320);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; loginPanel.add(loginUser, gbc);
        gbc.gridx = 0; gbc.gridy++; loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; loginPanel.add(loginPass, gbc);
        gbc.gridx = 0; gbc.gridy++; loginPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; loginPanel.add(loginRole, gbc);

        JButton btnLogin = new JButton("Login");
        JButton btnToRegister = new JButton("Go to Register");
        gbc.gridx = 0; gbc.gridy++; loginPanel.add(btnLogin, gbc);
        gbc.gridx = 1; loginPanel.add(btnToRegister, gbc);

        // Register panel
        JPanel regPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints(); gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; regPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; regPanel.add(regName, gbc);
        gbc.gridx = 0; gbc.gridy++; regPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; regPanel.add(regUser, gbc);
        gbc.gridx = 0; gbc.gridy++; regPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; regPanel.add(regPass, gbc);
        gbc.gridx = 0; gbc.gridy++; regPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; regPanel.add(regRole, gbc);

        JButton btnRegister = new JButton("Register");
        JButton btnToLogin = new JButton("Go to Login");
        gbc.gridx = 0; gbc.gridy++; regPanel.add(btnRegister, gbc);
        gbc.gridx = 1; regPanel.add(btnToLogin, gbc);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(regPanel, "REGISTER");
        cardLayout.show(mainPanel, "LOGIN");

        add(mainPanel);

        // actions
        btnToRegister.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        btnToLogin.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        btnRegister.addActionListener(e -> doRegister());
        btnLogin.addActionListener(e -> doLogin());
    }

    private void doRegister() {
        String username = regUser.getText().trim();
        String password = new String(regPass.getPassword()).trim();
        String role = (String) regRole.getSelectedItem();
        String fullname = regName.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password");
            return;
        }

        Connection c = DB.getConnection();
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement("INSERT INTO users (username,password,role,full_name) VALUES (?,?,?,?)");
            ps.setString(1, username);
            ps.setString(2, password); // simple example: plain text
            ps.setString(3, role);
            ps.setString(4, fullname);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registered. You can login now.");
            cardLayout.show(mainPanel, "LOGIN");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Register failed: " + ex.getMessage());
        } finally {
            DB.close(null, ps);
        }
    }

    private void doLogin() {
        String username = loginUser.getText().trim();
        String password = new String(loginPass.getPassword()).trim();
        String role = (String) loginRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password");
            return;
        }

        Connection c = DB.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            rs = ps.executeQuery();
            if (rs.next()) {
                int uid = rs.getInt("id");
                String fullname = rs.getString("full_name");
                JOptionPane.showMessageDialog(this, "Welcome " + (fullname != null ? fullname : username) + "!");

                // open role specific dashboard
                dispose();
                switch (role) {
                    case "ADMIN":
                        new AdminDashboard(uid, username).setVisible(true);
                        break;
                    case "DOCTOR":
                        new DoctorDashboard(uid, username).setVisible(true);
                        break;
                    case "STAFF":
                        new StaffDashboard(uid, username).setVisible(true);
                        break;
                    default:
                        new UserDashboard(uid, username).setVisible(true);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or role");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage());
        } finally {
            DB.close(rs, ps);
        }
    }
}
