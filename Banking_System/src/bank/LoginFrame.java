package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtPass;
    private JButton btnLogin, btnRegister;

    public LoginFrame() {
        setTitle("Bank of UOB - Login");
        setSize(400,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Username or Email:"), c);
        txtLogin = new JTextField(18);
        c.gridx = 1; p.add(txtLogin, c);
        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Password:"), c);
        txtPass = new JPasswordField(18);
        c.gridx = 1; p.add(txtPass, c);

        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        JPanel bottom = new JPanel();
        bottom.add(btnLogin);
        bottom.add(btnRegister);

        add(p, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> {
            RegisterFrame rf = new RegisterFrame(this);
            rf.setVisible(true);
            this.setVisible(false);
        });
    }

    private void doLogin() {
        String id = txtLogin.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        if (id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter credentials");
            return;
        }

        if (id.equals("admin") && pass.equals("admin123")) {
            // open admin dashboard
            AdminDashboard ad = new AdminDashboard();
            ad.setVisible(true);
            this.dispose();
            return;
        }

        User u = UserDAO.login(id, pass);
        if (u != null) {
            UserDashboard ud = new UserDashboard(u);
            ud.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        }
    }
}
