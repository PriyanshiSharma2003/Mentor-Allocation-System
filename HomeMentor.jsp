<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="StudentBeans.Student" %> 
<%@ page import="UserRegisteration.UserDAO" %> 
<%@ page import="MentorBeans.Mentor" %> 
<%
    String username = (String) session.getAttribute("username");
    UserDAO userDAO = new UserDAO();
    Mentor mentor = null;
    List<Student> students = null;

    try {
        mentor = userDAO.getMentorByUsername(username);
        if (mentor != null) {      	
            students = userDAO.getStudentsByMentorId(mentor.getId());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    String mentorName = (mentor != null) ? mentor.getName() : "Mentor";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Mentor Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        header {
            background: #4CAF50;
            color: #fff;
            padding: 10px 20px;
            text-align: center;
        }
        .container {
            width: 90%;
            margin: 20px auto;
        }
        table {
            width: 100%;
            margin: 20px 0;
            border-collapse: collapse;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .remove-button {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 8px 12px;
            cursor: pointer;
        }
        .remove-button:hover {
            opacity: 0.8;
        }
        .announcement-board {
            background: #fff;
            padding: 20px;
            margin-top: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .announcement-board h2 {
            margin-bottom: 20px;
        }
        .announcement-board form {
            margin-bottom: 20px;
        }
        .announcement-board textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .announcement-board select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .announcement-board button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            cursor: pointer;
        }
        .announcement-board button:hover {
            opacity: 0.8;
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
    <header>
        <h1>Welcome, <%= mentorName %>!</h1>
        <form action="LogoutServlet" method="post" style="display:inline;">
            <button type="submit" style="background-color: #4CAF50; color: white; border: none; padding: 10px 15px; cursor: pointer;">
                Logout
            </button>
        </form>
        <!-- Chat Icon -->
        <a href="chat.jsp" title="Chat" style="margin-left: 15px; font-size: 24px; color: white;">
            <i class="fas fa-comments"></i>
        </a>
        
    </header>
    <div class="container">
        <h2 style="text-align: center;">Your Students</h2>
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Branch</th>
                    <th>Semester</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% if (students != null && !students.isEmpty()) {
                    for (Student student : students) { %>
                        <tr>
                            <td><%= student.getName() %></td>
                            <td><%= student.getCourse() %></td>
                            <td><%= student.getSemester() %></td>
                            <td><a href="mailto:<%= student.getEmail() %>"><%= student.getEmail() %></a></td>
                            <td>
                                <form action="RemoveStudentServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="studentId" value="<%= student.getEmail() %>">
                                    <button type="submit" class="remove-button"><i class="fas fa-trash"></i> Remove</button>
                                </form>
                            </td>
                        </tr>
                <% } 
                } else { %>
                    <tr>
                        <td colspan="5" style="text-align: center;">No students assigned.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <div class="announcement-board">
            <h2>Announcement Board</h2>
            <form action="PostAnnouncementServlet" method="post">
                <textarea name="announcement" rows="4" placeholder="Enter your announcement here..." required></textarea>
                <select name="branch" required>
                    <option value="all">All Branches</option>
                    <% 
                    List<String> branches = null;
                    try {
                        branches = userDAO.getBranches();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (branches != null && !branches.isEmpty()) {
                        for (String branch : branches) {
                    %>
                        <option value="<%= branch %>"><%= branch %></option>
                    <% 
                        }
                    }
                    %>
                </select>
                <button type="submit">Post Announcement</button>
            </form>
        </div>
    </div>

    <!-- Popup for successful announcement -->
    <div id="successPopup" class="popup" style="display: none;">
        <img src="images/Tick.jpg" alt="Success" />
        Announcement Posted Successfully!
    </div>

    <script>
        // Check if the popup should be shown
        document.addEventListener("DOMContentLoaded", function() {
            <% 
            Boolean showPopup = (Boolean) session.getAttribute("showPopup");
            if (showPopup != null && showPopup) { 
            %>
                var popup = document.getElementById("successPopup");
                popup.style.display = "block"; // Show popup
                setTimeout(function() {
                    popup.style.display = "none"; // Hide popup after 3 seconds
                }, 3000);
            <%
                session.removeAttribute("showPopup"); // Remove the session attribute
            }
            %>
        });
    </script>
</body>
</html>
