package com.student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

/** Authenticates portal users against bcrypt password hashes stored in MySQL. */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean authenticated = false;

        if (username != null && password != null) {
            String sql = "SELECT id, username, password_hash FROM portal_users WHERE username = ?";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username.trim());
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        String storedHash = result.getString("password_hash");
                        authenticated = storedHash != null && storedHash.startsWith("$2")
                                && BCrypt.checkpw(password, storedHash);
                        if (authenticated) {
                            HttpSession oldSession = req.getSession(false);
                            if (oldSession != null) oldSession.invalidate();
                            HttpSession session = req.getSession(true);
                            session.setAttribute("userId", result.getInt("id"));
                            session.setAttribute("username", result.getString("username"));
                        }
                    }
                }
            } catch (Exception ex) {
                throw new ServletException("Login could not be completed. Check the portal database configuration.", ex);
            }
        }

        if (authenticated) {
            res.sendRedirect("students");
        } else {
            req.setAttribute("error", "Invalid username or password.");
            req.getRequestDispatcher("login.jsp").forward(req, res);
        }
    }
}
