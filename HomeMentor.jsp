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
        table {
            width: 80%;
            margin: 20px auto;
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
    </header>
    <main>
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
                            <td><%= student.getBranch() %></td>
                            <td><%= student.getSemester() %></td>
                            <td><%= student.getEmail() %></td>
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
    </main>
</body>
</html> 
