package dao.impl;

import dao.IStatDAO;
import model.Student;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatDAO implements IStatDAO {

    @Override
    public Map<String, Integer> getStudentCountByCourse() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT c.name, COUNT(e.id) AS total " +
                "FROM course c LEFT JOIN enrollment e ON c.id = e.course_id " +
                "GROUP BY c.name";
        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("name"), rs.getInt("total"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.* FROM student s " +
                "JOIN enrollment e ON s.id = e.student_id " +
                "WHERE e.course_id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    s.setEmail(rs.getString("email"));
                    s.setSex(rs.getBoolean("sex"));
                    students.add(s);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return students;
    }

    @Override
    public Map<String, Integer> getEnrollmentStatusStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT status, COUNT(*) FROM enrollment GROUP BY status";
        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    @Override
    public Map<String, Integer> getTopCourses(int limit) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT c.name, COUNT(e.id) as total " +
                "FROM course c JOIN enrollment e ON c.id = e.course_id " +
                "GROUP BY c.name ORDER BY total DESC LIMIT ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stats.put(rs.getString("name"), rs.getInt("total"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }
}