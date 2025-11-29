import javax.swing.*;
import java.sql.*;

public class RemoveCarPage extends JFrame {

    JComboBox<String> carList;
    int carIds[];

    public RemoveCarPage() {
        setTitle("Remove Car");
        setSize(400, 200);
        setLayout(null);

        loadCars();

        JButton remBtn = new JButton("Remove");
        remBtn.setBounds(130, 100, 120, 30);
        add(remBtn);

        remBtn.addActionListener(e -> removeCar());

        setVisible(true);
        setLocationRelativeTo(null);
    }

    void loadCars() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM cars");

            carList = new JComboBox<>();
            carIds = new int[100];
            int i = 0;

            while (rs.next()) {
                carList.addItem(rs.getString("name"));
                carIds[i++] = rs.getInt("id");
            }

            carList.setBounds(100, 40, 180, 30);
            add(carList);

        } catch (Exception e) { e.printStackTrace(); }
    }

    void removeCar() {
        int index = carList.getSelectedIndex();
        int id = carIds[index];

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM cars WHERE id=?");
            pst.setInt(1, id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car Removed!");
            dispose();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
