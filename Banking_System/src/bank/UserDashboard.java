package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class UserDashboard extends JFrame {
    private User user;
    private JLabel lblWelcome, lblBalance;
    private JButton btnDeposit, btnWithdraw, btnTransfer, btnBalance, btnEdit, btnTxHistory, btnLogout;

    public UserDashboard(User u) {
        this.user = u;
        setTitle("Bank of UOB - User Dashboard");
        setSize(500,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        lblWelcome = new JLabel("Welcome " + user.getUsername() + " to Bank of UOB (Branch: " + user.getBranch() + ")");
        lblBalance = new JLabel("Balance: " + user.getBalance());

        btnDeposit = new JButton("Deposit");
        btnWithdraw = new JButton("Withdraw");
        btnTransfer = new JButton("Transfer");
        btnBalance = new JButton("Check Balance");
        btnEdit = new JButton("Edit Profile");
        btnTxHistory = new JButton("Transaction History");
        btnLogout = new JButton("Logout");

        JPanel p = new JPanel(new GridLayout(0,2,8,8));
        p.add(btnDeposit); p.add(btnWithdraw);
        p.add(btnTransfer); p.add(btnBalance);
        p.add(btnEdit); p.add(btnTxHistory);
        p.add(btnLogout);

        add(lblWelcome, BorderLayout.NORTH);
        add(p, BorderLayout.CENTER);
        add(lblBalance, BorderLayout.SOUTH);

        btnDeposit.addActionListener(e -> doDeposit());
        btnWithdraw.addActionListener(e -> doWithdraw());
        btnTransfer.addActionListener(e -> doTransfer());
        btnBalance.addActionListener(e -> refreshBalance());
        btnEdit.addActionListener(e -> editProfile());
        btnTxHistory.addActionListener(e -> viewTransactions());
        btnLogout.addActionListener(e -> logout());
    }

    private void doDeposit() {
        String s = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (s == null) return;
        try {
            double amt = Double.parseDouble(s);
            if (BankService.deposit(user, amt)) {
                JOptionPane.showMessageDialog(this, "Deposit successful");
                refreshBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }

    private void doWithdraw() {
        String s = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (s == null) return;
        try {
            double amt = Double.parseDouble(s);
            if (BankService.withdraw(user, amt)) {
                JOptionPane.showMessageDialog(this, "Withdraw successful");
                refreshBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Withdraw failed (balance must remain >= 1000)");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }

    private void doTransfer() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JTextField txtTo = new JTextField();
        JTextField txtAmt = new JTextField();
        p.add(new JLabel("To Account No:")); p.add(txtTo);
        p.add(new JLabel("Amount:")); p.add(txtAmt);
        int res = JOptionPane.showConfirmDialog(this, p, "Transfer", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            String toAcc = txtTo.getText().trim();
            double amt = Double.parseDouble(txtAmt.getText().trim());
            if (BankService.transfer(user, toAcc, amt)) {
                JOptionPane.showMessageDialog(this, "Transfer successful");
                refreshBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed (check account, amount, or min balance)");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }

    private void refreshBalance() {
        // fetch latest from DB
        User latest = UserDAO.findByAccountNo(user.getAccountNo());
        if (latest != null) {
            user = latest;
            lblBalance.setText("Balance: " + user.getBalance());
        }
    }

    private void editProfile() {
        JTextField txtName = new JTextField(user.getUsername());
        JTextField txtEmail = new JTextField(user.getEmail());
        JTextField txtPhone = new JTextField(user.getPhone());
        JTextField txtAddress = new JTextField(user.getAddress());
        JPasswordField txtPass = new JPasswordField(user.getPassword());

        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(new JLabel("Username:")); p.add(txtName);
        p.add(new JLabel("Email:")); p.add(txtEmail);
        p.add(new JLabel("Phone:")); p.add(txtPhone);
        p.add(new JLabel("Address:")); p.add(txtAddress);
        p.add(new JLabel("Password:")); p.add(txtPass);

        int res = JOptionPane.showConfirmDialog(this, p, "Edit Profile", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        user.setUsername(txtName.getText().trim());
        user.setEmail(txtEmail.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setAddress(txtAddress.getText().trim());
        user.setPassword(new String(txtPass.getPassword()).trim());

        boolean ok = UserDAO.updateProfile(user);
        JOptionPane.showMessageDialog(this, ok ? "Profile updated" : "Update failed");
    }

    private void viewTransactions() {
        java.util.List<String[]> txs = TransactionDAO.getTransactionsFor(user.getAccountNo());
        StringBuilder sb = new StringBuilder();
        for (String[] r : txs) {
            sb.append(String.join(" | ", r)).append("\n");
        }
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Transaction History", JOptionPane.PLAIN_MESSAGE);
    }

    private void logout() {
        LoginFrame lf = new LoginFrame();
        lf.setVisible(true);
        this.dispose();
    }
}
