import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import UserRegisteration.UserDAO;
import java.io.IOException;

public class HomeStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String stdUsername = (String) session.getAttribute("username"); // Fetch std_username from session
        
        if (stdUsername == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        UserDAO userDAO = new UserDAO();
        try {
            boolean hasMentor = userDAO.hasMentor(stdUsername); 
            String studentBranch = userDAO.getStudentBranch(stdUsername); // Pass std_username to getStudentBranch
//            System.out.println("Fetched Student Branch: " + studentBranch); // Debug statement

            if (studentBranch == null) {
                System.out.println("Student Branch is null. Check database for username: " + stdUsername);
            }

            request.setAttribute("studentName", stdUsername); // Set std_username as studentName
            request.setAttribute("hasMentor", hasMentor);
            request.setAttribute("studentBranch", studentBranch); // Set studentBranch in request
            request.getRequestDispatcher("HomeStudent.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error fetching student data", e);
        }
    }
}
