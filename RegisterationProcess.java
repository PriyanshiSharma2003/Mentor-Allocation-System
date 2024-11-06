import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import RandomGeneration.UserUtils;
import UserRegisteration.UserDAO;

public class RegisterationProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public RegisterationProcess() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String generatedUsername = "";
        String generatedPassword = UserUtils.generateRandomUserId(5);
        
        try {
            UserDAO userDAO = new UserDAO();

            // Generate username
            String first4CharsOfName = name.substring(0, Math.min(name.length(), 4));
            String last5DigitsOfPhone = phone.length() >= 5 ? phone.substring(phone.length() - 5) : phone;
            generatedUsername = first4CharsOfName.substring(0, 1).toUpperCase() + first4CharsOfName.substring(1) + last5DigitsOfPhone;
            System.out.println(generatedUsername);
            System.out.println(generatedPassword);
            boolean registrationSuccess = false;

            if ("Mentor".equals(role)) {
                String department = request.getParameter("department");
                registrationSuccess = userDAO.registerMentor(name, email, phone, department, generatedUsername, generatedPassword);
            } else if ("Student".equals(role)) {
                String branch = request.getParameter("branch");
                String semester = request.getParameter("Semester");
                String usn = request.getParameter("USN");
                registrationSuccess = userDAO.registerStudent(name, usn, email, phone, branch, semester, generatedUsername, generatedPassword);
                System.out.println(registrationSuccess);
            }

            if (registrationSuccess) {
                // Redirect back to login page with a success message
                response.sendRedirect("Login.jsp?username=" + generatedUsername + "&password=" + generatedPassword);
            } else {
                response.sendRedirect("Register.jsp?error=Registration Failed. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Register.jsp?error=Something went wrong. Please try again.");
        }
	}

}
