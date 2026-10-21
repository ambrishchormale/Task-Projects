CREATE DATABASE employee_leave_db;

USE employee_leave_db;

CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    department VARCHAR(50)
);

CREATE TABLE leave_balance (
    employee_id INT PRIMARY KEY,
    total_leaves INT DEFAULT 12,
    leaves_taken INT DEFAULT 0,
    FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
);

CREATE TABLE leave_requests (
    leave_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(200),
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
);

use employee_leave_db;
show TABLES;