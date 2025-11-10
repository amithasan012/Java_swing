package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RegisterFrame extends JFrame {
    private JTextField txtUser, txtEmail, txtPhone, txtAddress;
    private JPasswordField txtPass;
    private JButton btnRegister;
    private JFrame parent;

    public RegisterFrame(JFrame parent) {
        this.parent = parent;
        setTitle("Bank of UOB - Register");
        setSize(450,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Username:"), c);
        txtUser = new JTextField(18); c.gridx = 1; p.add(txtUser, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Email:"), c);
        txtEmail = new JTextField(18); c.gridx = 1; p.add(txtEmail, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Phone:"), c);
        txtPhone = new JTextField(18); c.gridx = 1; p.add(txtPhone, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Address:"), c);
        txtAddress = new JTextField(18); c.gridx = 1; p.add(txtAddress, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Password:"), c);
        txtPass = new JPasswordField(18); c.gridx = 1; p.add(txtPass, c);

        btnRegister = new JButton("Register");
        c.gridx = 0; c.gridy++; c.gridwidth = 2; p.add(btnRegister, c);

        add(p);

        btnRegister.addActionListener(e -> register());
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                parent.setVisible(true);
            }
        });
    }

    private void register() {
        String username = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        if (username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, email and password required");
            return;
        }

        String accNo = generateAccountNo();
        User u = new User(accNo, username, email, phone, address, pass, 1000.0, "datiara");
        boolean ok = UserDAO.registerUser(u);
        if (ok) {
            // log initial deposit as well (already balance is 1000 default)
            TransactionDAO.logTransaction("DEPOSIT", accNo, accNo, 1000.0, "Initial deposit");
            JOptionPane.showMessageDialog(this, "Registered. Account No: " + accNo + "\nDefault balance: 1000.00");
            this.dispose();
            parent.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed (maybe username/email in use).");
        }
    }

    private String generateAccountNo() {
        long ts = System.currentTimeMillis();
        int r = new Random().nextInt(900) + 100;
        return "UOB" + (ts % 1000000) + r;
    }
}
