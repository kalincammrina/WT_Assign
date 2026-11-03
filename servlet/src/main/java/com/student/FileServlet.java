package com.student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Streams a student's uploaded file after checking the portal session and file path. */
@WebServlet("/file")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
            if (id <= 0) throw new NumberFormatException("Invalid file id");
        } catch (NumberFormatException ex) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file id");
            return;
        }

        String relativePath;
        String mimeType;
        String originalName;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT original_name, file_path, mime_type FROM student_files WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
                    return;
                }
                originalName = result.getString("original_name");
                relativePath = result.getString("file_path");
                mimeType = result.getString("mime_type");
            }
        } catch (SQLException ex) {
            throw new ServletException("Unable to retrieve file metadata", ex);
        }

        String configuredRoot = getServletContext().getInitParameter("uploadRoot");
        if (configuredRoot == null || configuredRoot.trim().isEmpty()) {
            throw new ServletException("Set the uploadRoot context parameter in WEB-INF/web.xml");
        }

        Path root = Paths.get(configuredRoot).toAbsolutePath().normalize();
        Path file = root.resolve(relativePath).normalize();
        // Prevent ../ traversal and symbolic paths outside the configured upload directory.
        if (!file.startsWith(root) || !Files.isRegularFile(file)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Stored file not found");
            return;
        }

        String safeName = originalName == null ? "download" : originalName.replaceAll("[\\r\\n\"\\\\]", "_");
        res.setContentType(mimeType == null ? "application/octet-stream" : mimeType);
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("Content-Disposition", "inline; filename=\"" + safeName + "\"");
        res.setContentLengthLong(Files.size(file));

        try (InputStream input = Files.newInputStream(file);
             OutputStream output = res.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
}
