import javax.swing.*;
import java.sql.*;

public class RemoveDoctorFrame extends JFrame {

    public RemoveDoctorFrame() {
        setTitle("Remove Doctor");
        setSize(300,200);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l = new JLabel("Enter Doctor ID:");
        l.setBounds(30,40,150,30);
        add(l);

        JTextField id = new JTextField();
        id.setBounds(150,40,100,30);
        add(id);

        JButton rm = new JButton("Remove");
        rm.setBounds(100,90,100,30);
        add(rm);

        rm.addActionListener(e -> {
            try{
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM doctors WHERE id=?");
                ps.setInt(1, Integer.parseInt(id.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Doctor Removed!");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
