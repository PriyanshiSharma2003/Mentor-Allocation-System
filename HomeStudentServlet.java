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
	    String studentName = (String) session.getAttribute("studentName"); // Retrieve the student name

	    if (studentName == null) {
	        response.sendRedirect("Login.jsp");
	        return;
	    }

	    UserDAO userDAO = new UserDAO();
	    try {
	        // No need to fetch the name again; it should be available in the session
	        boolean hasMentor = userDAO.hasMentor(studentName); // Check if the student has a mentor

	        request.setAttribute("studentName", studentName);
	        request.setAttribute("hasMentor", hasMentor);
	        request.getRequestDispatcher("HomeStudent.jsp").forward(request, response);

	    } catch (Exception e) {
	        throw new ServletException("Error fetching student data", e);
	    }
	    
	    
	}
}



