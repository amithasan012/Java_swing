import javax.swing.*;
import java.sql.*;

public class ViewAllTransactions extends JFrame {

    public ViewAllTransactions() {

        setTitle("All Transactions");
        setSize(600, 400);

        String[] cols = {"User", "Car", "Amount", "Date"};
        String[][] data = new String[200][4];

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT u.username, c.name, t.amount, t.rent_date " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN cars c ON t.car_id = c.id"
            );

            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString(1);
                data[i][1] = rs.getString(2);
                data[i][2] = rs.getString(3);
                data[i][3] = rs.getString(4);
                i++;
            }

        } catch (Exception ex) { ex.printStackTrace(); }

        JTable table = new JTable(data, cols);
        add(new JScrollPane(table));

        setVisible(true);
        setLocationRelativeTo(null);
    }
}
