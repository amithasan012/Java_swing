import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    JTextField userField;
    JPasswordField passField;

    public LoginPage() {
        setTitle("Car Renting System - Login");
        setSize(400, 300);
        setLayout(null);

        JLabel u = new JLabel("Username:");
        u.setBounds(50, 50, 100, 30);
        add(u);

        userField = new JTextField();
        userField.setBounds(150, 50, 150, 30);
        add(userField);

        JLabel p = new JLabel("Password:");
        p.setBounds(50, 100, 100, 30);
        add(p);

        passField = new JPasswordField();
        passField.setBounds(150, 100, 150, 30);
        add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 160, 100, 30);
        add(loginBtn);

        JButton regBtn = new JButton("Register");
        regBtn.setBounds(180, 160, 120, 30);
        add(regBtn);

        loginBtn.addActionListener(e -> login());
        regBtn.addActionListener(e -> { new RegisterPage(); dispose(); });

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void login() {
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?"
            );
            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                if (role.equals("admin")) {
                    new AdminDashboard();
                } else {
                    new UserDashboard(rs.getInt("id"));
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
