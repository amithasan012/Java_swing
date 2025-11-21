import javax.swing.*;
import java.awt.*;

public class StaffDashboard extends JFrame {

    public StaffDashboard() {
        setTitle("Staff Dashboard");
        setSize(400, 400);
        setLayout(new GridLayout(3,1));
        setLocationRelativeTo(null);

        JButton apps = new JButton("Appointments");
        JButton docs = new JButton("View Doctors");
        JButton logout = new JButton("Logout");

        add(apps); add(docs); add(logout);

        apps.addActionListener(e -> Utils.showAppointmentApproval());
        docs.addActionListener(e -> Utils.showQuery("SELECT * FROM doctors"));

        logout.addActionListener(e -> { dispose(); new LoginFrame(); });

        setVisible(true);
    }
}
