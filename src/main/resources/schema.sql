CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;
CREATE TABLE IF NOT EXISTS brands (id BIGINT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL);
CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY,first_name VARCHAR(100) NOT NULL,middle_name VARCHAR(100),last_name VARCHAR(100) NOT NULL,email VARCHAR(150) UNIQUE NOT NULL,password VARCHAR(255) NOT NULL,contact_number VARCHAR(20) NOT NULL,role VARCHAR(20) NOT NULL,driving_license VARCHAR(100));
CREATE TABLE IF NOT EXISTS vehicles (id BIGINT AUTO_INCREMENT PRIMARY KEY,model VARCHAR(150) NOT NULL,type VARCHAR(100) NOT NULL,brand_id BIGINT, image_url VARCHAR(255),price_per_day DECIMAL(10,2) NOT NULL,available BOOLEAN DEFAULT TRUE,FOREIGN KEY (brand_id) REFERENCES brands(id));
CREATE TABLE IF NOT EXISTS bookings (id BIGINT AUTO_INCREMENT PRIMARY KEY,customer_id BIGINT,vehicle_id BIGINT,start_date DATE,end_date DATE,returned BOOLEAN DEFAULT FALSE,fine DECIMAL(10,2) DEFAULT 0, status VARCHAR(30),FOREIGN KEY (customer_id) REFERENCES users(id),FOREIGN KEY (vehicle_id) REFERENCES vehicles(id));
CREATE TABLE IF NOT EXISTS payments (id BIGINT AUTO_INCREMENT PRIMARY KEY,booking_id BIGINT,amount DECIMAL(10,2),status VARCHAR(30),timestamp DATETIME,payment_method VARCHAR(50),FOREIGN KEY (booking_id) REFERENCES bookings(id));
CREATE TABLE IF NOT EXISTS testimonials (id BIGINT AUTO_INCREMENT PRIMARY KEY,customer_id BIGINT,message TEXT,rating INT,status VARCHAR(20),FOREIGN KEY (customer_id) REFERENCES users(id));
CREATE TABLE IF NOT EXISTS contact_queries (id BIGINT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(150),email VARCHAR(150),message TEXT);
CREATE TABLE IF NOT EXISTS subscribers (id BIGINT AUTO_INCREMENT PRIMARY KEY,email VARCHAR(150));
CREATE TABLE IF NOT EXISTS otp_verification (id BIGINT AUTO_INCREMENT PRIMARY KEY,email VARCHAR(150),otp_code VARCHAR(10),expires_at DATETIME,verified BOOLEAN DEFAULT FALSE);

INSERT IGNORE INTO users (first_name,middle_name,last_name,email,password,contact_number,role,driving_license)
VALUES ('Admin', NULL, 'User', 'admin@carrental.local', 'Admin123!', '00000000000', 'ADMIN', 'ADMIN123');

CREATE TABLE IF NOT EXISTS page_content (id BIGINT AUTO_INCREMENT PRIMARY KEY, page_key VARCHAR(100) UNIQUE NOT NULL, content TEXT);
INSERT IGNORE INTO page_content (page_key, content) VALUES ('home_description', 'Welcome to Car Rental System');

CREATE TABLE IF NOT EXISTS contact_info (id BIGINT AUTO_INCREMENT PRIMARY KEY, phone VARCHAR(50), email VARCHAR(150), address VARCHAR(255));
INSERT IGNORE INTO contact_info (id, phone, email, address) VALUES (1, '09123456789', 'info@carrental.local', '123 Main St');
