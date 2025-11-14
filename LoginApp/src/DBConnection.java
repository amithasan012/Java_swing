import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/login_db";
    private static final String USER ="root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection(){
        Connection conn = null;
        try {
            //Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println("Mysql Driver NOt Found");
        }
        return conn;
    }
}