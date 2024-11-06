import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import UserRegisteration.UserDAO;

public class PasswordResetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public PasswordResetServlet() {
        super();
        
    }

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputUserName = request.getParameter("username");
        String newPassword = request.getParameter("newPassword");
        String role = request.getParameter("role");

        UserDAO userDAO = new UserDAO();
        boolean isUpdated = false;

        try {
            isUpdated = userDAO.updatePassword(role, inputUserName, newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "There was an error processing your request. Please try again later.");
        }

        if (isUpdated) {
            request.setAttribute("successMessage", "Password Updated Successfully. You can now <a href='Login.jsp'>login</a> with your new password.");
        } else {
            request.setAttribute("errorMessage", "Username not found in the " + role + " database. Please check and try again.");
        }

        request.getRequestDispatcher("PasswordResetResult.jsp").forward(request, response);
    }

}
