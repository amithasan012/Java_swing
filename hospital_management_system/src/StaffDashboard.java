// StaffDashboard.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StaffDashboard extends JFrame {
    private int staffId;
    private String username;

    private DefaultTableModel doctorsModel = new DefaultTableModel(new String[]{"ID","Name","Dept","Fee"},0);
    private DefaultTableModel appointModel = new DefaultTableModel(new String[]{"ID","Patient","Doctor","Date","Time","Status"},0);

    public StaffDashboard(int uid, String user) {
        this.staffId = uid; this.username = user;
        setTitle("Staff Dashboard - " + username);
        setSize(850,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadDoctors();
        loadAppointments();
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

        JPanel pDocs = new JPanel(new BorderLayout());
        JTable tDocs = new JTable(doctorsModel);
        pDocs.add(new JScrollPane(tDocs), BorderLayout.CENTER);

        JPanel pApps = new JPanel(new BorderLayout());
        JTable tApps = new JTable(appointModel);
        pApps.add(new JScrollPane(tApps), BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnRefresh = new JButton("Refresh");
        controls.add(btnApprove); controls.add(btnReject); controls.add(btnRefresh);
        pApps.add(controls, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadAppointments());
        btnApprove.addActionListener(e -> {
            int r = tApps.getSelectedRow();
            if (r == -1) return;
            int appId = (int) appointModel.getValueAt(r,0);
            updateAppointmentStatus(appId, "APPROVED");
        });
        btnReject.addActionListener(e -> {
            int r = tApps.getSelectedRow();
            if (r == -1) return;
            int appId = (int) appointModel.getValueAt(r,0);
            updateAppointmentStatus(appId, "REJECTED");
        });

        tabs.add("Doctors", pDocs);
        tabs.add("Appointments", pApps);
        add(tabs);
    }

    private void updateAppointmentStatus(int appId, String status) {
        try (PreparedStatement ps = DB.getConnection().prepareStatement("UPDATE appointments SET status=? WHERE id=?")) {
            ps.setString(1, status); ps.setInt(2, appId); ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Appointment " + status);
            loadAppointments();
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: " + ex.getMessage()); }
    }

    private void loadDoctors() {
        doctorsModel.setRowCount(0);
        try (Statement st = DB.getConnection().createStatement(); ResultSet rs = st.executeQuery("SELECT id,name,department,fee FROM doctors")) {
            while (rs.next()) {
                doctorsModel.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4)});
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadAppointments() {
        appointModel.setRowCount(0);
        String q = "SELECT a.id, p.name as patient, d.name as doctor, a.appointment_date, a.appointment_time, a.status " +
                   "FROM appointments a JOIN patients p ON a.patient_id=p.id JOIN doctors d ON a.doctor_id=d.id";
        try (Statement st = DB.getConnection().createStatement(); ResultSet rs = st.executeQuery(q)) {
            while (rs.next()) {
                appointModel.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5), rs.getString(6)
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
