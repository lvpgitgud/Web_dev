<%-- 
    Document   : export_csv
    Created on : Nov 13, 2025, 4:32:26 PM
    Author     : Admin
--%>

<%@ page language="java" contentType="text/csv; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<%
response.setContentType("text/csv");
response.setHeader("Content-Disposition", "attachment; filename=\"students.csv\"");

Connection conn = null;
PreparedStatement pstmt = null;
ResultSet rs = null;

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/student_management",
        "lvp",
        "050904"
    );

    String keyword = request.getParameter("keyword");
    String sql;

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql = "SELECT * FROM students WHERE full_name LIKE ? OR student_code LIKE ? OR major LIKE ? ORDER BY id DESC";
        pstmt = conn.prepareStatement(sql);
        String search = "%" + keyword.trim() + "%";
        pstmt.setString(1, search);
        pstmt.setString(2, search);
        pstmt.setString(3, search);
    } else {
        sql = "SELECT * FROM students ORDER BY id DESC";
        pstmt = conn.prepareStatement(sql);
    }

    rs = pstmt.executeQuery();

    out.println("ID,Student Code,Full Name,Email,Major");

    while (rs.next()) {
        String id = String.valueOf(rs.getInt("id"));
        String studentCode = rs.getString("student_code");
        String fullName = rs.getString("full_name") ;
        String email = rs.getString("email") ;
        String major = rs.getString("major") ;

        out.println("\"" + id + "\",\"" + studentCode + "\",\"" + fullName + "\",\"" + email + "\",\"" + major + "\"");
    }

} catch (Exception e) {
    out.println("Error exporting CSV: " + e.getMessage());
} finally {
    if (rs != null) rs.close();
    if (pstmt != null) pstmt.close();
    if (conn != null) conn.close();
}
%>

