// BankDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDAO {
    
    // User authentication
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("account_no"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("password"),
                    rs.getDouble("balance"),
                    rs.getString("branch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Check if username or email exists
    public static boolean userExists(String username, String email) {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Register new user
    public static boolean registerUser(User user) {
        String sql = "INSERT INTO users (account_no, username, email, phone, address, password, branch) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getAccountNo());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getPassword());
            pstmt.setString(7, user.getBranch());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Generate account number
    public static String generateAccountNo() {
        String sql = "SELECT COUNT(*) as count FROM users";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int count = rs.getInt("count") + 1;
                return "UOB" + String.format("%06d", count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "UOB000001";
    }
    
    // Update balance
    public static boolean updateBalance(String accountNo, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE account_no = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNo);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get user by account number
    public static User getUserByAccountNo(String accountNo) {
        String sql = "SELECT * FROM users WHERE account_no = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getString("account_no"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("password"),
                    rs.getDouble("balance"),
                    rs.getString("branch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Add transaction
    public static boolean addTransaction(String accountNo, String type, double amount, String targetAccount, String description) {
        String sql = "INSERT INTO transactions (account_no, type, amount, target_account, description) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, targetAccount);
            pstmt.setString(5, description);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get user transactions
    public static ResultSet getUserTransactions(String accountNo) {
        String sql = "SELECT * FROM transactions WHERE account_no = ? ORDER BY transaction_date DESC";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNo);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all users (for admin)
    public static ResultSet getAllUsers() {
        String sql = "SELECT account_no, username, email, phone, balance, branch FROM users WHERE username != 'admin'";
        
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all transactions (for admin)
    public static ResultSet getAllTransactions() {
        String sql = "SELECT t.*, u.username FROM transactions t JOIN users u ON t.account_no = u.account_no ORDER BY t.transaction_date DESC";
        
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Delete user (admin)
    public static boolean deleteUser(String accountNo) {
        String sql1 = "DELETE FROM transactions WHERE account_no = ?";
        String sql2 = "DELETE FROM users WHERE account_no = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Delete transactions first
            try (PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
                pstmt1.setString(1, accountNo);
                pstmt1.executeUpdate();
            }
            
            // Then delete user
            try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setString(1, accountNo);
                int result = pstmt2.executeUpdate();
                conn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Update user profile
    public static boolean updateUserProfile(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone = ?, address = ?, password = ? WHERE account_no = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getAccountNo());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}