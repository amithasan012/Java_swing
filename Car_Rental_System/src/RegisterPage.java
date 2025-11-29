import javax.swing.*;
import java.sql.*;

public class RegisterPage extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public RegisterPage() {
        setTitle("Register");
        setSize(400, 300);
        setLayout(null);

        JLabel u = new JLabel("New Username:");
        u.setBounds(50, 50, 150, 30);
        add(u);

        userField = new JTextField();
        userField.setBounds(180, 50, 150, 30);
        add(userField);

        JLabel p = new JLabel("New Password:");
        p.setBounds(50, 100, 150, 30);
        add(p);

        passField = new JPasswordField();
        passField.setBounds(180, 100, 150, 30);
        add(passField);

        JButton regBtn = new JButton("Register");
        regBtn.setBounds(120, 160, 120, 30);
        add(regBtn);

        regBtn.addActionListener(e -> register());

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void register() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO users(username,password,role) VALUES(?,?,?)"
            );
            pst.setString(1, userField.getText());
            pst.setString(2, new String(passField.getPassword()));
            pst.setString(3, "user");

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful!");
            new LoginPage();
            dispose();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
