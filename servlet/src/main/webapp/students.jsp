<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%!
    // Escape database content before inserting it into HTML text or attributes.
    private String h(Object value) {
        if (value == null) return "";
        return value.toString().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
%>
<%
    List<Map<String,Object>> students = (List<Map<String,Object>>) request.getAttribute("students");
    if (students == null) students = Collections.emptyList();
%>
<!doctype html>
<html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Student profiles | CampusFlow</title><link rel="stylesheet" href="portal.css"></head>
<body class="directory-page">
<header class="portal-header"><a class="brand dark" href="students">CAMPUSFLOW <span>PORTAL</span></a>
<nav><span>Student directory</span><a href="logout">Sign out ↗</a></nav></header>
<main class="portal-content"><div class="tag">DIRECTORY / VERIFIED RECORDS</div>
<div class="title-row"><div><h1>Student profiles</h1><p>Browse student information and access uploaded documents.</p></div>
<div class="count"><strong><%= students.size() %></strong><span>RECORDS</span></div></div>
<div class="profile-grid">
<% for (Map<String,Object> student : students) {
   String name = h(student.get("name"));
   String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase(Locale.ROOT);
   Object fileId = student.get("fileId");
   String mime = String.valueOf(student.get("mime"));
%>
<article class="profile-card"><div class="profile-top"><div class="initials"><%= initial %></div><span class="status">● VERIFIED RECORD</span></div>
<h2><%= name %></h2><div class="roll"><%= h(student.get("roll")) %></div>
<div class="profile-meta"><span>COURSE</span><b><%= h(student.get("course")) %></b><span>EMAIL</span><b><%= h(student.get("email")) %></b></div>
<div class="documents"><strong>FILES &amp; DOCUMENTS</strong>
<% List<Map<String,Object>> files = (List<Map<String,Object>>) student.get("files");
   if (files != null && !files.isEmpty()) {
       for (Map<String,Object> file : files) {
           int currentFileId = ((Number) file.get("id")).intValue();
           String currentMime = String.valueOf(file.get("mime")); %>
<a href="file?id=<%= currentFileId %>"><%= h(file.get("name")) %> ↗</a>
<% if ("image/jpeg".equals(currentMime) || "image/png".equals(currentMime)) { %>
<img class="student-image" src="file?id=<%= currentFileId %>" alt="Uploaded student image">
<% } } } else { %><span class="no-file">No files uploaded</span><% } %>
</div></article>
<% } %>
<% if (students.isEmpty()) { %><p class="empty-state">No student records are available yet. Add students through the PHP administration module.</p><% } %>
</div></main></body></html>
