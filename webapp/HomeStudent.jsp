<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="UserRegisteration.UserDAO" %>
<%@ page import="AnnouncementBeans.Announcement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Home</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
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

        .announcements-container {
            margin-top: 20px;
            width: 80%;
            max-width: 600px;
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .announcement {
            margin-bottom: 15px;
            padding: 10px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .announcement p {
            margin: 0;
        }

        .announcement .branch {
            font-size: 12px;
            color: #777;
        }

        .read-more-container {
            max-height: 200px; /* Set a fixed height for the scrollable container */
            overflow-y: auto; /* Make it scrollable */
            display: none; /* Hide by default */
            margin-top: 10px;
        }

        .read-more-button {
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            margin-top: 10px;
        }

        .read-more-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<%
    HttpSession ss = request.getSession();
    String studentName = (String) ss.getAttribute("stdName");
    Boolean hasMentor = (Boolean) ss.getAttribute("hasMentor");
    String studentBranch = (String) ss.getAttribute("stdBranch");

    //System.out.println("Student Name in JSP: " + studentName); // Debug statement
    //System.out.println("Has Mentor in JSP: " + hasMentor); // Debug statement
    //System.out.println("Student Branch in JSP: " + studentBranch); // Debug statement

    if (studentName == null) {
        response.sendRedirect("Login.jsp");
        return;
    }

    // Fetch announcements
    UserDAO userDAO = new UserDAO();
    List<Announcement> announcements = null;
    try {
        announcements = userDAO.getAnnouncementsForStudent(studentBranch);
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<h2 class="welcome-message">Hi, <%= studentName %>! Welcome to your dashboard.</h2>

<!-- Centered Logout Button -->
<form action="LogoutServlet" method="post">
    <button type="submit" class="logout-button">Logout</button>
</form>
<!-- Chat Icon -->
        <a href="chat.jsp" title="Chat" style="margin-left: 15px; font-size: 24px; color: red;">
            <i class="fas fa-comments"></i>
        </a>

<div class="form-container">
    <% if (hasMentor != null && hasMentor) { %>
        <p>You have already selected a mentor.</p>
    <% } else { %>
        <form action="SelectMentorServlet" method="post">
            <label>Select a Mentor:</label>
            <select name="mentor" required>
                <% 
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

<!-- Announcements Section -->
<div class="announcements-container">
    <h3>Announcements</h3>
    <% if (announcements != null && !announcements.isEmpty()) { %>
        <% 
        int totalAnnouncements = announcements.size();
        int displayLimit = 1; // Show only the latest announcement
        for (int i = 0; i < Math.min(displayLimit, totalAnnouncements); i++) {
            Announcement announcement = announcements.get(i);
        %>
            <div class="announcement">
                <p><%= announcement.getMessage() %></p>
            </div>
        <% } %>

        <% if (totalAnnouncements > displayLimit) { %>
            <div id="readMoreContainer" class="read-more-container">
                <% for (int i = displayLimit; i < totalAnnouncements; i++) {
                    Announcement announcement = announcements.get(i);
                %>
                    <div class="announcement">
                        <p><%= announcement.getMessage() %></p>
                    </div>
                <% } %>
            </div>
            <button class="read-more-button" onclick="toggleReadMore()">Read More</button>
        <% } %>
    <% } else { %>
        <p>No announcements available.</p>
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

<script>
    function toggleReadMore() {
        var readMoreContainer = document.getElementById("readMoreContainer");
        var readMoreButton = document.querySelector(".read-more-button");

        if (readMoreContainer.style.display === "none" || readMoreContainer.style.display === "") {
            readMoreContainer.style.display = "block";
            readMoreButton.textContent = "Show Less";
        } else {
            readMoreContainer.style.display = "none";
            readMoreButton.textContent = "Read More";
        }
    }
</script>
</body>
</html>