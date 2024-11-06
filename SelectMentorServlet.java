import java.io.IOException;
import java.sql.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class SelectMentorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        String selectedMentor = request.getParameter("mentor");

        if (username == null || selectedMentor == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MentorAllocationSystem", "root", "tiger");

            // Check if student already has a mentor assigned
            String mentorCheckQuery = "SELECT COUNT(*) FROM MentorMentee WHERE mentee_username = ?";
            PreparedStatement mentorCheckStmt = conn.prepareStatement(mentorCheckQuery);
            mentorCheckStmt.setString(1, username);
            ResultSet mentorCheckRs = mentorCheckStmt.executeQuery();

            if (mentorCheckRs.next() && mentorCheckRs.getInt(1) > 0) {
                session.setAttribute("selectionMessage", "Error: You have already selected a mentor.");
                mentorCheckStmt.close();
                conn.close();
                session.setAttribute("mentorSelected", true);
                response.sendRedirect("HomeStudent.jsp");
                return; // Exit if the student has already selected a mentor
            }
            
            
            // Check current mentee count for selected mentor
            String countQuery = "SELECT COUNT(*) AS menteeCount FROM MentorMentee WHERE mentor_username = ?";
            PreparedStatement countStmt = conn.prepareStatement(countQuery);
            countStmt.setString(1, selectedMentor);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next() && rs.getInt("menteeCount") <20) {
                // Get mentee's name
                String studentNameQuery = "SELECT std_name FROM StudentRegistration sr JOIN StudentLogin sl ON sr.std_id = sl.std_id WHERE sl.std_username = ?";
                PreparedStatement studentNameStmt = conn.prepareStatement(studentNameQuery);
                studentNameStmt.setString(1, username);
                ResultSet studentRs = studentNameStmt.executeQuery();

                if (studentRs.next()) {
                    String menteeName = studentRs.getString("std_name");
                    	
                    String mentorIdQuery = "SELECT mentor_id FROM MentorRegistration WHERE mentor_name = ?";
                    PreparedStatement mentorIdStmt = conn.prepareStatement(mentorIdQuery);
                    mentorIdStmt.setString(1, selectedMentor);
                    ResultSet mentorIdRs = mentorIdStmt.executeQuery();
                    int mentorId = 0;
                    if (mentorIdRs.next()) {
                         mentorId = mentorIdRs.getInt("mentor_id");
                    }
                    
                    String insertQuery = "INSERT INTO MentorMentee (id,mentor_username, mentee_username, mentee_name) VALUES (?,?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, mentorId);
                    insertStmt.setString(2, selectedMentor); // mentor_username
                    insertStmt.setString(3, username);        // mentee_username
                    insertStmt.setString(4, menteeName);      // mentee_name

                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        session.setAttribute("selectionMessage", "Mentor selected successfully");
                        session.setAttribute("hasMentor", true); // Add this line to mark that the mentor is selected
                    } else {
                        session.setAttribute("selectionMessage", "Error: Unable to select mentor.");
                    }
                    insertStmt.close();
                }

                studentRs.close();
                studentNameStmt.close();
            } else {
                session.setAttribute("selectionMessage", "Error: Mentor already has 20 mentees.");
            }

            rs.close();
            countStmt.close();
            conn.close();

        } catch (Exception e) {
            session.setAttribute("selectionMessage", "Error: " + e.getMessage());
        }

        response.sendRedirect("HomeStudent.jsp");
    }

}