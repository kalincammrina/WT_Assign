package com.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Loads students and every associated upload for the dynamic JSP profile directory. */
@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        List<Map<String, Object>> students = new ArrayList<>();
        String studentSql = "SELECT id, name, roll_number, email, course FROM students ORDER BY name";
        String fileSql = "SELECT id, original_name, mime_type, file_category FROM student_files " +
                "WHERE student_id = ? ORDER BY uploaded_at DESC, id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement studentStatement = connection.prepareStatement(studentSql);
             ResultSet studentRows = studentStatement.executeQuery();
             PreparedStatement fileStatement = connection.prepareStatement(fileSql)) {
            while (studentRows.next()) {
                Map<String, Object> student = new HashMap<>();
                int studentId = studentRows.getInt("id");
                student.put("id", studentId);
                student.put("name", studentRows.getString("name"));
                student.put("roll", studentRows.getString("roll_number"));
                student.put("email", studentRows.getString("email"));
                student.put("course", studentRows.getString("course"));

                List<Map<String, Object>> files = new ArrayList<>();
                fileStatement.setInt(1, studentId);
                try (ResultSet fileRows = fileStatement.executeQuery()) {
                    while (fileRows.next()) {
                        Map<String, Object> file = new HashMap<>();
                        file.put("id", fileRows.getInt("id"));
                        file.put("name", fileRows.getString("original_name"));
                        file.put("mime", fileRows.getString("mime_type"));
                        file.put("category", fileRows.getString("file_category"));
                        files.add(file);
                    }
                }
                student.put("files", files);
                students.add(student);
            }
        } catch (SQLException ex) {
            throw new ServletException("Could not load student records. Check the database connection and schema.", ex);
        }

        req.setAttribute("students", students);
        req.getRequestDispatcher("students.jsp").forward(req, res);
    }
}
