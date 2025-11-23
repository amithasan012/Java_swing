// UserDashboard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboard extends JFrame {
    private User currentUser;
    private JLabel welcomeLabel;
    
    public UserDashboard(User user) {
        this.currentUser = user;
        setTitle("Bank of UOB - User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Welcome panel
        JPanel welcomePanel = new JPanel();
        welcomeLabel = new JLabel("Welcome " + currentUser.getUsername() + " to the Bank of UOB", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 0, 139));
        welcomePanel.add(welcomeLabel);
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        //mainPanel.setBackground(new Color(20,27,50));
        
        // Dashboard buttons panel
        JPanel dashboardPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        dashboardPanel.setBackground(new Color(20,27,50));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[] buttons = {
            "Deposit", "Withdraw", "Transfer", 
            "Check Balance", "Edit Profile", "Transaction History",
            "Log Out"
        };
        
        for (String buttonText : buttons) {
            JButton button = new JButton(buttonText);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            
            button.addActionListener(new DashboardButtonListener());
            dashboardPanel.add(button);
        }
        
        // Add empty panels for grid alignment
        //dashboardPanel.add(new JPanel());
        //dashboardPanel.add(new JPanel());
        
        mainPanel.add(dashboardPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private class DashboardButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            
            switch (command) {
                case "Deposit":
                    new DepositFrame(currentUser).setVisible(true);
                    break;
                case "Withdraw":
                    new WithdrawFrame(currentUser).setVisible(true);
                    break;
                case "Transfer":
                    new TransferFrame(currentUser).setVisible(true);
                    break;
                case "Check Balance":
                    new BalanceFrame(currentUser).setVisible(true);
                    break;
                case "Edit Profile":
                    new EditProfileFrame(currentUser).setVisible(true);
                    break;
                case "Transaction History":
                    new TransactionHistoryFrame(currentUser).setVisible(true);
                    break;
                case "Log Out":
                    int confirm = JOptionPane.showConfirmDialog(
                        UserDashboard.this, 
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        new LoginFrame().setVisible(true);
                        dispose();
                    }
                    break;
            }
        }
    }
}