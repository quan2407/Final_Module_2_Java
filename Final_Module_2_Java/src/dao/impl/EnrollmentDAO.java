package dao.impl;

import dao.IEnrollmentDAO;
import model.Enrollment;
import utils.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO implements IEnrollmentDAO {

    @Override
    public List<Enrollment> findAll() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment ORDER BY registered_at DESC";
        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public Enrollment findById(Integer id) {
        String sql = "SELECT * FROM enrollment WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEnrollment(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Enrollment e) {
        String sql = "INSERT INTO enrollment (student_id, course_id, status) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, e.getStudentId());
            pstmt.setInt(2, e.getCourseId());
            pstmt.setString(3, e.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Enrollment e) {
        String sql = "UPDATE enrollment SET student_id = ?, course_id = ?, status = ? WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, e.getStudentId());
            pstmt.setInt(2, e.getCourseId());
            pstmt.setString(3, e.getStatus());
            pstmt.setInt(4, e.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateStatus(int id, String status) {
        String sql = "UPDATE enrollment SET status = ? WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Enrollment> findByStudentIdSorted(int studentId, String sortBy, String order) {
        List<Enrollment> list = new ArrayList<>();

        String sortColumn = sortBy.equalsIgnoreCase("name") ? "c.name" : "e.registered_at";

        String sortOrder = order.equalsIgnoreCase("DESC") ? "DESC" : "ASC";

        String sql = "SELECT e.* FROM enrollment e " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ? " +
                "ORDER BY " + sortColumn + " " + sortOrder;

        return getEnrollments(studentId, list, sql);
    }

    private List<Enrollment> getEnrollments(int studentId, List<Enrollment> list, String sql) {
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM enrollment WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Enrollment> findByStudentId(int sid) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment WHERE student_id = ? ORDER BY registered_at DESC";
        return getEnrollments(sid, list, sql);
    }

    @Override
    public List<Enrollment> findByCourseId(int cid) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment WHERE course_id = ? ORDER BY registered_at DESC";
        return getEnrollments(cid, list, sql);
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setId(rs.getInt("id"));
        e.setStudentId(rs.getInt("student_id"));
        e.setCourseId(rs.getInt("course_id"));
        e.setRegisteredAt(rs.getTimestamp("registered_at"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}