<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
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
        .login-container {
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
        .register-link, .forgot-password-link {
            margin-top: 15px;
        }
        .popup {
            position: fixed;
            top: 20px;
            right: 20px;
            background-color: white;
            border: 2px solid #4CAF50;
            color: #4CAF50;
            padding: 10px;
            display: none;
            z-index: 1000;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Login</h2>
        <form action="LoginProcess" method="post">
            <input type="text" name="username" placeholder="Enter Username" required><br>
            <input type="password" name="password" placeholder="Enter Password" required><br>
            <label for="role">Login as:</label><br>
            <select name="role" required>
                <option value="Mentor">Mentor</option>
                <option value="Student">Student</option>
            </select><br>
            <button type="submit">Login</button>
        </form>

        <div class="forgot-password-link">
            <p><a href="ForgotPassword.jsp">Forgot Password?</a></p>
        </div>

        <div class="register-link">
            <p>Don't have an account? <a href="Register.jsp">Register</a></p>
        </div>
    </div>

    <div id="logoutPopup" class="popup">You have logged out successfully!</div>

    <script>
        window.onload = function() {
            // Show the logout popup if logout=true is in the URL
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get('logout') === 'true') {
                var popup = document.getElementById("logoutPopup");
                popup.style.display = "block"; // Show popup
                setTimeout(function() {
                    popup.style.display = "none"; // Hide popup after 2 seconds
                }, 2000);
            }

            const username = urlParams.get('username');
            const password = urlParams.get('password');

            if (username && password) {
                alert("Registration Successful!\nUsername: " + username + "\nTemporary Password: " + password);
            }
        }
    </script>
</body>
</html>
