package service.impl;

import dao.ICourseDAO;
import dao.impl.CourseDAO;
import model.Course;
import service.ICourseService;

import java.util.List;

public class CourseService implements ICourseService {
    private final ICourseDAO courseDAO = new CourseDAO();

    @Override
    public List<Course> findAll() {
        return courseDAO.findAll();
    }

    @Override
    public Course findById(Integer id) {
        return courseDAO.findById(id);
    }

    @Override
    public void save(Course c) {
        // Có thể thêm logic: kiểm tra thời lượng > 0
        if (c.getDuration() <= 0) {
            throw new IllegalArgumentException("Thời lượng khóa học phải lớn hơn 0");
        }
        courseDAO.save(c);
    }

    @Override
    public void update(Course c) {
        courseDAO.update(c);
    }

    @Override
    public void delete(Integer id) {
        courseDAO.delete(id);
    }

    @Override
    public List<Course> search(String keyword) {
        return courseDAO.search(keyword);
    }

    @Override
    public List<Course> findAllSorted(String sortBy, String order) {
        return courseDAO.findAllSorted(sortBy, order);
    }

    @Override
    public List<Course> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return courseDAO.findAll();
        }
        return courseDAO.searchByName(keyword);
    }
}