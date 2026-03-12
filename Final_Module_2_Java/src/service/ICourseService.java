package service;

import model.Course;
import java.util.List;

public interface ICourseService extends IGenericService<Course, Integer> {
    List<Course> search(String keyword);
    List<Course> findAllSorted(String sortBy, String order);

    List<Course> searchByName(String keyword);
}