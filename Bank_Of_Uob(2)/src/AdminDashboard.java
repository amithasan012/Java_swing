import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Bank of UOB - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Welcome panel
        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Admin Dashboard - Bank of UOB", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 0, 139));
        welcomePanel.add(welcomeLabel);
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        
        // Dashboard buttons
        JPanel dashboardPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        String[] buttons = {"View All Users", "Remove Users", "View Transactions", "Log Out"};
        
        for (String buttonText : buttons) {
            JButton button = new JButton(buttonText);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            
            button.addActionListener(new AdminButtonListener());
            dashboardPanel.add(button);
        }
        
        mainPanel.add(dashboardPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private class AdminButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            
            switch (command) {
                case "View All Users":
                case "Remove Users":
                    new UserManagementFrame().setVisible(true);
                    break;
                case "View Transactions":
                    new AllTransactionsFrame().setVisible(true);
                    break;
                case "Log Out":
                    new LoginFrame().setVisible(true);
                    dispose();
                    break;
            }
        }
    }
}