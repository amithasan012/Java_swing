// TransferFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TransferFrame extends JFrame {
    private User currentUser;
    private JTextField targetAccountField, amountField;
    private JButton transferButton;
    
    public TransferFrame(User user) {
        this.currentUser = user;
        setTitle("Transfer Money");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Transfer Money", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel(new GridLayout(1, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        infoPanel.add(new JLabel("Your Current Balance: ₹" + currentUser.getBalance()));
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        formPanel.add(new JLabel("Target Account No:"));
        targetAccountField = new JTextField();
        formPanel.add(targetAccountField);
        
        formPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        formPanel.add(amountField);
        
        formPanel.add(new JLabel());
        transferButton = new JButton("Transfer");
        transferButton.setBackground(new Color(0, 100, 0));
        transferButton.setForeground(Color.BLACK);
        formPanel.add(transferButton);
        
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        transferButton.addActionListener(e -> processTransfer());
    }
    
    private void processTransfer() {
        try {
            String targetAccount = targetAccountField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            
            if (targetAccount.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter target account number", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if transferring to own account
            if (targetAccount.equals(currentUser.getAccountNo())) {
                JOptionPane.showMessageDialog(this, 
                    "Cannot transfer to your own account", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if target account exists
            User targetUser = BankDAO.getUserByAccountNo(targetAccount);
            if (targetUser == null) {
                JOptionPane.showMessageDialog(this, 
                    "Target account not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double currentBalance = currentUser.getBalance();
            double newBalance = currentBalance - amount;
            
            if (newBalance < 1000) {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient balance! Minimum balance must be $1000\n" +
                    "Maximum you can transfer: $" + (currentBalance - 1000),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Start transfer transaction
            if (performTransfer(amount, targetUser, targetAccount)) {
                JOptionPane.showMessageDialog(this, 
                    "Transfer successful!\nAmount: $" + amount + 
                    "\nTo: " + targetAccount +
                    "\nNew Balance: $" + newBalance, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Transfer failed. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid amount", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean performTransfer(double amount, User targetUser, String targetAccount) {
        try {
            // Update sender's balance
            double senderNewBalance = currentUser.getBalance() - amount;
            if (!BankDAO.updateBalance(currentUser.getAccountNo(), senderNewBalance)) {
                return false;
            }
            
            // Update receiver's balance
            double receiverNewBalance = targetUser.getBalance() + amount;
            if (!BankDAO.updateBalance(targetAccount, receiverNewBalance)) {
                // Rollback sender's balance if receiver update fails
                BankDAO.updateBalance(currentUser.getAccountNo(), currentUser.getBalance());
                return false;
            }
            
            // Record transactions for both parties
            boolean senderTransaction = BankDAO.addTransaction(
                currentUser.getAccountNo(), "TRANSFER", amount, targetAccount, 
                "Transfer to " + targetAccount
            );
            
            boolean receiverTransaction = BankDAO.addTransaction(
                targetAccount, "TRANSFER", amount, currentUser.getAccountNo(), 
                "Transfer from " + currentUser.getAccountNo()
            );
            
            if (senderTransaction && receiverTransaction) {
                currentUser.setBalance(senderNewBalance);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}