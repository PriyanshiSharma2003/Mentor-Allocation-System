<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="UserRegisteration.UserDAO" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Home</title>
    <style>
        /* CSS styles */
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        .form-container {
            text-align: center;
            margin-top: 20px;
        }

        .welcome-message {
            color: #4CAF50;
            font-size: 24px;
        }

        .logout-button {
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            margin-bottom: 20px;
        }

        .logout-button:hover {
            background-color: #45a049;
        }

        .popup {
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: white;
            border: 2px solid #4CAF50;
            color: #4CAF50;
            padding: 10px;
            display: none;
            z-index: 1000;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
        }

        .popup img {
            width: 20px;
            height: 20px;
            vertical-align: middle;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<%
    String studentName = (String) session.getAttribute("stdName");
    Boolean hasMentor = (Boolean) session.getAttribute("hasMentor");

    if (studentName == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
%>

<h2 class="welcome-message">Hi, <%= studentName %>! Welcome to your dashboard.</h2>

<!-- Centered Logout Button -->
<form action="LogoutServlet" method="post">
    <button type="submit" class="logout-button">Logout</button>
</form>

<div class="form-container">
    <% if (hasMentor != null && hasMentor) { %>
        <p>You have already selected a mentor.</p>
    <% } else { %>
        <form action="SelectMentorServlet" method="post">
            <label>Select a Mentor:</label>
            <select name="mentor" required>
                <% 
                UserDAO userDAO = new UserDAO();
                List<String> mentors = new ArrayList<>();
                try {
                    mentors = userDAO.getAvailableMentors();
                } catch (Exception e) {
                    out.println("<option disabled>Error loading mentors</option>");
                }

                if (mentors.isEmpty()) {
                    out.println("<option disabled>No mentors available</option>");
                } else {
                    for (String mentor : mentors) {
                %>
                    <option value="<%= mentor %>"><%= mentor %></option>
                <% 
                    }
                }
                %>
            </select>
            <input type="submit" value="Select Mentor">
        </form>
    <% } %>
</div>

<div id="successPopup" class="popup">
    <img src="images/Tick.jpg" alt="Success" />
    Mentor Selected Successfully!
</div>

<% 
    Boolean mentorSelected = (Boolean) session.getAttribute("mentorSelected");
    if (mentorSelected != null && mentorSelected) {
%>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            var popup = document.getElementById("successPopup");
            popup.style.display = "block"; // Show popup
            setTimeout(function() {
                popup.style.display = "none"; // Hide popup after 3 seconds
            }, 3000);
        });
    </script>
    <% session.removeAttribute("mentorSelected"); %>
<% } %>
</body>
</html>