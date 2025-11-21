import javax.swing.*;
import java.sql.*;

public class AddDoctorFrame extends JFrame {

    JTextField n,d,f,ph,t;

    public AddDoctorFrame() {
        setTitle("Add Doctor");
        setSize(300,400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l1=new JLabel("Name:");   l1.setBounds(30,30,100,30); add(l1);
        n=new JTextField(); n.setBounds(120,30,120,30); add(n);

        JLabel l2=new JLabel("Dept:");   l2.setBounds(30,80,100,30); add(l2);
        d=new JTextField(); d.setBounds(120,80,120,30); add(d);

        JLabel l3=new JLabel("Fee:");    l3.setBounds(30,130,100,30); add(l3);
        f=new JTextField(); f.setBounds(120,130,120,30); add(f);

        JLabel l4=new JLabel("Phone:");  l4.setBounds(30,180,100,30); add(l4);
        ph=new JTextField(); ph.setBounds(120,180,120,30); add(ph);

        JLabel l5=new JLabel("Time:");   l5.setBounds(30,230,100,30); add(l5);
        t=new JTextField(); t.setBounds(120,230,120,30); add(t);

        JButton add = new JButton("Add");
        add.setBounds(100,280,80,30); add(add);

        add.addActionListener(e -> addDoctor());

        setVisible(true);
    }

    void addDoctor(){
        try{
            Connection con=DB.getConnection();
            PreparedStatement ps=con.prepareStatement(
                "INSERT INTO doctors(name,department,fee,number,available_time)VALUES(?,?,?,?,?)"
            );
            ps.setString(1,n.getText());
            ps.setString(2,d.getText());
            ps.setDouble(3,Double.parseDouble(f.getText()));
            ps.setString(4,ph.getText());
            ps.setString(5,t.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Doctor Added!");
            dispose();
        }catch(Exception e){ e.printStackTrace(); }
    }
}
