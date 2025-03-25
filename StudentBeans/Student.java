package StudentBeans;

public class Student {
    private int id;
    private String name;
    private String course;
    private String semester;
    private String email;

    // Constructor
    public Student(int id, String name, String course, String semester, String email) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.email = email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}