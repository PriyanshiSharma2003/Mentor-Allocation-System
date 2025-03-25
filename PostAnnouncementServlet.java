import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import AnnouncementDAO.PostAnnouncementDAO;
import AnnouncementBeans.Announcement;

@WebServlet("/PostAnnouncementServlet")
public class PostAnnouncementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PostAnnouncementDAO announcementDAO;

    public PostAnnouncementServlet() {
        super();
    }

    public void init() {
        announcementDAO = new PostAnnouncementDAO(); 
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String announcementText = request.getParameter("announcement");
        String branch = request.getParameter("branch");
        System.out.println("Announcement Branch: " + branch); // Debug statement

        Announcement announcement = new Announcement();
        announcement.setMessage(announcementText);
        announcement.setBranch(branch);

        try {
            announcementDAO.saveAnnouncement(announcement);

            HttpSession session = request.getSession();
            session.setAttribute("showPopup", true);

            response.sendRedirect("HomeMentor.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("HomeMentor.jsp?status=error");
        }
    }
}