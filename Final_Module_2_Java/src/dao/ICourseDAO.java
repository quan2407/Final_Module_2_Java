package dao;

import model.Course;
import java.util.List;

public interface ICourseDAO extends IGenericDAO<Course, Integer> {
    // Tìm kiếm khóa học theo tên hoặc tên giảng viên
    List<Course> search(String keyword);

    // Sắp xếp khóa học (theo tên/thời lượng/ngày tạo)
    List<Course> findAllSorted(String sortBy, String order);
    List<Course> searchByName(String name);
}