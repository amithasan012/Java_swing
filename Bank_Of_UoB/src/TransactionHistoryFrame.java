// TransactionHistoryFrame.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionHistoryFrame extends JFrame {
    private User currentUser;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    public TransactionHistoryFrame(User user) {
        this.currentUser = user;
        setTitle("Transaction History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        
        initComponents();
        loadTransactionData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Transaction History - " + currentUser.getUsername(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"Date", "Type", "Amount", "Target Account", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadTransactionData() {
        try {
            ResultSet rs = BankDAO.getUserTransactions(currentUser.getAccountNo());
            tableModel.setRowCount(0); // Clear existing data
            
            while (rs != null && rs.next()) {
                String date = rs.getTimestamp("transaction_date").toString();
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String targetAccount = rs.getString("target_account");
                String description = rs.getString("description");
                
                // Format amount with currency symbol
                String formattedAmount = "$" + amount;
                
                tableModel.addRow(new Object[]{date, type, formattedAmount, targetAccount, description});
            }
            
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No transactions found", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading transaction history", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}