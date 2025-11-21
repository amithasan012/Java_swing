CREATE DATABASE bank_of_uob;
USE bank_of_uob;

-- Users table
CREATE TABLE users (
    account_no VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 1000.00,
    branch VARCHAR(50) DEFAULT 'Datiara',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_no VARCHAR(20),
    type ENUM('DEPOSIT', 'WITHDRAW', 'TRANSFER'),
    amount DECIMAL(15,2),
    target_account VARCHAR(20),
    description TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_no) REFERENCES users(account_no)
);

-- Insert admin user
INSERT INTO users (account_no, username, email, password, balance, branch) 
VALUES ('ADMIN001', 'admin', 'admin@uob.com', 'admin123', 0, 'Datiara');