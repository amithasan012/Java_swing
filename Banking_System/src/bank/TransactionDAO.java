package bank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static boolean logTransaction(String txType, String fromAccount, String toAccount, double amount, String desc) {
        String sql = "INSERT INTO transactions(tx_type, from_account, to_account, amount, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, txType);
            ps.setString(2, fromAccount);
            ps.setString(3, toAccount);
            ps.setDouble(4, amount);
            ps.setString(5, desc);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public static List<String[]> getAllTransactions() {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY tx_date DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{
                    rs.getString("tx_date"),
                    rs.getString("tx_type"),
                    rs.getString("from_account"),
                    rs.getString("to_account"),
                    rs.getString("amount"),
                    rs.getString("description")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return rows;
    }

    public static List<String[]> getTransactionsFor(String accountNo) {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_account = ? OR to_account = ? ORDER BY tx_date DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNo);
            ps.setString(2, accountNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                        rs.getString("tx_date"),
                        rs.getString("tx_type"),
                        rs.getString("from_account"),
                        rs.getString("to_account"),
                        rs.getString("amount"),
                        rs.getString("description")
                    });
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return rows;
    }
}
