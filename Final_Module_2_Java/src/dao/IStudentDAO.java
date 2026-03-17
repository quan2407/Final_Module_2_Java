package dao;

import model.Student;
import java.util.List;

public interface IStudentDAO extends IGenericDAO<Student, Integer> {
    List<Student> search(String keyword);
    List<Student> findAllSorted(String sortBy, String order);

    public void updatePassword(int studentId, String newHashedPassword);
    Student findByEmail(String email);

    boolean isEmailExist(String email);
}