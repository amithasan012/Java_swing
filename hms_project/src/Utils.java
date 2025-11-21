import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class Utils {

    public static void showQuery(String q) {
        try {
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws Exception {

        ResultSetMetaData m = rs.getMetaData();
        int col = m.getColumnCount();
        String[] cols = new String[col];

        for (int i = 1; i <= col; i++) cols[i-1] = m.getColumnName(i);

        DefaultTableModel model = new DefaultTableModel(cols, 0);

        while (rs.next()) {
            Object[] row = new Object[col];
            for (int i = 1; i <= col; i++) row[i-1] = rs.getObject(i);
            model.addRow(row);
        }
        return model;
    }

    public static void showAppointmentApproval() {
        try {
            Connection con = DB.getConnection();
            String q = "SELECT * FROM appointments";
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs = ps.executeQuery();

            JTable table = new JTable(buildTableModel(rs));

            int opt = JOptionPane.showConfirmDialog(null,
                    new JScrollPane(table),
                    "Approve Appointment? (Select row first)",
                    JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Appointment ID to approve:"));
                PreparedStatement up = con.prepareStatement(
                        "UPDATE appointments SET status='approved' WHERE id=?");
                up.setInt(1, id);
                up.executeUpdate();
                JOptionPane.showMessageDialog(null, "Approved!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
