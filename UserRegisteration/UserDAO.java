package UserRegisteration;
import java.sql.*;
import java.util.*;
import ChatBeans.ChatMessage;
import AnnouncementBeans.Announcement;
import StudentBeans.Student;
import MentorBeans.Mentor;

public class UserDAO {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/MentorAllocationSystem", "root", "tiger");
    }

    public boolean registerMentor(String name, String email, String phone, String department, String username, String password) throws Exception {
        String query = "INSERT INTO mentorregistration (mentor_name, mentor_email, mentor_phone, mentor_department) VALUES (?, ?, ?, ?)";
        String loginQuery = "INSERT INTO mentorlogin (mentor_username, mentor_password) VALUES (?, ?)";
        
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             PreparedStatement loginStmt = con.prepareStatement(loginQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, department);
            pstmt.executeUpdate();
            
            loginStmt.setString(1, username);
            loginStmt.setString(2, password);
            loginStmt.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerStudent(String name, String usn, String email, String phone, String branch, String semester, String username, String password) throws Exception {
        String insertStudentQuery = "INSERT INTO StudentRegistration (std_name, std_usn, std_email, std_phone, std_course, std_sem) VALUES (?, ?, ?, ?, ?, ?)";
        String insertLoginQuery = "INSERT INTO StudentLogin (std_id, std_username, std_password) VALUES (?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertStudentQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement loginStmt = con.prepareStatement(insertLoginQuery)) {

            // Insert into StudentRegistration
            pstmt.setString(1, name);
            pstmt.setString(2, usn);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, branch);
            pstmt.setInt(6, Integer.parseInt(semester)); // Convert semester to int
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated std_id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int stdId = generatedKeys.getInt(1);

                    // Insert into StudentLogin
                    loginStmt.setInt(1, stdId);
                    loginStmt.setString(2, username);
                    loginStmt.setString(3, password);
                    loginStmt.executeUpdate();

                    return true;
                } else {
                    throw new Exception("Failed to retrieve generated std_id.");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("catch executed");
            throw new Exception("Error registering student: " + e.getMessage());
        }
    }
    
    public boolean assignMentorToStudent(String mentorName, String studentUsername) throws Exception {
        String query = "INSERT INTO MentorMentee (mentor_id, mentee_id) " +
                       "SELECT mr.mentor_id, sl.std_id " +
                       "FROM MentorRegistration mr, StudentLogin sl " +
                       "WHERE mr.mentor_name = ? AND sl.std_username = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, mentorName);
            pstmt.setString(2, studentUsername);
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error assigning mentor to student: " + e.getMessage());
        }
    }
    
    public boolean validatementor(String username, String password) throws Exception {
        String query = "SELECT * FROM mentorlogin WHERE mentor_username = ? AND mentor_password = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validatestudent(String username, String password) throws Exception {
        String query = "SELECT * FROM studentlogin WHERE std_username = ? AND std_password = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePassword(String role, String username, String newPassword) throws Exception {
        String checkQuery = ""; // Query to check if the username exists
        String updateQuery = ""; // Query to update the password

        if ("Mentor".equals(role)) {
            checkQuery = "SELECT mentor_id FROM MentorLogin WHERE mentor_username = ?";
            updateQuery = "UPDATE MentorLogin SET mentor_password = ? WHERE mentor_username = ?";
        } else if ("Student".equals(role)) {
            checkQuery = "SELECT std_id FROM StudentLogin WHERE std_username = ?";
            updateQuery = "UPDATE StudentLogin SET std_password = ? WHERE std_username = ?";
        }

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(checkQuery)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Username exists, proceed to update the password
                pstmt.close(); 

             
                try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setString(2, username);
                    int rowsUpdated = updateStmt.executeUpdate();
                    return rowsUpdated > 0;
                }
            } else {
                
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Student> getStudentsByMentorId(Integer mentorId) throws Exception {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.std_id, s.std_name, s.std_course, s.std_sem, s.std_email " +
                       "FROM StudentRegistration s " +
                       "JOIN MentorMentee m ON s.std_id = m.mentee_id " + 
                       "WHERE m.mentor_id = ?"; 

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, mentorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("std_id"), 
                    rs.getString("std_name"),
                    rs.getString("std_course"),
                    rs.getString("std_sem"),
                    rs.getString("std_email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching students for mentor: " + e.getMessage());
        }
        return students;
    }

    
    public Mentor getMentorByUsername(String username) throws Exception {
        Mentor mentor = null;
        String query = "SELECT mr.mentor_id, mr.mentor_name " +
                       "FROM MentorLogin ml " +
                       "JOIN MentorRegistration mr ON ml.mentor_id = mr.mentor_id " +
                       "WHERE ml.mentor_username = ?"; // This is correct
        
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                mentor = new Mentor();
                mentor.setId(rs.getInt("mentor_id")); 
                mentor.setName(rs.getString("mentor_name")); // Change to fetch mentor_name
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching mentor details.");
        }
        return mentor;
    }
    
    public void removeStudentFromMentor(Integer mentorId, String studentEmail) throws Exception {
        String query = "DELETE FROM MentorMentee " +
                       "WHERE id = ? AND mentee_name = (SELECT std_name FROM StudentRegistration WHERE std_email = ?)";
        
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, mentorId);
            pstmt.setString(2, studentEmail); 
            System.out.println(query);
            System.out.println(mentorId);
            System.out.println(studentEmail);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Failed to remove student from mentor.");
        }
    }

    
    public String getStudentName(String username) throws Exception {
        String studentName = null;
        String query = "SELECT std_name FROM StudentRegistration sr JOIN StudentLogin sl ON sr.std_id = sl.std_id WHERE sl.std_username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                studentName = rs.getString("std_name");
            }
        }
        return studentName;
    }
    
    public Integer getStudentId(String username) throws Exception {
        Integer studentId = null;
        String query = "SELECT sr.std_id FROM StudentRegistration sr JOIN StudentLogin sl ON sr.std_id = sl.std_id WHERE sl.std_username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                studentId = rs.getInt("std_id");
            }
        }
        return studentId;
    }
    
    public boolean hasMentor(String username) throws Exception {
        String query = "SELECT COUNT(*) FROM MentorMentee WHERE mentee_username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    
    public List<String> getAvailableMentors() throws Exception {
        List<String> availableMentors = new ArrayList<>();
        String query = "SELECT mentor_name FROM MentorRegistration WHERE mentor_id NOT IN (SELECT mentor_id FROM MentorMentee GROUP BY mentor_id HAVING COUNT(*) >= 20)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                availableMentors.add(rs.getString("mentor_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error retrieving available mentors: " + e.getMessage());
        }

        return availableMentors;
    }
    
    public List<String> getBranches() throws Exception {
        List<String> branches = new ArrayList<>();
        String query = "SELECT DISTINCT std_course FROM StudentRegistration"; 

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                branches.add(rs.getString("std_course"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return branches;
    }
    
    public List<Announcement> getAnnouncementsForStudent(String branch) throws Exception {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT * FROM announcements WHERE branch = ? OR branch = 'all' ORDER BY created_at DESC";
        
        System.out.println("Fetching announcements for branch: " + branch); // Debug statement
        
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, branch);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(resultSet.getInt("id"));
                announcement.setMessage(resultSet.getString("message"));
                announcement.setBranch(resultSet.getString("branch"));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        return announcements;
    }
    
    public String getStudentBranch(String stdUsername) throws Exception {
        String studentBranch = null;
        String query = "SELECT std_course FROM StudentRegistration sr " +
                       "JOIN StudentLogin sl ON sr.std_id = sl.std_id " +
                       "WHERE sl.std_username = ?"; // Fetch branch using std_username

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, stdUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                studentBranch = rs.getString("std_course");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return studentBranch;
    }

 // Fetch mentor ID for a student
    public Integer getMentorIdForStudent(int studentId) throws Exception {
        String query = "SELECT mentor_id FROM MentorMentee WHERE mentee_id = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("mentor_id"); // Fetch mentor_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching mentor ID for student: " + e.getMessage());
        }
        return null; // Return null if no mentor is assigned
    }
    
    public String getStudentUsernameById(int studentId) throws Exception {
        String query = "SELECT std_username FROM StudentLogin WHERE std_id = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("std_username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching student username by ID: " + e.getMessage());
        }
        return null; // Return null if no username is found
    }

    
    public List<ChatMessage> getMessages(int senderId, int receiverId) throws Exception {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM Messages " +
                       "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                       "ORDER BY timestamp ASC";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.setInt(3, receiverId);
            pstmt.setInt(4, senderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(new ChatMessage(
                    rs.getInt("message_id"),
                    rs.getInt("sender_id"),
                    rs.getInt("receiver_id"),
                    rs.getString("message"),
                    rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error fetching messages.");
        }
        return messages;
    }
    
}