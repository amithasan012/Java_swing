import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AllTransactionsFrame extends JFrame {
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    
    public AllTransactionsFrame() {
        setTitle("All Transactions - Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        loadAllTransactions();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("All Transactions - Bank of UOB", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"Date", "Account No", "Username", "Type", "Amount", "Target Account", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setAutoCreateRowSorter(true);
        
        // Set column widths
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Date
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Account No
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Username
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Type
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Amount
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Target Account
        transactionTable.getColumnModel().getColumn(6).setPreferredWidth(200); // Description
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.BLACK);
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(Color.GRAY);
        closeButton.setForeground(Color.BLACK);
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        // Event listeners
        refreshButton.addActionListener(e -> loadAllTransactions());
        closeButton.addActionListener(e -> dispose());
    }
    
    private void loadAllTransactions() {
        try {
            ResultSet rs = BankDAO.getAllTransactions();
            tableModel.setRowCount(0); // Clear existing data
            
            int count = 0;
            while (rs != null && rs.next()) {
                String date = rs.getTimestamp("transaction_date").toString();
                String accountNo = rs.getString("account_no");
                String username = rs.getString("username");
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String targetAccount = rs.getString("target_account");
                String description = rs.getString("description");
                
                // Format amount based on transaction type
                String formattedAmount = "$" + amount;
                if ("WITHDRAW".equals(type) || "TRANSFER".equals(type)) {
                    formattedAmount = "-₹" + amount;
                }
                
                tableModel.addRow(new Object[]{
                    date, accountNo, username, type, 
                    formattedAmount, targetAccount, description
                });
                count++;
            }
            
            // Update title with count
            JLabel titleLabel = (JLabel) ((JPanel) getContentPane().getComponent(0)).getComponent(0);
            titleLabel.setText("All Transactions - Bank of UOB (Total: " + count + " transactions)");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading transactions", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}