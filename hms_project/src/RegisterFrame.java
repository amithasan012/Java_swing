import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterFrame extends JFrame {

    JTextField tfUser;
    JPasswordField tfPass;
    JComboBox<String> roleBox;

    public RegisterFrame() {
        setTitle("Register");
        setSize(350, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(30, 40, 100, 30);
        add(l1);

        tfUser = new JTextField();
        tfUser.setBounds(120, 40, 150, 30);
        add(tfUser);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(30, 90, 100, 30);
        add(l2);

        tfPass = new JPasswordField();
        tfPass.setBounds(120, 90, 150, 30);
        add(tfPass);

        JLabel l3 = new JLabel("Role:");
        l3.setBounds(30, 140, 100, 30);
        add(l3);

        String roles[] = {"user", "staff"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(120, 140, 150, 30);
        add(roleBox);

        JButton reg = new JButton("Register");
        reg.setBounds(120, 190, 100, 30);
        add(reg);

        reg.addActionListener(e -> registerUser());
        setVisible(true);
    }

    void registerUser() {
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username,password,role) VALUES(?,?,?)");

            ps.setString(1, tfUser.getText());
            ps.setString(2, new String(tfPass.getPassword()));
            ps.setString(3, (String) roleBox.getSelectedItem());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
