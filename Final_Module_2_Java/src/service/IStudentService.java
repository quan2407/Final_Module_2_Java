package service;

import model.Student;
import java.util.List;

public interface IStudentService extends IGenericService<Student, Integer> {
    void updatePassword(int studentId, String newHashedPassword);

    List<Student> search(String keyword);
    List<Student> findAllSorted(String sortBy, String order);
    Student login(String email, String password);
}