// UserManagementFrame.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManagementFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton removeButton, refreshButton;
    
    public UserManagementFrame() {
        setTitle("User Management - Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        initComponents();
        loadUserData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("User Management - All Users", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"Account No", "Username", "Email", "Phone", "Balance", "Branch"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        removeButton = new JButton("Remove Selected User");
        removeButton.setBackground(new Color(178, 34, 34));
        removeButton.setForeground(Color.BLACK);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.BLACK);
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.GRAY);
        closeButton.setForeground(Color.BLACK);
        
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        // Event listeners
        removeButton.addActionListener(e -> removeSelectedUser());
        refreshButton.addActionListener(e -> loadUserData());
        closeButton.addActionListener(e -> dispose());
    }
    
    private void loadUserData() {
        try {
            ResultSet rs = BankDAO.getAllUsers();
            tableModel.setRowCount(0); // Clear existing data
            
            while (rs != null && rs.next()) {
                String accountNo = rs.getString("account_no");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                double balance = rs.getDouble("balance");
                String branch = rs.getString("branch");
                
                tableModel.addRow(new Object[]{
                    accountNo, username, email, phone, 
                    "$" + balance, branch
                });
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading user data", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a user to remove", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String accountNo = (String) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to remove user:\n" +
            "Account: " + accountNo + "\n" +
            "Username: " + username + "\n\n" +
            "This action cannot be undone!",
            "Confirm User Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (BankDAO.deleteUser(accountNo)) {
                JOptionPane.showMessageDialog(this,
                    "User removed successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to remove user",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}