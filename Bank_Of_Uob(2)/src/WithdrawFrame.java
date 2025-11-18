// WithdrawFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WithdrawFrame extends JFrame {
    private User currentUser;
    private JTextField amountField;
    private JButton withdrawButton;
    
    public WithdrawFrame(User user) {
        this.currentUser = user;
        setTitle("Withdraw Money");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Withdraw Money", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        infoPanel.add(new JLabel("Current Balance: ₹" + currentUser.getBalance()));
        infoPanel.add(new JLabel("Minimum Balance: ₹1000.00"));
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        formPanel.add(new JLabel("Amount to Withdraw:"));
        amountField = new JTextField();
        formPanel.add(amountField);
        
        formPanel.add(new JLabel());
        withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(new Color(178, 34, 34));
        withdrawButton.setForeground(Color.BLACK);
        formPanel.add(withdrawButton);
        
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        withdrawButton.addActionListener(e -> processWithdrawal());
    }
    
    private void processWithdrawal() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double currentBalance = currentUser.getBalance();
            double newBalance = currentBalance - amount;
            
            if (newBalance < 1000) {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient balance! Minimum balance must be $1000\n" +
                    "Maximum you can withdraw: $" + (currentBalance - 1000),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (BankDAO.updateBalance(currentUser.getAccountNo(), newBalance) && 
                BankDAO.addTransaction(currentUser.getAccountNo(), "WITHDRAW", amount, null, "Cash withdrawal")) {
                
                currentUser.setBalance(newBalance);
                JOptionPane.showMessageDialog(this, 
                    "Withdrawal successful!\nAmount: ₹" + amount + 
                    "\nNew Balance: ₹" + newBalance, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Withdrawal failed. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid amount", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}