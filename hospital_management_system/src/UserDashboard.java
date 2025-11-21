// UserDashboard.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserDashboard extends JFrame {
    private int userId;
    private String username;

    private DefaultTableModel docsModel = new DefaultTableModel(new String[]{"ID","Name","Dept","Fee","Contact"},0);
    private DefaultTableModel appsModel = new DefaultTableModel(new String[]{"ID","Doctor","Date","Time","Status"},0);

    public UserDashboard(int uid, String user) {
        this.userId = uid; this.username = user;
        setTitle("User Dashboard - " + username);
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadDoctors();
        loadMyAppointments();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

        JPanel pDocs = new JPanel(new BorderLayout());
        JTable tDocs = new JTable(docsModel);
        pDocs.add(new JScrollPane(tDocs), BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        JButton btnBook = new JButton("Book Selected Doctor");
        JTextField date = new JTextField(8);
        JTextField time = new JTextField(6);
        bottom.add(new JLabel("Date(YYYY-MM-DD):")); bottom.add(date);
        bottom.add(new JLabel("Time:")); bottom.add(time);
        bottom.add(btnBook);
        pDocs.add(bottom, BorderLayout.SOUTH);

        btnBook.addActionListener(e -> {
            int r = tDocs.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this,"Select a doctor"); return; }
            int did = (int) docsModel.getValueAt(r,0);
            String d = date.getText().trim();
            String t = time.getText().trim();
            // need patient record: map user->patient. For simplicity, create a patient entry for each user if not exists.
            int patientId = ensurePatientForUser();
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO appointments (patient_id,doctor_id,appointment_date,appointment_time) VALUES (?,?,?,?)")) {
                ps.setInt(1, patientId); ps.setInt(2, did); ps.setDate(3, Date.valueOf(d)); ps.setString(4,t);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Appointment requested (Pending approval)");
                loadMyAppointments();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });

        JPanel pApps = new JPanel(new BorderLayout());
        JTable tApps = new JTable(appsModel);
        pApps.add(new JScrollPane(tApps), BorderLayout.CENTER);
        JButton btnRefresh = new JButton("Refresh");
        pApps.add(btnRefresh, BorderLayout.SOUTH);
        btnRefresh.addActionListener(e -> loadMyAppointments());

        tabs.add("Doctors", pDocs);
        tabs.add("My Appointments", pApps);
        add(tabs);
    }

    private int ensurePatientForUser() {
        try {
            // try to find patient by users.full_name or username
            PreparedStatement ps = DB.getConnection().prepareStatement("SELECT full_name FROM users WHERE id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            String fullname = null;
            if (rs.next()) fullname = rs.getString("full_name");
            rs.close(); ps.close();

            if (fullname != null) {
                PreparedStatement ps2 = DB.getConnection().prepareStatement("SELECT id FROM patients WHERE name LIKE ? LIMIT 1");
                ps2.setString(1, "%" + fullname + "%");
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) { int pid = rs2.getInt(1); rs2.close(); ps2.close(); return pid; }
                rs2.close(); ps2.close();
                // else create
                PreparedStatement ps3 = DB.getConnection().prepareStatement("INSERT INTO patients (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                ps3.setString(1, fullname);
                ps3.executeUpdate();
                ResultSet gk = ps3.getGeneratedKeys();
                if (gk.next()) { int pid = gk.getInt(1); gk.close(); ps3.close(); return pid; }
                ps3.close();
            }
            // fallback create patient with username
            PreparedStatement ps4 = DB.getConnection().prepareStatement("INSERT INTO patients (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps4.setString(1, username);
            ps4.executeUpdate();
            ResultSet gk2 = ps4.getGeneratedKeys();
            if (gk2.next()) { int pid = gk2.getInt(1); gk2.close(); ps4.close(); return pid; }
            ps4.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
        return -1;
    }

    private void loadDoctors() {
        docsModel.setRowCount(0);
        try (Statement st = DB.getConnection().createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM doctors")) {
            while (rs.next()) {
                docsModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"), rs.getString("department"),
                    rs.getDouble("fee"), rs.getString("contact")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadMyAppointments() {
        appsModel.setRowCount(0);
        // find patient id for this user first
        try {
            PreparedStatement ps = DB.getConnection().prepareStatement("SELECT full_name FROM users WHERE id=?");
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            String fullname = null;
            if (rs.next()) fullname = rs.getString("full_name");
            rs.close(); ps.close();

            PreparedStatement ps2 = DB.getConnection().prepareStatement(
                "SELECT a.id, d.name, a.appointment_date, a.appointment_time, a.status FROM appointments a " +
                "JOIN doctors d ON a.doctor_id=d.id JOIN patients p ON a.patient_id=p.id WHERE p.name LIKE ?");
            ps2.setString(1, "%" + (fullname!=null?fullname:username) + "%");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                appsModel.addRow(new Object[]{
                    rs2.getInt(1), rs2.getString(2), rs2.getDate(3), rs2.getString(4), rs2.getString(5)
                });
            }
            rs2.close(); ps2.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
