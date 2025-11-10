package bank;

public class BankService {
    public static final double MIN_BALANCE = 1000.0;

    public static boolean deposit(User user, double amount) {
        if (amount <= 0) return false;
        double newBal = user.getBalance() + amount;
        if (UserDAO.changeBalance(user.getAccountNo(), newBal)) {
            TransactionDAO.logTransaction("DEPOSIT", user.getAccountNo(), user.getAccountNo(), amount, "Deposit by user");
            user.setBalance(newBal);
            return true;
        }
        return false;
    }

    public static boolean withdraw(User user, double amount) {
        if (amount <= 0) return false;
        double newBal = user.getBalance() - amount;
        if (newBal < MIN_BALANCE) return false;
        if (UserDAO.changeBalance(user.getAccountNo(), newBal)) {
            TransactionDAO.logTransaction("WITHDRAW", user.getAccountNo(), user.getAccountNo(), amount, "Withdraw by user");
            user.setBalance(newBal);
            return true;
        }
        return false;
    }

    public static boolean transfer(User fromUser, String toAccountNo, double amount) {
        if (amount <= 0) return false;
        if (toAccountNo.equals(fromUser.getAccountNo())) return false;
        User toUser = UserDAO.findByAccountNo(toAccountNo);
        if (toUser == null) return false;
        double newFromBal = fromUser.getBalance() - amount;
        if (newFromBal < MIN_BALANCE) return false;
        double newToBal = toUser.getBalance() + amount;

        // basic transaction without DB transaction locking for simplicity
        boolean a = UserDAO.changeBalance(fromUser.getAccountNo(), newFromBal);
        boolean b = UserDAO.changeBalance(toUser.getAccountNo(), newToBal);
        if (a && b) {
            TransactionDAO.logTransaction("TRANSFER", fromUser.getAccountNo(), toAccountNo, amount, "Transfer to " + toAccountNo);
            fromUser.setBalance(newFromBal);
            return true;
        } else {
            // if one failed, ideally rollback - omitted for brevity
            return false;
        }
    }
}
