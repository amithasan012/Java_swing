import javax.swing.*;
import java.sql.*;

public class BookAppointmentFrame extends JFrame {

    JComboBox<String> docBox;
    JTextField dt, tm;
    String user;

    public BookAppointmentFrame(String user) {
        this.user = user;

        setTitle("Book Appointment");
        setSize(300,300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l1=new JLabel("Doctor:");
        l1.setBounds(30,40,100,30);
        add(l1);

        docBox = new JComboBox<>();
        docBox.setBounds(120,40,120,30);
        add(docBox);

        loadDoctors();

        JLabel l2=new JLabel("Date:");
        l2.setBounds(30,100,100,30);
        add(l2);

        dt = new JTextField();
        dt.setBounds(120,100,120,30);
        add(dt);

        JLabel l3=new JLabel("Time:");
        l3.setBounds(30,150,100,30);
        add(l3);

        tm = new JTextField();
        tm.setBounds(120,150,120,30);
        add(tm);

        JButton b = new JButton("Book");
        b.setBounds(100,200,80,30);
        add(b);

        b.addActionListener(e -> book());

        setVisible(true);
    }

    void loadDoctors(){
        try{
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM doctors");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) docBox.addItem(rs.getString("name"));
        }catch(Exception e){ e.printStackTrace(); }
    }

    void book(){
        try{
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments(patient,doctor,date,time,status)VALUES(?,?,?,?,?)");
            ps.setString(1,user);
            ps.setString(2,(String) docBox.getSelectedItem());
            ps.setString(3,dt.getText());
            ps.setString(4,tm.getText());
            ps.setString(5,"pending");
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Appointment Booked (Pending)!");
            dispose();

        }catch(Exception e){ e.printStackTrace(); }
    }
}
