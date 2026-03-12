package model;

import java.sql.Timestamp;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private Timestamp registeredAt;
    private String status;

    public Enrollment() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public Timestamp getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Timestamp registeredAt) { this.registeredAt = registeredAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}