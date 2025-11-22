/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.controller;

/**
 *
 * @author Admin
 */
import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {
    
    private StudentDAO studentDAO;
    
    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            case "search":
                searchStudents(request, response);
                break;
            case "sort":
                sortStudents(request, response);
                break;
            case "filter":
                filterStudents(request, response);
                break;
            case "filterSort":
                filterAndSortStudents(request, response);
                break;
            case "listCombined":
                listCombined(request, response);
                break;
            default:
                listStudentsPaginated(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
        }
    }
    

    
    // Show form for new student
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    // Show form for editing student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);
        
        request.setAttribute("student", existingStudent);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    private void insertStudent(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

    String studentCode = request.getParameter("studentCode");
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String major = request.getParameter("major");

    Student newStudent = new Student(studentCode, fullName, email, major);

    if (!validateStudent(newStudent, request)) {
        // Forward back to form with errors
        request.setAttribute("student", newStudent);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
        return;
    }

    if (studentDAO.addStudent(newStudent)) {
        response.sendRedirect("student?action=list&message=Student added successfully" );
    } else {
        request.setAttribute("student", newStudent);
        request.setAttribute("errorCode", "Failed to add student. Try again.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
}
    
    private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

    int id = Integer.parseInt(request.getParameter("id"));
    String studentCode = request.getParameter("studentCode");
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String major = request.getParameter("major");

    Student student = new Student(studentCode, fullName, email, major);
    student.setId(id);
    

    if (!validateStudent(student, request)) {
        request.setAttribute("student", student);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
        return;
    }

    if (studentDAO.updateStudent(student)) {
        response.sendRedirect("student?action=list&message=Student updated successfully");
    } else {
        request.setAttribute("student", student);
        request.setAttribute("errorCode", "Failed to update student. Try again.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
}
    
    // Delete student
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }
    
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String keyword = request.getParameter("keyword");
    

    List<Student> students;

    if (keyword == null || keyword.trim().isEmpty()) {
        // If no keyword 
        students = studentDAO.getAllStudents();
        keyword = ""; 
    } else {
        students = studentDAO.searchStudents(keyword);
    }

    request.setAttribute("students", students);
    request.setAttribute("keyword", keyword);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);

    }
    private boolean validateStudent(Student student, HttpServletRequest request) {
    boolean isValid = true;

    String code = student.getStudentCode();


    if (code == null || code.trim().isEmpty()) {
        request.setAttribute("errorCode", "Student code is required");
        isValid = false;
    } else if (!code.matches("[A-Z]{2}[0-9]{3,}")) {
        request.setAttribute("errorCode", "Invalid format. Use 2 letters + 3+ digits (e.g., SV001)");
        isValid = false;
    }


    String fullName = student.getFullName();
    if (fullName == null || fullName.trim().isEmpty()) {
        request.setAttribute("errorName", "Full name is required");
        isValid = false;
    } else if (fullName.trim().length() < 2) {
        request.setAttribute("errorName", "Full name must be at least 2 characters");
        isValid = false;
    }

    String email = student.getEmail();
    if (email != null && !email.trim().isEmpty()) {

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            request.setAttribute("errorEmail", "Invalid email format");
            isValid = false;
        }
    }

    String major = student.getMajor();
    if (major == null || major.trim().isEmpty()) {
        request.setAttribute("errorMajor", "Major is required");
        isValid = false;
    }

    return isValid;
}
    private void sortStudents(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String sortBy = request.getParameter("sortBy"); // e.g., "full_name"
    String order = request.getParameter("order");   // "asc" or "desc"

    List<Student> students = studentDAO.getStudentsSorted(sortBy, order);

    request.setAttribute("students", students);
    request.setAttribute("sortBy", sortBy);
    request.setAttribute("order", order);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}
    
private void filterStudents(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String major = request.getParameter("major");
    List<Student> students;

    if (major == null || major.trim().isEmpty()) {
        students = studentDAO.getAllStudents();
        major = ""; 
    } else {
        students = studentDAO.getStudentsByMajor(major);
    }

    request.setAttribute("students", students);
    request.setAttribute("selectedMajor", major); 
    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}

private void filterAndSortStudents(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String major = request.getParameter("major");
    String sortBy = request.getParameter("sortBy");
    String order = request.getParameter("order");

    // Default values
    if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
    if (order == null || order.isEmpty()) order = "asc";

    List<Student> students;

    if (major != null && !major.trim().isEmpty()) {
        // Filter by major AND sort
        students = studentDAO.getStudentsFiltered(major, sortBy, order);
        request.setAttribute("selectedMajor", major);
    } else {
        // No major filter → show all students with sorting
        students = studentDAO.getStudentsSorted(sortBy, order);
        request.setAttribute("selectedMajor", ""); // no filter selected
    }

    request.setAttribute("students", students);
    request.setAttribute("sortBy", sortBy);
    request.setAttribute("order", order);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}

private void listStudents(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

    // Get optional parameters
    String sortBy = request.getParameter("sortBy");
    String order = request.getParameter("order");
    String major = request.getParameter("major");

    // Default values
    if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
    if (order == null || order.isEmpty()) order = "asc";

    List<Student> students;

    // Check if major filter is applied
    if (major != null && !major.isEmpty()) {
        // Filter by major with sorting
        students = studentDAO.getStudentsFiltered(major, sortBy, order);
        request.setAttribute("selectedMajor", major);
    } else {
        // Only sorting
        students = studentDAO.getStudentsSorted(sortBy, order);
        request.setAttribute("selectedMajor", "");
    }

    // Set attributes for view
    request.setAttribute("students", students);
    request.setAttribute("sortBy", sortBy);
    request.setAttribute("order", order);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}

private void listStudentsPaginated(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String pageParam = request.getParameter("page");
    int currentPage = 1;
    if (pageParam != null) {
        try {
            currentPage = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
    }

    int recordsPerPage = 10;

    int totalRecords = studentDAO.getTotalStudents();

    int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

    if (currentPage < 1) currentPage = 1;
    if (currentPage > totalPages) currentPage = totalPages > 0 ? totalPages : 1;

    int offset = (currentPage - 1) * recordsPerPage;

    List<Student> students = studentDAO.getStudentsPaginated(offset, recordsPerPage);

    request.setAttribute("students", students);
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}

private void listCombined(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String keyword = request.getParameter("keyword");
    String major = request.getParameter("major");
    String sortBy = request.getParameter("sortBy");
    String order = request.getParameter("order");

    List<Student> students = studentDAO.getStudentsCombined(keyword, major, sortBy, order);

    request.setAttribute("students", students);
    request.setAttribute("keyword", keyword != null ? keyword : "");
    request.setAttribute("selectedMajor", major != null ? major : "");
    request.setAttribute("sortBy", sortBy != null ? sortBy : "id");
    request.setAttribute("order", order != null ? order : "asc");

    RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
    dispatcher.forward(request, response);
}

}
