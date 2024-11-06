import UserRegisteration.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RemoveStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        Integer mentorId = (Integer) request.getSession().getAttribute("mentorId"); // Assuming mentorId is stored in session

        UserDAO userDAO = new UserDAO();
        try {
            userDAO.removeStudentFromMentor(mentorId, studentId);
            response.sendRedirect("HomeMentor.jsp"); // Redirect back to the mentor's home page
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "There was an error removing the student.");
            request.getRequestDispatcher("HomeMentor.jsp").forward(request, response);
        }
    }
}
