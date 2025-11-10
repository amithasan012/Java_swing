package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private JButton btnUsers, btnTx, btnLogout;

    public AdminDashboard() {
        setTitle("Bank of UOB - Admin");
        setSize(500,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        btnUsers = new JButton("View Users");
        btnTx = new JButton("View Transactions");
        btnLogout = new JButton("Logout");
        JPanel p = new JPanel();
        p.add(btnUsers); p.add(btnTx); p.add(btnLogout);
        add(p);
        btnUsers.addActionListener(e -> showUsers());
        btnTx.addActionListener(e -> showTransactions());
        btnLogout.addActionListener(e -> {
            LoginFrame lf = new LoginFrame(); lf.setVisible(true); this.dispose();
        });
    }

    private void showUsers() {
        List<User> users = UserDAO.getAllUsers();
        String[] cols = {"Account No", "Username", "Email", "Phone", "Balance"};
        Object[][] data = new Object[users.size()][cols.length];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i][0] = u.getAccountNo();
            data[i][1] = u.getUsername();
            data[i][2] = u.getEmail();
            data[i][3] = u.getPhone();
            data[i][4] = u.getBalance();
        }
        JTable table = new JTable(data, cols);
        JScrollPane sp = new JScrollPane(table);
        int res = JOptionPane.showConfirmDialog(this, sp, "All Users - Select a row to delete then press Yes to remove selected", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String acc = (String)table.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete user " + acc + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = UserDAO.deleteUser(acc);
                    JOptionPane.showMessageDialog(this, ok ? "Deleted" : "Delete failed");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No row selected");
            }
        }
    }

    private void showTransactions() {
        java.util.List<String[]> txs = TransactionDAO.getAllTransactions();
        StringBuilder sb = new StringBuilder();
        for (String[] r : txs) {
            sb.append(String.join(" | ", r)).append("\n");
        }
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "All Transactions", JOptionPane.PLAIN_MESSAGE);
    }
}
