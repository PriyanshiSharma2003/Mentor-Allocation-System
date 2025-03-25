import java.io.IOException;
import java.sql.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
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

        System.out.println("SelectMentorServlet: doPost called");
        System.out.println("Username: " + username);
        System.out.println("Selected Mentor: " + selectedMentor);

        if (username == null || selectedMentor == null) {
            System.out.println("Username or selected mentor is null");
            RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MentorAllocationSystem", "root", "tiger");
            System.out.println("Database connection established: " + conn);

            // Check if student already has a mentor assigned
            String mentorCheckQuery = "SELECT COUNT(*) FROM MentorMentee WHERE mentee_username = ?";
            PreparedStatement mentorCheckStmt = conn.prepareStatement(mentorCheckQuery);
            mentorCheckStmt.setString(1, username);
            ResultSet mentorCheckRs = mentorCheckStmt.executeQuery();

            if (mentorCheckRs.next() && mentorCheckRs.getInt(1) > 0) {
                System.out.println("Student already has a mentor");
                session.setAttribute("selectionMessage", "Error: You have already selected a mentor.");
                mentorCheckStmt.close();
                conn.close();
                session.setAttribute("mentorSelected", true);
                response.sendRedirect("HomeStudent.jsp");
                return;
            }

            // Check current mentee count for selected mentor
            String countQuery = "SELECT COUNT(*) AS menteeCount FROM MentorMentee WHERE mentor_username = ?";
            PreparedStatement countStmt = conn.prepareStatement(countQuery);
            countStmt.setString(1, selectedMentor);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next() && rs.getInt("menteeCount") < 20) {
                // Get mentee's name and ID
                String studentQuery = "SELECT sr.std_id, sr.std_name FROM StudentRegistration sr JOIN StudentLogin sl ON sr.std_id = sl.std_id WHERE sl.std_username = ?";
                PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
                studentStmt.setString(1, username);
                ResultSet studentRs = studentStmt.executeQuery();

                if (studentRs.next()) {
                    int menteeId = studentRs.getInt("std_id"); // Retrieve mentee_id
                    String menteeName = studentRs.getString("std_name");

                    // Get mentor ID
                    String mentorIdQuery = "SELECT mentor_id FROM MentorRegistration WHERE mentor_name = ?";
                    PreparedStatement mentorIdStmt = conn.prepareStatement(mentorIdQuery);
                    mentorIdStmt.setString(1, selectedMentor);
                    ResultSet mentorIdRs = mentorIdStmt.executeQuery();
                    int mentorId = 0;
                    if (mentorIdRs.next()) {
                        mentorId = mentorIdRs.getInt("mentor_id");
                    }
                    System.out.println("Mentor ID: " + mentorId); // Debug statement

                    // Insert mentor-mentee relationship
                    String insertQuery = "INSERT INTO MentorMentee (mentor_id, mentee_id, mentor_username, mentee_username, mentee_name) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, mentorId);
                    insertStmt.setInt(2, menteeId); // Include mentee_id
                    insertStmt.setString(3, selectedMentor);
                    insertStmt.setString(4, username);
                    insertStmt.setString(5, menteeName);

                    try {
                        int rowsInserted = insertStmt.executeUpdate();
                        System.out.println("Rows Inserted: " + rowsInserted); // Debug statement
                        if (rowsInserted > 0) {
                            session.setAttribute("selectionMessage", "Mentor selected successfully");
                            session.setAttribute("hasMentor", true);
                        } else {
                            session.setAttribute("selectionMessage", "Error: Unable to select mentor.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        session.setAttribute("selectionMessage", "Error: Failed to insert mentor-mentee relationship.");
                    }

                    insertStmt.close();
                }

                studentRs.close();
                studentStmt.close();
            } else {
                session.setAttribute("selectionMessage", "Error: Mentor already has 20 mentees.");
            }

            rs.close();
            countStmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("selectionMessage", "Error: " + e.getMessage());
        }

        response.sendRedirect("HomeStudent.jsp");
    }
}
