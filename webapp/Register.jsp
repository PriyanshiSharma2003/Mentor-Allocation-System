<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
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
        .register-container {
            background-color: white;
            padding: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 300px;
        }
        input[type="text"], input[type="email"], input[type="tel"], select {
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
        .form-section {
            display: none;
        }
    </style>

    <script>
    function toggleForm() {
        var role = document.getElementById("role").value;

        // Toggle visibility of forms based on role selection
        var mentorForm = document.getElementById("mentorForm");
        var studentForm = document.getElementById("studentForm");

        if (role === "Mentor") {
            mentorForm.style.display = "block";
            studentForm.style.display = "none";
            
            // Remove 'required' attributes from student fields
            document.querySelector("[name='USN']").required = false;
            document.querySelector("[name='branch']").required = false;
            document.querySelector("[name='Semester']").required = false;
            
            // Add 'required' attribute to mentor field
            document.querySelector("[name='department']").required = true;
        } else if (role === "Student") {
            mentorForm.style.display = "none";
            studentForm.style.display = "block";
            
            // Add 'required' attributes to student fields
            document.querySelector("[name='USN']").required = true;
            document.querySelector("[name='branch']").required = true;
            document.querySelector("[name='Semester']").required = true;
            
            // Remove 'required' attribute from mentor field
            document.querySelector("[name='department']").required = false;
        } else {
            mentorForm.style.display = "none";
            studentForm.style.display = "none";
        }
    }

    // Check for URL parameters and show success popup if present
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const username = urlParams.get('username');
        const password = urlParams.get('password');

        if (username && password) {
            alert("Registration Successful!\nUsername: " + username + "\nTemporary Password: " + password);
        }
    }
    </script>

</head>
<body>
    <div class="register-container">
        <h2>Register</h2>
         <form action="RegisterationProcess" method="post">
            <input type="text" name="name" placeholder="Enter Name" required><br>
            <input type="email" name="email" placeholder="Enter Email" required><br>
            <input type="tel" name="phone" placeholder="Enter Phone Number" required pattern="[0-9]{10}" title="Enter a valid 10-digit phone number"><br>

            <label for="role">Register as:</label><br>
            <select id="role" name="role" onchange="toggleForm()" required>
                <option value="">Select Role</option>
                <option value="Mentor">Mentor</option>
                <option value="Student">Student</option>
            </select><br>

            <div id="mentorForm" class="form-section">
                <input type="text" name="department" placeholder="Enter Department" required><br>
            </div>

            <div id="studentForm" class="form-section">
                <input type="text" name="USN" placeholder="Enter University Number" required><br>
                <input type="text" name="branch" placeholder="Enter Branch" required><br>
                <input type="text" name="Semester" placeholder="Enter Semester" required><br>
            </div>

            <button type="submit">Register</button>
        </form>
    </div>
</body>
</html>
