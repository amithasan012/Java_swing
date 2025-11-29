CREATE DATABASE carrental;
USE carrental;

-- USERS TABLE
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    role VARCHAR(10)  -- 'admin' or 'user'
);

INSERT INTO users(username, password, role)
VALUES ('admin', 'admin123', 'admin');

-- CARS TABLE
CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(50),
    seats INT,
    price_day DOUBLE,
    price_week DOUBLE,
    price_month DOUBLE,
    status VARCHAR(20) DEFAULT 'available'  -- available / rented
);

-- TRANSACTIONS TABLE
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    car_id INT,
    amount DOUBLE,
    rent_date VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);
