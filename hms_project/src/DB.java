import java.sql.*;

public class DB {
    static Connection con;

    public static Connection getConnection() {
        try {
            if (con == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/hms",
                        "root",
                        "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
