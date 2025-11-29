import javax.swing.*;

public class UserDashboard extends JFrame {
    int userId;

    public UserDashboard(int userId) {
        this.userId = userId;

        setTitle("User Dashboard");
        setSize(400, 350);
        setLayout(null);

        JButton show = new JButton("Show Available Cars");
        JButton rent = new JButton("Rent a Car");
        JButton view = new JButton("View My Transactions");
        JButton logout = new JButton("Logout");

        show.setBounds(100, 40, 200, 40);
        rent.setBounds(100, 100, 200, 40);
        view.setBounds(100, 160, 200, 40);
        logout.setBounds(100, 220, 200, 40);

        add(show); add(rent); add(view); add(logout);

        show.addActionListener(e -> new ShowAvailableCars());
        rent.addActionListener(e -> new RentCarPage(userId));
        view.addActionListener(e -> new ViewMyTransactions(userId));
        logout.addActionListener(e -> { new LoginPage(); dispose(); });

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
