// User.java
public class User {
    private String accountNo;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String password;
    private double balance;
    private String branch;
    
    //public User() {}
    
    public User(String accountNo, String username, String email, String phone, 
                String address, String password, double balance, String branch) {
        this.accountNo = accountNo;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.balance = balance;
        this.branch = branch;
    }
    
    // Getters and Setters
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
}