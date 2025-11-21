// DB.java
import java.sql.*;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = "root";
    private static final String PASS = ""; // change this

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn != null) return conn;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("DB connected");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Cannot connect to DB: " + e.getMessage());
            System.exit(0);
            return null;
        }
    }

    public static void close(ResultSet rs, Statement st) {
        try {
            if (rs != null) rs.close();
            if (st != null) st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
