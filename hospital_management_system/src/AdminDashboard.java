// AdminDashboard.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private int adminId;
    private String adminUser;

    private JTabbedPane tabs = new JTabbedPane();

    // tables models
    private DefaultTableModel doctorsModel = new DefaultTableModel(new String[]{"ID","Name","Dept","Fee","Contact"},0);
    private DefaultTableModel patientsModel = new DefaultTableModel(new String[]{"ID","Name","Age","Gender","Contact"},0);
    private DefaultTableModel staffModel = new DefaultTableModel(new String[]{"ID","Name","Role","Contact"},0);
    private DefaultTableModel appointModel = new DefaultTableModel(new String[]{"ID","Patient","Doctor","Date","Time","Status"},0);

    public AdminDashboard(int id, String user) {
        this.adminId = id; this.adminUser = user;
        setTitle("Admin Dashboard - " + user);
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        loadAll();
    }

    private void initUI() {
        // doctors tab
        JPanel pDoctors = new JPanel(new BorderLayout());
        JTable tDoctors = new JTable(doctorsModel);
        pDoctors.add(new JScrollPane(tDoctors), BorderLayout.CENTER);

        JPanel addDoc = new JPanel();
        JTextField docName = new JTextField(12);
        JTextField docDept = new JTextField(10);
        JTextField docFee = new JTextField(6);
        JTextField docContact = new JTextField(10);
        JButton btnAddDoc = new JButton("Add Doctor");
        addDoc.add(new JLabel("Name:")); addDoc.add(docName);
        addDoc.add(new JLabel("Dept:")); addDoc.add(docDept);
        addDoc.add(new JLabel("Fee:")); addDoc.add(docFee);
        addDoc.add(new JLabel("Contact:")); addDoc.add(docContact);
        addDoc.add(btnAddDoc);
        pDoctors.add(addDoc, BorderLayout.SOUTH);

        btnAddDoc.addActionListener(e -> {
            String name = docName.getText().trim();
            String dept = docDept.getText().trim();
            String feeStr = docFee.getText().trim();
            String contact = docContact.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(this,"Enter doctor name"); return; }
            double fee = 0;
            try { fee = Double.parseDouble(feeStr); } catch (Exception ex) {}
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO doctors (name,department,fee,contact) VALUES (?,?,?,?)")) {
                ps.setString(1, name); ps.setString(2, dept); ps.setDouble(3, fee); ps.setString(4, contact);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Doctor added");
                loadDoctors();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });

        // patients tab
        JPanel pPatients = new JPanel(new BorderLayout());
        JTable tPatients = new JTable(patientsModel);
        pPatients.add(new JScrollPane(tPatients), BorderLayout.CENTER);
        JPanel addPat = new JPanel();
        JTextField patName = new JTextField(12);
        JTextField patAge = new JTextField(4);
        JTextField patGender = new JTextField(6);
        JTextField patContact = new JTextField(10);
        JButton btnAddPat = new JButton("Add Patient");
        addPat.add(new JLabel("Name:")); addPat.add(patName);
        addPat.add(new JLabel("Age:")); addPat.add(patAge);
        addPat.add(new JLabel("Gender:")); addPat.add(patGender);
        addPat.add(new JLabel("Contact:")); addPat.add(patContact);
        addPat.add(btnAddPat);
        pPatients.add(addPat, BorderLayout.SOUTH);

        btnAddPat.addActionListener(e -> {
            String name = patName.getText().trim();
            String ageS = patAge.getText().trim();
            String gender = patGender.getText().trim();
            String contact = patContact.getText().trim();
            int age = 0; try { age = Integer.parseInt(ageS); } catch (Exception ex) {}
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO patients (name,age,gender,contact) VALUES (?,?,?,?)")) {
                ps.setString(1,name); ps.setInt(2,age); ps.setString(3,gender); ps.setString(4,contact);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Patient added");
                loadPatients();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });

        // staff tab
        JPanel pStaff = new JPanel(new BorderLayout());
        JTable tStaff = new JTable(staffModel);
        pStaff.add(new JScrollPane(tStaff), BorderLayout.CENTER);
        JPanel addStaff = new JPanel();
        JTextField staffName = new JTextField(12);
        JTextField staffRole = new JTextField(10);
        JTextField staffContact = new JTextField(10);
        JButton btnAddStaff = new JButton("Add Staff");
        addStaff.add(new JLabel("Name:")); addStaff.add(staffName);
        addStaff.add(new JLabel("Role:")); addStaff.add(staffRole);
        addStaff.add(new JLabel("Contact:")); addStaff.add(staffContact);
        addStaff.add(btnAddStaff);
        pStaff.add(addStaff, BorderLayout.SOUTH);

        btnAddStaff.addActionListener(e -> {
            String name = staffName.getText().trim();
            String role = staffRole.getText().trim();
            String contact = staffContact.getText().trim();
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO staff (name,role,contact) VALUES (?,?,?)")) {
                ps.setString(1,name); ps.setString(2,role); ps.setString(3,contact);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Staff added");
                loadStaff();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });

        // appointments tab
        JPanel pApp = new JPanel(new BorderLayout());
        JTable tApp = new JTable(appointModel);
        pApp.add(new JScrollPane(tApp), BorderLayout.CENTER);
        JPanel schedulePanel = new JPanel();
        JComboBox<String> cbPatient = new JComboBox<>();
        JComboBox<String> cbDoctor = new JComboBox<>();
        JTextField appDate = new JTextField(8); // yyyy-mm-dd
        JTextField appTime = new JTextField(6);
        JButton btnSchedule = new JButton("Schedule");
        JButton btnRefresh = new JButton("Refresh");
        schedulePanel.add(new JLabel("Patient:")); schedulePanel.add(cbPatient);
        schedulePanel.add(new JLabel("Doctor:")); schedulePanel.add(cbDoctor);
        schedulePanel.add(new JLabel("Date(YYYY-MM-DD):")); schedulePanel.add(appDate);
        schedulePanel.add(new JLabel("Time:")); schedulePanel.add(appTime);
        schedulePanel.add(btnSchedule);
        schedulePanel.add(btnRefresh);
        pApp.add(schedulePanel, BorderLayout.SOUTH);

        btnSchedule.addActionListener(e -> {
            String pSel = (String) cbPatient.getSelectedItem();
            String dSel = (String) cbDoctor.getSelectedItem();
            if (pSel == null || dSel == null) { JOptionPane.showMessageDialog(this,"Select patient and doctor"); return; }
            int pid = Integer.parseInt(pSel.split(":")[0]);
            int did = Integer.parseInt(dSel.split(":")[0]);
            String date = appDate.getText().trim();
            String time = appTime.getText().trim();
            try (PreparedStatement ps = DB.getConnection().prepareStatement(
                    "INSERT INTO appointments (patient_id,doctor_id,appointment_date,appointment_time) VALUES (?,?,?,?)")) {
                ps.setInt(1,pid); ps.setInt(2,did); ps.setDate(3, Date.valueOf(date)); ps.setString(4,time);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this,"Appointment scheduled (Pending approval)");
                loadAppointments();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });

        btnRefresh.addActionListener(e -> loadAll());

        tabs.add("Doctors", pDoctors);
        tabs.add("Patients", pPatients);
        tabs.add("Staff", pStaff);
        tabs.add("Appointments", pApp);

        add(tabs);
    }

    private void loadAll() {
        loadDoctors();
        loadPatients();
        loadStaff();
        loadAppointments();
    }

    private void loadDoctors() {
        doctorsModel.setRowCount(0);
        try (Statement st = DB.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM doctors")) {
            while (rs.next()) {
                doctorsModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"), rs.getString("department"),
                    rs.getDouble("fee"), rs.getString("contact")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadPatients() {
        patientsModel.setRowCount(0);
        try (Statement st = DB.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM patients")) {
            while (rs.next()) {
                patientsModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"), rs.getInt("age"),
                    rs.getString("gender"), rs.getString("contact")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadStaff() {
        staffModel.setRowCount(0);
        try (Statement st = DB.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM staff")) {
            while (rs.next()) {
                staffModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"), rs.getString("role"),
                    rs.getString("contact")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadAppointments() {
        appointModel.setRowCount(0);
        String q = "SELECT a.id, p.name as patient, d.name as doctor, a.appointment_date, a.appointment_time, a.status " +
                   "FROM appointments a JOIN patients p ON a.patient_id=p.id JOIN doctors d ON a.doctor_id=d.id";
        try (Statement st = DB.getConnection().createStatement();
             ResultSet rs = st.executeQuery(q)) {
            while (rs.next()) {
                appointModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("patient"), rs.getString("doctor"),
                    rs.getDate("appointment_date"), rs.getString("appointment_time"), rs.getString("status")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
