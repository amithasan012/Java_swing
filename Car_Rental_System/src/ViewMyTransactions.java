import javax.swing.*;
import java.sql.*;

public class ViewMyTransactions extends JFrame {

    public ViewMyTransactions(int userId) {

        setTitle("My Transactions");
        setSize(600, 400);

        String[] cols = {"Car Name", "Amount", "Date"};
        String[][] data = new String[200][3];

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                "SELECT c.name, t.amount, t.rent_date " +
                "FROM transactions t JOIN cars c ON t.car_id=c.id WHERE t.user_id=?"
            );
            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();
            int i = 0;

            while (rs.next()) {
                data[i][0] = rs.getString(1);
                data[i][1] = rs.getString(2);
                data[i][2] = rs.getString(3);
                i++;
            }

        } catch (Exception e) { e.printStackTrace(); }

        JTable table = new JTable(data, cols);
        add(new JScrollPane(table));

        setVisible(true);
        setLocationRelativeTo(null);
    }
}
