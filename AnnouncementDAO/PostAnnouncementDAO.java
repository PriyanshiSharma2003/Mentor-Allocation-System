package AnnouncementDAO;

import AnnouncementBeans.Announcement;
import java.sql.*;

public class PostAnnouncementDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/MentorAllocationSystem";
    private String jdbcUsername = "root";
    private String jdbcPassword = "tiger";

    private static final String INSERT_ANNOUNCEMENT_SQL = "INSERT INTO announcements (message, branch) VALUES (?, ?);";

    public PostAnnouncementDAO() {}

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void saveAnnouncement(Announcement announcement) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ANNOUNCEMENT_SQL)) {
            preparedStatement.setString(1, announcement.getMessage());
            preparedStatement.setString(2, announcement.getBranch());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}