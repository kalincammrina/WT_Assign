package com.student;
import java.sql.Connection; import java.sql.DriverManager; import java.sql.SQLException;
public class DBConnection {
 // Match these values with the MySQL credentials used by your XAMPP installation.
 private static final String URL="jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC";
 private static final String USER="root"; private static final String PASSWORD="";
 public static Connection getConnection() throws SQLException { try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch(ClassNotFoundException e){ throw new SQLException("MySQL JDBC driver not found",e); } return DriverManager.getConnection(URL,USER,PASSWORD); }
}
