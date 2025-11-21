CREATE DATABASE IF NOT EXISTS hospitaldb;
USE hospitaldb;

-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Insert Admin Account
INSERT INTO users (username, password, role) 
VALUES ('admin', 'admin123', 'admin');

-- Insert example staff (optional)
INSERT INTO users (username, password, role)
VALUES ('staff1', 'staff123', 'staff');

-- Insert example patient/user (optional)
INSERT INTO users (username, password, role)
VALUES ('user1', 'user123', 'user');


-- DOCTORS TABLE
CREATE TABLE IF NOT EXISTS doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(100),
    fee DOUBLE,
    phone VARCHAR(50),
    available_time VARCHAR(100)
);


-- APPOINTMENTS TABLE
CREATE TABLE IF NOT EXISTS appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    doctor_id INT,
    date VARCHAR(50),
    time VARCHAR(50),
    status VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);
