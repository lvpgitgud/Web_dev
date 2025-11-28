/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.dao;

/**
 *
 * @author Admin
 */
import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "lvp";
    private static final String DB_PASSWORD = "050904";
    
    // Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }
    
 
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                return student;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Add new student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_code = ?, full_name = ?, email = ?, major = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
    }
    
    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



private static final String[] VALID_SORT_COLUMNS = {"id", "student_code", "full_name", "email", "major"};

private String validateSortBy(String sortBy) {
    for (String col : VALID_SORT_COLUMNS) {
        if (col.equalsIgnoreCase(sortBy)) {
            return col;
        }
    }
    return "id"; // default
}


private String validateOrder(String order) {
    if ("desc".equalsIgnoreCase(order)) {
        return "DESC";
    }
    return "ASC"; // default
}

public List<Student> getStudentsSorted(String sortBy, String order) {
    List<Student> students = new ArrayList<>();
    
    String validSortBy = validateSortBy(sortBy);
    String validOrder = validateOrder(order);
    
    String sql = "SELECT * FROM students ORDER BY " + validSortBy + " " + validOrder;
    
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setStudentCode(rs.getString("student_code"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            student.setCreatedAt(rs.getTimestamp("created_at"));
            students.add(student);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return students;
}

public List<Student> getStudentsByMajor(String major) {
    List<Student> students = new ArrayList<>();
    String sql = "SELECT * FROM students WHERE major = ? ORDER BY id DESC";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, major);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setStudentCode(rs.getString("student_code"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            student.setCreatedAt(rs.getTimestamp("created_at"));
            students.add(student);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return students;
}

public List<Student> getStudentsFiltered(String major, String sortBy, String order) {
    List<Student> students = new ArrayList<>();
    
    String validSortBy = validateSortBy(sortBy);
    String validOrder = validateOrder(order);
    
    String sql = "SELECT * FROM students WHERE major = ? ORDER BY " + validSortBy + " " + validOrder;
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, major);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setStudentCode(rs.getString("student_code"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            student.setCreatedAt(rs.getTimestamp("created_at"));
            students.add(student);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return students;
}

public List<Student> searchStudents(String keyword) {
    List<Student> students = new ArrayList<>();
    
    if (keyword == null || keyword.trim().isEmpty()) {
        // If keyword is empty, return all students
        return getAllStudents();
    }
    
    String sql = "SELECT * FROM students " +
                 "WHERE student_code LIKE ? OR full_name LIKE ? OR email LIKE ? " +
                 "ORDER BY id DESC";
    
    String searchPattern = "%" + keyword.trim() + "%";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, searchPattern);
        pstmt.setString(2, searchPattern);
        pstmt.setString(3, searchPattern);
        
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setStudentCode(rs.getString("student_code"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            student.setCreatedAt(rs.getTimestamp("created_at"));
            students.add(student);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return students;
}

public int getTotalStudents() {
    String sql = "SELECT COUNT(*) FROM students";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0; // default if error occurs
}

public List<Student> getStudentsPaginated(int offset, int limit) {
    List<Student> students = new ArrayList<>();
    String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, limit);
        pstmt.setInt(2, offset);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setStudentCode(rs.getString("student_code"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            student.setCreatedAt(rs.getTimestamp("created_at"));
            students.add(student);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return students;
}
public List<Student> getStudentsCombined(String keyword, String major, String sortBy, String order) {
    List<Student> students = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM students WHERE 1=1");

    if (keyword != null && !keyword.trim().isEmpty()) {
        sql.append(" AND (student_code LIKE ? OR full_name LIKE ? OR email LIKE ?)");
    }

    if (major != null && !major.trim().isEmpty()) {
        sql.append(" AND major = ?");
    }

    sql.append(" ORDER BY ").append(validateSortBy(sortBy)).append(" ").append(validateOrder(order));

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

        int index = 1;
        if (keyword != null && !keyword.trim().isEmpty()) {
            String pattern = "%" + keyword.trim() + "%";
            pstmt.setString(index++, pattern);
            pstmt.setString(index++, pattern);
            pstmt.setString(index++, pattern);
        }

        if (major != null && !major.trim().isEmpty()) {
            pstmt.setString(index++, major);
        }

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setStudentCode(rs.getString("student_code"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMajor(rs.getString("major"));
            student.setCreatedAt(rs.getTimestamp("created_at"));
            students.add(student);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return students;

}

}