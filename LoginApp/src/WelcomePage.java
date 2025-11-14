import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    private JButton btnLogout;
    public WelcomePage(String username) {
        setTitle("Welcome Page");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Welcome to the System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JLabel lblUser = new JLabel("Hello, " + username + " 👋", SwingConstants.CENTER);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(lblUser, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JPanel btnPanel = new JPanel();
        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(204, 0, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnPanel.add(btnLogout);
        footer.add(btnPanel);

        JLabel lblFooter = new JLabel("You are successfully logged in!", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 14));
        lblFooter.setForeground(Color.DARK_GRAY);
        footer.add(lblFooter);
        add(footer, BorderLayout.SOUTH);
        
        btnLogout.addActionListener(e -> {
            new LoginForm();
            dispose();
        });

        getContentPane().setBackground(new Color(245, 247, 250));

        setVisible(true);
    }
}
