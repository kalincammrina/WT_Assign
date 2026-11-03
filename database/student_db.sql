CREATE DATABASE IF NOT EXISTS student_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_db;
CREATE TABLE IF NOT EXISTS students (
 id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(120) NOT NULL,
 roll_number VARCHAR(40) NOT NULL UNIQUE,
 email VARCHAR(160) NOT NULL UNIQUE,
 course VARCHAR(100) NOT NULL,
 password_hash VARCHAR(255) NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS student_files (
 id INT AUTO_INCREMENT PRIMARY KEY,
 student_id INT NOT NULL,
 original_name VARCHAR(255) NOT NULL,
 stored_name VARCHAR(255) NOT NULL,
 file_path VARCHAR(500) NOT NULL,
 mime_type VARCHAR(120) NOT NULL,
 file_size BIGINT NOT NULL,
 file_category ENUM('profile','document') NOT NULL DEFAULT 'document',
 uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_student_files_student FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS portal_users (
 id INT AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(80) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Create a login user after importing the schema with:
-- php -r "echo password_hash('ChangeMe123!', PASSWORD_DEFAULT), PHP_EOL;"
-- INSERT INTO portal_users(username,password_hash) VALUES ('admin','PASTE_HASH_HERE');
