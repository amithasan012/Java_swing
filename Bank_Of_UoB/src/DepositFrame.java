// DepositFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DepositFrame extends JFrame {
    private User currentUser;
    private JTextField amountField;
    private JButton depositButton;
    
    public DepositFrame(User user) {
        this.currentUser = user;
        setTitle("Deposit Money");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Deposit Money", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        formPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        formPanel.add(amountField);
        
        formPanel.add(new JLabel());
        depositButton = new JButton("Deposit");
        depositButton.setBackground(new Color(0, 100, 0));
        depositButton.setForeground(Color.BLACK);
        formPanel.add(depositButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        depositButton.addActionListener(e -> processDeposit());
    }
    
    private void processDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double newBalance = currentUser.getBalance() + amount;
            
            if (BankDAO.updateBalance(currentUser.getAccountNo(), newBalance) && 
                BankDAO.addTransaction(currentUser.getAccountNo(), "DEPOSIT", amount, null, "Cash deposit")) {
                
                currentUser.setBalance(newBalance);
                JOptionPane.showMessageDialog(this, 
                    "Deposit successful!\nNew Balance: $" + newBalance, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Deposit failed. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid amount", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}