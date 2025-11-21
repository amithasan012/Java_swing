import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    String username;

    public UserDashboard(String username) {
        this.username = username;

        setTitle("User Dashboard");
        setSize(400, 400);
        setLayout(new GridLayout(4,1));
        setLocationRelativeTo(null);

        JButton vd = new JButton("View All Doctors");
        JButton va = new JButton("View My Appointments");
        JButton bn = new JButton("Book New Appointment");
        JButton lo = new JButton("Logout");

        add(vd); add(va); add(bn); add(lo);

        vd.addActionListener(e -> Utils.showQuery("SELECT * FROM doctors"));
        va.addActionListener(e -> Utils.showQuery("SELECT * FROM appointments WHERE patient='" + username + "'"));
        bn.addActionListener(e -> new BookAppointmentFrame(username));

        lo.addActionListener(e -> { dispose(); new LoginFrame(); });

        setVisible(true);
    }
}
