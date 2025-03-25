<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
        }
        .forgot-password-container {
            background-color: white;
            padding: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 300px;
        }
        input[type="text"], input[type="password"], select {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            width: 100%;
            border: none;
            cursor: pointer;
        }
        button:hover {
            opacity: 0.8;
        }
        .login-link {
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="forgot-password-container">
        <h2>Forgot Password</h2>
          <form action="PasswordResetServlet" method="post">

            <input type="text" name="username" placeholder="Enter your username" required><br>
            <input type="password" name="newPassword" placeholder="Enter new password" required><br>
            <label for="role">Select Role:</label><br>
            <select name="role" required>
                <option value="Mentor">Mentor</option>
                <option value="Student">Student</option>
            </select><br>
            <button type="submit">Save Password</button>
        </form>
        <div class="login-link">
            <p>Remembered your password? <a href="Login.jsp">Login</a></p>
        </div>
    </div>
</body>
</html>
