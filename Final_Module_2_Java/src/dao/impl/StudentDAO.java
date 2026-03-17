package dao.impl;

import dao.IStudentDAO;
import model.Student;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements IStudentDAO {

    @Override
    public List<Student> findAll() {
        return findAllSorted("id", "ASC");
    }

    @Override
    public Student findById(Integer id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Student s) {
        String sql = "INSERT INTO student (name, dob, email, sex, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getName());
            pstmt.setDate(2, s.getDob());
            pstmt.setString(3, s.getEmail());
            pstmt.setBoolean(4, s.isSex()); // setBoolean cho giới tính
            pstmt.setString(5, s.getPhone());
            pstmt.setString(6, s.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student s) {
        String sql = "UPDATE student SET name=?, dob=?, email=?, sex=?, phone=? WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getName());
            pstmt.setDate(2, s.getDob());
            pstmt.setString(3, s.getEmail());
            pstmt.setBoolean(4, s.isSex());
            pstmt.setString(5, s.getPhone());
            pstmt.setInt(6, s.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> search(String keyword) {
        List<Student> list = new ArrayList<>();
        // CAST(id AS TEXT) để có thể dùng LIKE với kiểu số trong Postgres
        String sql = "SELECT * FROM student WHERE name ILIKE ? OR email ILIKE ? OR CAST(id AS TEXT) LIKE ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchKey = "%" + keyword + "%";
            pstmt.setString(1, searchKey);
            pstmt.setString(2, searchKey);
            pstmt.setString(3, searchKey);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Student> findAllSorted(String sortBy, String order) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student ORDER BY " + sortBy + " " + order;
        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSetToStudent(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updatePassword(int studentId, String newHashedPassword) {
        String sql = "UPDATE student SET password = ? WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newHashedPassword);
            pstmt.setInt(2, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setDob(rs.getDate("dob"));
        s.setEmail(rs.getString("email"));
        s.setSex(rs.getBoolean("sex")); // Đọc BOOLEAN từ DB
        s.setPhone(rs.getString("phone"));
        s.setPassword(rs.getString("password"));
        s.setCreatedAt(rs.getDate("created_at"));
        return s;
    }
    @Override
    public Student findByEmail(String email) {
        String sql = "SELECT * FROM student WHERE email = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public boolean isEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM student WHERE email = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}