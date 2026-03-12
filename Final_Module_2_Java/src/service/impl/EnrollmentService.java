package service.impl;

import dao.IEnrollmentDAO;
import dao.impl.EnrollmentDAO;
import model.Enrollment;
import service.IEnrollmentService;

import java.util.List;

public class EnrollmentService implements IEnrollmentService {
    private final IEnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    @Override
    public List<Enrollment> findAll() {
        return enrollmentDAO.findAll();
    }

    @Override
    public Enrollment findById(Integer id) {
        return enrollmentDAO.findById(id);
    }

    @Override
    public void save(Enrollment e) {
        // Business Logic: Có thể kiểm tra xem học viên đã đăng ký khóa này chưa trước khi lưu
        enrollmentDAO.save(e);
    }

    @Override
    public void update(Enrollment e) {
        enrollmentDAO.update(e);
    }

    @Override
    public void delete(Integer id) {
        enrollmentDAO.delete(id);
    }

    @Override
    public List<Enrollment> findByCourseId(int courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    @Override
    public void updateStatus(int id, String status) {
        enrollmentDAO.updateStatus(id, status);
    }
    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    @Override
    public List<Enrollment> findByStudentIdSorted(int studentId, String sortBy, String order) {
        return enrollmentDAO.findByStudentIdSorted(studentId, sortBy, order);
    }
}