<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 0;
        }

        /* NAVBAR */
        .navbar {
            background: #2c3e50;
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .navbar h2 {
            font-size: 20px;
        }
        
        .navbar-right {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .role-badge {
            padding: 4px 10px;
            color: white;
            border-radius: 5px;
            font-size: 12px;
            margin-left: 5px;
            text-transform: uppercase;
            font-weight: bold;
        }
        .role-admin { background-color: #dc3545; }
        .role-user { background-color: #007bff; }

        
        .container {
            max-width: 450px;
            margin: 60px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        .btn-logout {
            padding: 8px 20px;
            background: #e74c3c;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
            transition: background 0.3s;
        }

        h1 {
            text-align: center;
            color: #444;
            margin-bottom: 25px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group label {
            font-weight: 500;
            color: #333;
        }

        input[type="password"] {
            width: 100%;
            padding: 12px;
            border-radius: 6px;
            border: 1px solid #ccc;
            margin-top: 6px;
            font-size: 15px;
        }

        input[type="password"]:focus {
            border-color: #667eea;
            outline: none;
            box-shadow: 0 0 4px rgba(102,126,234,0.4);
        }

        .btn-submit {
            width: 100%;
            padding: 12px;
            margin-top: 10px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            color: white;
            cursor: pointer;
            font-weight: 600;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            transition: 0.2s;
        }

        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(102, 126, 234, 0.4);
        }

        .message {
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 5px;
            font-weight: 500;
        }
        .success { background: #d4edda; color: #155724; }
        .error   { background: #f8d7da; color: #721c24; }
        .btn-dashboard {
            padding: 8px 20px;
            background: blue;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
            transition: background 0.3s;
        }
        
        .btn-dashboard:hover {background: #667eea;}
        
        .btn-logout:hover {
            background: #c0392b;
        }
    </style>
</head>

<body>

    <!-- NAVBAR -->
    <div class="navbar">
        <h2>🔐 Change Password</h2>

        <div class="navbar-right">
            <div class="user-info">
                Welcome, ${sessionScope.fullName}
                <span class="role-badge role-${sessionScope.role}">
                    ${sessionScope.role}
                </span>
            </div>

            <a href="dashboard" class = "btn-dashboard">Dashboard</a>
            <a href="logout" class = "btn-logout">Logout</a>
        </div>
    </div>


    <div class="container">

        <!-- Success Message -->
        <c:if test="${not empty success}">
            <div class="message success">
                ✅ ${success}
            </div>
        </c:if>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="message error">
                ❌ ${error}
            </div>
        </c:if>

        <h1>Change Password</h1>

        <form action="change_password" method="post">

            <div class="form-group">
                <label>Current Password</label>
                <input type="password" name="currentPassword" required>
            </div>

            <div class="form-group">
                <label>New Password</label>
                <input type="password" name="newPassword" minlength="8" required>
            </div>

            <div class="form-group">
                <label>Confirm New Password</label>
                <input type="password" name="confirmPassword" minlength="8" required>
            </div>

            <button type="submit" class="btn-submit">Update Password</button>
        </form>

    </div>

</body>
</html>
