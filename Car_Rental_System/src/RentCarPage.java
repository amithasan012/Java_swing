import javax.swing.*;
import java.sql.*;

public class RentCarPage extends JFrame {

    JComboBox<String> carList;
    int carIds[];
    int userId;
    JTextField amountField, dateField;

    public RentCarPage(int uid) {
        userId = uid;

        setTitle("Rent a Car");
        setSize(400, 350);
        setLayout(null);

        loadCars();

        JLabel amt = new JLabel("Amount:");
        amt.setBounds(50, 120, 100, 30);
        add(amt);

        amountField = new JTextField();
        amountField.setBounds(150, 120, 150, 30);
        add(amountField);

        JLabel dt = new JLabel("Date:");
        dt.setBounds(50, 170, 100, 30);
        add(dt);

        dateField = new JTextField();
        dateField.setBounds(150, 170, 150, 30);
        add(dateField);

        JButton rentBtn = new JButton("Rent Now");
        rentBtn.setBounds(120, 230, 150, 30);
        add(rentBtn);

        rentBtn.addActionListener(e -> rentCar());

        setVisible(true);
        setLocationRelativeTo(null);
    }

    void loadCars() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM cars WHERE status='available'");

            carList = new JComboBox<>();
            carList.setBounds(100, 50, 180, 30);
            add(carList);

            carIds = new int[100];
            int i = 0;

            while (rs.next()) {
                carList.addItem(rs.getString("name"));
                carIds[i++] = rs.getInt("id");
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    void rentCar() {
        int index = carList.getSelectedIndex();
        int cid = carIds[index];

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO transactions(user_id,car_id,amount,rent_date) VALUES(?,?,?,?)"
            );
            pst.setInt(1, userId);
            pst.setInt(2, cid);
            pst.setDouble(3, Double.parseDouble(amountField.getText()));
            pst.setString(4, dateField.getText());
            pst.executeUpdate();

            PreparedStatement update = conn.prepareStatement(
                "UPDATE cars SET status='rented' WHERE id=?"
            );
            update.setInt(1, cid);
            update.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car Rented Successfully!");
            dispose();

        } catch (Exception e) { e.printStackTrace(); }
    }
}
