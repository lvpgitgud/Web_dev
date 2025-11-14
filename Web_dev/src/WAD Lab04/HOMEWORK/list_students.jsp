<%-- 
    Document   : list_students
    Created on : Nov 8, 2025, 9:34:48 AM
    Author     : Admin
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <style>
    body {
        font-family: Arial, sans-serif;
        margin: 20px;
        background-color: #f5f5f5;
    }
    h1 { color: #333; }

    .message {
        padding: 12px 16px;
        border-radius: 5px;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: 500;
        width: fit-content;
        animation: fadeIn 0.3s ease-in;
    }
    .success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }
    .error {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .btn {
        display: inline-block;
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 5px;
        margin-bottom: 15px;
    }
    .btn:hover {
        background-color: #0056b3;
    }

    .table-responsive {
        overflow-x: auto;
        background-color: white;
        border-radius: 5px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }
    table {
        width: 100%;
        border-collapse: collapse;
        min-width: 700px;
    }
    th {
        background-color: #007bff;
        color: white;
        padding: 12px;
        text-align: left;
    }
    td {
        padding: 10px;
        border-bottom: 1px solid #ddd;
    }
    tr:hover { background-color: #f8f9fa; }

    @media (max-width: 768px) {
        table { font-size: 12px; }
        th, td { padding: 6px; }
    }

    .pagination {
        text-align: center;
        margin-top: 20px;
    }
    .pagination a, .pagination strong {
        display: inline-block;
        margin: 0 5px;
        padding: 8px 12px;
        text-decoration: none;
        border-radius: 5px;
    }
    .pagination a {
        background: #007bff;
        color: #fff;
    }
    .pagination a:hover {
        background: #0056b3;
    }
    .pagination strong {
        background: #6c757d;
        color: #fff;
    }
    </style>
</head>
<body>

    <h1>📚 Student Management System</h1>
    <% if (request.getParameter("message") != null) { %>
        <div class="message success">✓ <%= request.getParameter("message") %></div>
    <% } %>

    <% if (request.getParameter("error") != null) { %>
        <div class="message error">✗ <%= request.getParameter("error") %></div>
    <% } %>

    
    <a href="add_student.jsp" class="btn">➕ Add New Student</a>
    <a href="export_csv.jsp<%= (request.getParameter("keyword") != null && !request.getParameter("keyword").isEmpty()) 
    ? "?keyword=" + request.getParameter("keyword") 
    : "" %>" 
    class="btn">⬇️ Export CSV</a>

    
    <form action="list_students.jsp" method="GET" onsubmit="return submitForm(this)">
    <input type="text" name="keyword" placeholder="Search by name, code, or major..."
           value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>">
    <button type="submit" class="btn">Search</button>
    <a href="list_students.jsp" class="btn">Clear</a>
    </form>
    <%
        String order = request.getParameter("order");
        String sortBy = request.getParameter("sort");
        String keyword = request.getParameter("keyword");
        String pageParam = request.getParameter("page");
    

        // Default 
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";
        if (order == null || order.trim().isEmpty()) order = "desc";

        if(keyword == null){ //THIS IS A HACK TO DEAL WITH EMPTY KEYWORD
            keyword = "";
        }
    
    %>
    <form method="get" style="margin-bottom: 15px; display:flex; gap:10px; align-items:center;">
    <input type="hidden" name="keyword" value="<%= keyword %>">

    <input type="hidden" name="page" value="<%= pageParam %>">

    <label>Sort By:</label>

    <select name="sort" onchange="this.form.submit()">
        <option value="id" <%= sortBy.equals("id") ? "selected" : "" %>>ID</option>
        <option value="student_code" <%= sortBy.equals("student_code") ? "selected" : "" %>>Student Code</option>
        <option value="full_name" <%= sortBy.equals("full_name") ? "selected" : "" %>>Full Name</option>
        <option value="email" <%= sortBy.equals("email") ? "selected" : "" %>>Email</option>
        <option value="major" <%= sortBy.equals("major") ? "selected" : "" %>>Major</option>
        <option value="created_at" <%= sortBy.equals("created_at") ? "selected" : "" %>>Created At</option>
    </select>
    <select name="order" onchange="this.form.submit()">
        <option value="asc" <%= order.equals("asc") ? "selected" : "" %>>Ascending</option>
        <option value="desc" <%= order.equals("desc") ? "selected" : "" %>>Descending</option>
</select>

</form>
    <div class="table-responsive">
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Student Code</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Major</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
<%
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = null;
 

    
    

    

    int currentPage = 1;
    if (pageParam != null) {
        try { currentPage = Integer.parseInt(pageParam); } catch (NumberFormatException e) { currentPage = 1; }
    }
    int recordsPerPage = 10;
    int offset = (currentPage - 1) * recordsPerPage;
    //
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/student_management",
            "lvp",
            "050904"
        );  
        String countSql;
        PreparedStatement countStmt;
        if (keyword != null && !keyword.trim().isEmpty()) {
            countSql = "SELECT COUNT(*) FROM students WHERE full_name LIKE ? OR student_code LIKE ? OR major LIKE ?";
            countStmt = conn.prepareStatement(countSql);
            String search = "%" + keyword.trim() + "%";
            countStmt.setString(1, search);
            countStmt.setString(2, search);
            countStmt.setString(3, search);
        } else {
            countSql = "SELECT COUNT(*) FROM students";
            countStmt = conn.prepareStatement(countSql);
        }

        ResultSet countRs = countStmt.executeQuery();
        countRs.next();
        int totalRecords = countRs.getInt(1);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        countRs.close();
        countStmt.close();

        
        PreparedStatement pstmt;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql = "SELECT * FROM students WHERE full_name LIKE ? OR student_code LIKE ? OR major LIKE ? ORDER BY " + sortBy + " " + order + " LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            String search = "%" + keyword.trim() + "%";
            pstmt.setString(1, search);
            pstmt.setString(2, search);
            pstmt.setString(3, search);
            pstmt.setInt(4, recordsPerPage);
            pstmt.setInt(5, offset);
        } else {
            sql = "SELECT * FROM students ORDER BY " + sortBy + " " + order + " LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recordsPerPage);
            pstmt.setInt(2, offset);
        }

        rs = pstmt.executeQuery();

        boolean hasResults = false;
        while (rs.next()) {
            hasResults = true;
            int id = rs.getInt("id");
            String studentCode = rs.getString("student_code");
            String fullName = rs.getString("full_name");
            String email = rs.getString("email");
            String major = rs.getString("major");
            Timestamp createdAt = rs.getTimestamp("created_at");
%>
            <tr>
                <td><%= id %></td>
                <td><%= studentCode %></td>
                <td><%= fullName %></td>
                <td><%= email != null ? email : "N/A" %></td>
                <td><%= major != null ? major : "N/A" %></td>
                <td><%= createdAt %></td>
                <td>
                    <a href="edit_student.jsp?id=<%= id %>" class="action-link">✏️ Edit</a>
                    <a href="delete_student.jsp?id=<%= id %>" 
                       class="action-link delete-link"
                       onclick="return confirm('Are you sure?')">🗑️ Delete</a>
                </td>
            </tr>
            <%
        }
        if (!hasResults) {
            out.println("<tr><td colspan='7'>No students found.</td></tr>");
        }

        

            %>
                    
        </tbody>
    
    </table>
                    
    </div>        
    <div class="pagination">             
        <% if (currentPage > 1) { %>
            <a href="list_students.jsp?sort=<%=sortBy%>&order=<%=order%>&keyword=<%=keyword%>&page=<%= currentPage - 1 %>">Previous</a>
            <% } %>
    
    
            <% for (int i = 1; i <= totalPages; i++) { %>
                <% if (i == currentPage) { %>
                    <strong><%= i %></strong>
                <% } else { %>
                    <a href="list_students.jsp?sort=<%=sortBy%>&order=<%=order%>&keyword=<%=keyword%>&page=<%= i %>"><%= i %></a>
                    <% } %>
                    <% } %>
            <% if (currentPage < totalPages) { %>
                <a href="list_students.jsp?sort=<%=sortBy%>&order=<%=order%>&keyword=<%=keyword%>&page=<%= currentPage + 1 %>">Next</a>
                <% } %>
    </div>
<%
    } catch (ClassNotFoundException e) {
        out.println("<tr><td colspan='7'>Error: JDBC Driver not found!</td></tr>");
        e.printStackTrace();
    } catch (SQLException e) {
        out.println("<tr><td colspan='7'>Database Error: " + e.getMessage() + "</td></tr>");
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>

       
<script>
    setTimeout(function() {
        var messages = document.querySelectorAll('.message');
        messages.forEach(function(msg) {
            msg.style.display = 'none';
        });
    }, 3000);
</script>
<script>
    function submitForm(form) {
        var btn = form.querySelector('button[type="submit"]');
        btn.disabled = true;
        btn.textContent = 'Processing...';
        return true; // continue submitting
    }
</script>
</body>
</html>

