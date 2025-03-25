package loginProcessPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import UserRegisteration.UserDAO;

public class LoginProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public LoginProcess() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String role = request.getParameter("role");
        
        UserDAO userDAO = new UserDAO();
        boolean isValidUser = false;

        try {    
            if ("Mentor".equals(role)) {
                isValidUser = userDAO.validatementor(username, password);
                
                if (isValidUser) {
                	HttpSession session = request.getSession();
                	session.setAttribute("username", username); 
                    Integer mentorId = userDAO.getMentorByUsername(username).getId();
                    session.setAttribute("mentorId", mentorId);
                    session.setAttribute("userId", mentorId); 
                    session.setAttribute("role","mentor");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("HomeMentor.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.getWriter().println("Invalid Username or Password for Mentor");
                }
            } 
            else if ("Student".equals(role)) {
                isValidUser = userDAO.validatestudent(username, password);
               
                if (isValidUser) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    
                    String studentName = userDAO.getStudentName(username); 
                    
                    session.setAttribute("stdName", studentName); 
                    
                    String studentBranch = userDAO.getStudentBranch(username);

                    session.setAttribute("stdBranch", studentBranch); 

                    boolean hasMentor = userDAO.hasMentor(username);
                    session.setAttribute("hasMentor", hasMentor);  
                    
                    Integer studentId = userDAO.getStudentId(username);
                    Integer mentorId = userDAO.getMentorIdForStudent(studentId);
                    session.setAttribute("mentorId", mentorId);

                    // Set attributes in the request object
                    request.setAttribute("studentName", studentName);
                    request.setAttribute("hasMentor", hasMentor);
                    request.setAttribute("studentBranch", studentBranch);
                    request.setAttribute("mentorId", mentorId);
                    session.setAttribute("role","student");

                    RequestDispatcher dispatcher = request.getRequestDispatcher("HomeStudent.jsp");
                    dispatcher.forward(request, response);
                }
                else {
                    response.getWriter().println("Invalid Username or Password for Student");
                }
            }
            else {
                response.getWriter().println("Invalid Role Selected");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("An error occurred while validating. Please try again later.");
        }
	}
}