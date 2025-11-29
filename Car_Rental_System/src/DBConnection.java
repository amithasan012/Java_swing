import java.sql.*;

public class DBConnection {

    static final String URL = "jdbc:mysql://localhost:3306/carrental";
    static final String USER = "root";
    static final String PASS = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
