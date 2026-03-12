package dao;

import model.Enrollment;
import java.util.List;

public interface IEnrollmentDAO extends IGenericDAO<Enrollment, Integer> {
    List<Enrollment> findByStudentId(int studentId);
    List<Enrollment> findByCourseId(int courseId);
    void updateStatus(int id, String status);

    List<Enrollment> findByStudentIdSorted(int studentId, String sortBy, String order);
}