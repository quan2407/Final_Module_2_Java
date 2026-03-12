package dao.impl;

import dao.ICourseDAO;
import model.Course;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements ICourseDAO {

    @Override
    public List<Course> findAll() {
        return findAllSorted("id", "ASC");
    }

    @Override
    public Course findById(Integer id) {
        String sql = "SELECT * FROM course WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Course c) {
        String sql = "INSERT INTO course (name, duration, instructor) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setInt(2, c.getDuration());
            pstmt.setString(3, c.getInstructor());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course c) {
        String sql = "UPDATE course SET name=?, duration=?, instructor=? WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setInt(2, c.getDuration());
            pstmt.setString(3, c.getInstructor());
            pstmt.setInt(4, c.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> search(String keyword) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE name ILIKE ? OR instructor ILIKE ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            pstmt.setString(1, key);
            pstmt.setString(2, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Course> findAllSorted(String sortBy, String order) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY " + sortBy + " " + order;
        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSetToCourse(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Course> searchByName(String name) {
        List<Course> list = new ArrayList<>();
        // Chỉ so sánh với cột name, không đụng tới instructor
        String sql = "SELECT * FROM course WHERE name ILIKE ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCourse(rs));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDuration(rs.getInt("duration"));
        c.setInstructor(rs.getString("instructor"));
        c.setCreatedAt(rs.getDate("created_at"));
        return c;
    }
}