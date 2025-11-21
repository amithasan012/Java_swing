// DoctorDashboard.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DoctorDashboard extends JFrame {
    private int doctorUserId;
    private String username;

    private DefaultTableModel myPatientsModel = new DefaultTableModel(new String[]{"AppID","PatientID","PatientName","Date","Time","Status"},0);

    public DoctorDashboard(int userId, String user) {
        this.doctorUserId = userId;
        this.username = user;
        setTitle("Doctor Dashboard - " + username);
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadAssignedPatients();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JTable t = new JTable(myPatientsModel);
        add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnPrescribe = new JButton("Give Prescription");
        JButton btnMarkTest = new JButton("Mark Tests Required");
        bottom.add(btnRefresh); bottom.add(btnPrescribe); bottom.add(btnMarkTest);
        add(bottom, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadAssignedPatients());
        btnPrescribe.addActionListener(e -> {
            int r = t.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this,"Select appointment row"); return; }
            int appId = (int) myPatientsModel.getValueAt(r,0);
            int patientId = (int) myPatientsModel.getValueAt(r,1);
            String note = JOptionPane.showInputDialog(this,"Enter prescription note:");
            if (note == null) return;
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO prescriptions (appointment_id, doctor_id, patient_id, note) VALUES (?,?,?,?)")) {
                ps.setInt(1,appId); ps.setInt(2, getDoctorIdFromUser()); ps.setInt(3,patientId);
                ps.setString(4,note);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Prescription saved");
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });

        btnMarkTest.addActionListener(e -> {
            int r = t.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this,"Select appointment row"); return; }
            int appId = (int) myPatientsModel.getValueAt(r,0);
            int patientId = (int) myPatientsModel.getValueAt(r,1);
            String tests = JOptionPane.showInputDialog(this,"Enter tests required (comma-separated):");
            if (tests == null) return;
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO prescriptions (appointment_id, doctor_id, patient_id, tests_required) VALUES (?,?,?,?)")) {
                ps.setInt(1,appId); ps.setInt(2, getDoctorIdFromUser()); ps.setInt(3,patientId);
                ps.setString(4,tests);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Tests marked");
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
    }

    private int getDoctorIdFromUser() throws SQLException {
        // map logged-in user record to doctors table by username -> doctor name mapping is simple approach:
        // If needed, link user to doctor via users.full_name and doctors.name. Here we try to find doctor by username or user full_name.
        PreparedStatement ps = DB.getConnection().prepareStatement("SELECT id,full_name FROM users WHERE id=?");
        ps.setInt(1, doctorUserId);
        ResultSet rs = ps.executeQuery();
        String name = null;
        if (rs.next()) name = rs.getString("full_name");
        rs.close(); ps.close();
        // find doctor by name
        if (name != null) {
            PreparedStatement ps2 = DB.getConnection().prepareStatement("SELECT id FROM doctors WHERE name LIKE ?");
            ps2.setString(1, "%" + name + "%");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                int did = rs2.getInt("id");
                rs2.close(); ps2.close();
                return did;
            }
            rs2.close(); ps2.close();
        }
        // fallback: ask doctor to enter their doctor ID
        String s = JOptionPane.showInputDialog(this, "Enter your Doctor ID (check Admin->Doctors):");
        return Integer.parseInt(s);
    }

    private void loadAssignedPatients() {
        myPatientsModel.setRowCount(0);
        try {
            int doctorId;
            try {
                doctorId = getDoctorIdFromUser();
            } catch (Exception ex) {
                // if canceled or invalid, stop
                return;
            }
            String q = "SELECT a.id, p.id as pid, p.name, a.appointment_date, a.appointment_time, a.status " +
                       "FROM appointments a JOIN patients p ON a.patient_id=p.id WHERE a.doctor_id=?";
            PreparedStatement ps = DB.getConnection().prepareStatement(q);
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                myPatientsModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("pid"),
                        rs.getString("name"),
                        rs.getDate("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("status")
                });
            }
            rs.close(); ps.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
