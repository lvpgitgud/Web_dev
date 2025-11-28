package com.student.controller;

import com.student.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.student.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet("/change_password")
public class ChangePasswordController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/change_password.jsp")
                .forward(request, response);
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    User user = (User) session.getAttribute("user"); // Correct attribute

    if (user == null) {
        response.sendRedirect("login?error=Please login first");
        return;
    }

    String currentPassword = request.getParameter("currentPassword");
    String newPassword = request.getParameter("newPassword");
    String confirmPassword = request.getParameter("confirmPassword");


    User dbUser = userDAO.getUserById(user.getId());


    if (!BCrypt.checkpw(currentPassword, dbUser.getPassword())) {
        request.setAttribute("error", "Current password is incorrect.");
        request.getRequestDispatcher("views/change_password.jsp").forward(request, response);
        return;
    }


    if (newPassword.length() < 8) {
        request.setAttribute("error", "New password must be at least 8 characters.");
        request.getRequestDispatcher("views/change_password.jsp").forward(request, response);
        return;
    }

    if (!newPassword.equals(confirmPassword)) {
        request.setAttribute("error", "Confirm password does not match.");
        request.getRequestDispatcher("views/change_password.jsp").forward(request, response);
        return;
    }

    String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    boolean updated = userDAO.updatePassword(user.getId(), hashedNewPassword);

    if (updated) {
        user.setPassword(hashedNewPassword); 
        request.setAttribute("success", "Password changed successfully!");
    } else {
        request.setAttribute("error", "Failed to update password.");
    }

    request.getRequestDispatcher("views/change_password.jsp").forward(request, response);
}
}