import javax.swing.*;
import java.sql.*;

public class AddCarPage extends JFrame {
    JTextField name, brand, seats, day, week, month;

    public AddCarPage() {
        setTitle("Add Car");
        setSize(400, 400);
        setLayout(null);

        String labels[] = {"Car Name", "Brand", "Seats", "Per Day", "Per Week", "Per Month"};
        JTextField fields[] = new JTextField[6];

        int y = 30;
        for (int i = 0; i < 6; i++) {
            JLabel l = new JLabel(labels[i]);
            l.setBounds(50, y, 150, 30);
            add(l);

            fields[i] = new JTextField();
            fields[i].setBounds(180, y, 150, 30);
            add(fields[i]);
            y += 50;
        }

        name = fields[0]; brand = fields[1]; seats = fields[2];
        day = fields[3]; week = fields[4]; month = fields[5];

        JButton addBtn = new JButton("Add Car");
        addBtn.setBounds(120, 330, 150, 30);
        add(addBtn);

        addBtn.addActionListener(e -> addCar());

        setVisible(true);
        setLocationRelativeTo(null);
    }

    void addCar() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO cars(name,brand,seats,price_day,price_week,price_month) VALUES(?,?,?,?,?,?)"
            );
            pst.setString(1, name.getText());
            pst.setString(2, brand.getText());
            pst.setInt(3, Integer.parseInt(seats.getText()));
            pst.setDouble(4, Double.parseDouble(day.getText()));
            pst.setDouble(5, Double.parseDouble(week.getText()));
            pst.setDouble(6, Double.parseDouble(month.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car Added!");
            dispose();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
