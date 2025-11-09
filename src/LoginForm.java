import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnRegister;

    public LoginForm() {
        setTitle("Login Page");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("User Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        formPanel.add(new JLabel("Username:"));
        txtUser = new JTextField();
        formPanel.add(txtUser);

        formPanel.add(new JLabel("Password:"));
        txtPass = new JPasswordField();
        formPanel.add(txtPass);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        btnLogin.setBackground(new Color(0, 153, 76));
        btnLogin.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(0, 102, 204));
        btnRegister.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnRegister.setFocusPainted(false);
        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
        add(btnPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> {
            new RegisterForm();
            dispose();
        });

        setVisible(true);
    }

    private void login() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new WelcomePage(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
