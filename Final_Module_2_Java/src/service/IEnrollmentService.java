package service;

import model.Enrollment;
import java.util.List;

public interface IEnrollmentService extends IGenericService<Enrollment, Integer> {
    List<Enrollment> findByCourseId(int courseId);

    // MỚI: Lấy danh sách đăng ký của riêng 1 học viên
    List<Enrollment> findByStudentId(int studentId);
    List<Enrollment> findByStudentIdSorted(int studentId, String sortBy, String order);
    void updateStatus(int id, String status);
}