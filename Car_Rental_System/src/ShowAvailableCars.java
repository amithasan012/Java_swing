import javax.swing.*;
import java.sql.*;

public class ShowAvailableCars extends JFrame {

    public ShowAvailableCars() {
        setTitle("Available Cars");
        setSize(600, 400);

        String[] cols = {"Name", "Brand", "Seats", "Day Price"};
        String[][] data = new String[200][4];

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT * FROM cars WHERE status='available'"
            );

            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString("name");
                data[i][1] = rs.getString("brand");
                data[i][2] = rs.getString("seats");
                data[i][3] = rs.getString("price_day");
                i++;
            }
        } catch (Exception e) { e.printStackTrace(); }

        JTable table = new JTable(data, cols);
        add(new JScrollPane(table));

        setVisible(true);
        setLocationRelativeTo(null);
    }
}
