import javax.swing.*;
import java.sql.*;

public class RemoveUserPage extends JFrame {

    JComboBox<String> userList;
    int ids[];

    public RemoveUserPage() {

        setTitle("Remove Users");
        setSize(400, 200);
        setLayout(null);

        try {
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE role='user'");

            userList = new JComboBox<>();
            ids = new int[100];
            int i = 0;

            while (rs.next()) {
                userList.addItem(rs.getString("username"));
                ids[i++] = rs.getInt("id");
            }

            userList.setBounds(100, 40, 180, 30);
            add(userList);

        } catch (Exception ex) { ex.printStackTrace(); }

        JButton rem = new JButton("Delete");
        rem.setBounds(130, 100, 120, 30);
        add(rem);

        rem.addActionListener(e -> {
            int index = userList.getSelectedIndex();
            int id = ids[index];

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement("DELETE FROM users WHERE id=?");
                pst.setInt(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "User Removed!");
                dispose();

            } catch (Exception ex) { ex.printStackTrace(); }
        });

        setVisible(true);
        setLocationRelativeTo(null);
    }
}
