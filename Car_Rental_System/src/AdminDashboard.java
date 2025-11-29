import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setLayout(null);

        JButton addCar = new JButton("Add Cars");
        JButton remCar = new JButton("Remove Cars");
        JButton remUser = new JButton("Remove Users");
        JButton viewTrans = new JButton("View All Transactions");
        JButton logout = new JButton("Logout");

        addCar.setBounds(150, 40, 200, 40);
        remCar.setBounds(150, 100, 200, 40);
        remUser.setBounds(150, 160, 200, 40);
        viewTrans.setBounds(150, 220, 200, 40);
        logout.setBounds(150, 280, 200, 40);

        add(addCar); add(remCar); add(remUser); add(viewTrans); add(logout);

        addCar.addActionListener(e -> new AddCarPage());
        remCar.addActionListener(e -> new RemoveCarPage());
        remUser.addActionListener(e -> new RemoveUserPage());
        viewTrans.addActionListener(e -> new ViewAllTransactions());
        logout.addActionListener(e -> { new LoginPage(); dispose(); });

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
