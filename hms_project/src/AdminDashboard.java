import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setLayout(new GridLayout(7, 1));
        setLocationRelativeTo(null);

        JButton p = new JButton("View All Patients");
        JButton d = new JButton("View All Doctors");
        JButton s = new JButton("View All Staff");
        JButton addDoc = new JButton("Add Doctor");
        JButton remDoc = new JButton("Remove Doctor");
        JButton apps = new JButton("View All Appointments");
        JButton logout = new JButton("Logout");

        add(p); add(d); add(s); add(addDoc); add(remDoc); add(apps); add(logout);

        p.addActionListener(e -> showTable("SELECT * FROM users WHERE role='user'"));
        d.addActionListener(e -> showTable("SELECT * FROM doctors"));
        s.addActionListener(e -> showTable("SELECT * FROM users WHERE role='staff'"));

        addDoc.addActionListener(e -> new AddDoctorFrame());
        remDoc.addActionListener(e -> new RemoveDoctorFrame());
        apps.addActionListener(e -> showTable("SELECT * FROM appointments"));

        logout.addActionListener(e -> { dispose(); new LoginFrame(); });

        setVisible(true);
    }

    void showTable(String q) {
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();

            JTable table = new JTable(Utils.buildTableModel(rs));
            JOptionPane.showMessageDialog(this, new JScrollPane(table));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
