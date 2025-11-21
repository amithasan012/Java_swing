import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField tfUser;
    JPasswordField tfPass;

    public LoginFrame() {
        setTitle("Login - HMS");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(30, 40, 100, 30);
        add(l1);

        tfUser = new JTextField();
        tfUser.setBounds(120, 40, 150, 30);
        add(tfUser);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(30, 80, 100, 30);
        add(l2);

        tfPass = new JPasswordField();
        tfPass.setBounds(120, 80, 150, 30);
        add(tfPass);

        JButton login = new JButton("Login");
        login.setBounds(70, 130, 80, 30);
        add(login);

        JButton register = new JButton("Register");
        register.setBounds(160, 130, 100, 30);
        add(register);

        login.addActionListener(e -> loginUser());
        register.addActionListener(e -> new RegisterFrame());

        setVisible(true);
    }

    void loginUser() {
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT role FROM users WHERE username=? AND password=?");

            ps.setString(1, tfUser.getText());
            ps.setString(2, new String(tfPass.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                dispose();

                if (role.equals("admin")) new AdminDashboard();
                else if (role.equals("user")) new UserDashboard(tfUser.getText());
                else if (role.equals("staff")) new StaffDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
