<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Password Reset Result</title>
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
        .result-container {
            background-color: white;
            padding: 20px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 300px;
        }
    </style>
</head>
<body>
    <div class="result-container">
        <h2><%= request.getAttribute("successMessage") != null ? "Success" : "Error" %></h2>
        <a href="Login.jsp">Back to Login</a>
    </div>
</body>
</html>
