import javax.swing.*;
import java.awt.*;

public class BalanceFrame extends JFrame {
    private User currentUser;
    
    public BalanceFrame(User user) {
        this.currentUser = user;
        setTitle("Account Balance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Account Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 0, 139));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Refresh user data to get latest balance
        User updatedUser = BankDAO.getUserByAccountNo(currentUser.getAccountNo());
        if (updatedUser != null) {
            currentUser = updatedUser;
        }
        
        infoPanel.add(createInfoLabel("Account Number:", currentUser.getAccountNo()));
        infoPanel.add(createInfoLabel("Account Holder:", currentUser.getUsername()));
        infoPanel.add(createInfoLabel("Branch:", currentUser.getBranch()));
        infoPanel.add(createInfoLabel("Email:", currentUser.getEmail()));
        infoPanel.add(createInfoLabel("Phone:", currentUser.getPhone()));
        
        JLabel balanceLabel = new JLabel("Current Balance: $" + currentUser.getBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(0, 100, 0));
        balanceLabel.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(balanceLabel);
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel keyLabel = new JLabel(label);
        keyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel valueLabel = new JLabel(value);
        
        panel.add(keyLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);
        return panel;
    }
}