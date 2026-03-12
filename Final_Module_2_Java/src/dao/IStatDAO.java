package dao;

import java.util.List;
import java.util.Map;

public interface IStatDAO {
    // 1. Thống kê số lượng học viên theo từng khóa học
    Map<String, Integer> getStudentCountByCourse();

    // 2. Thống kê danh sách học viên theo một khóa học cụ thể
    // Giúp biết được khóa học đó gồm những ai
    List<model.Student> getStudentsByCourseId(int courseId);

    // 3. Thống kê trạng thái đăng ký (Bao nhiêu người WAITING, CONFIRMED...)
    Map<String, Integer> getEnrollmentStatusStats();

    // 4. Thống kê các khóa học phổ biến nhất (Top courses)
    Map<String, Integer> getTopCourses(int limit);
}