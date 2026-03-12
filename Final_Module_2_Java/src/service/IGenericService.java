package service;

import java.util.List;

public interface IGenericService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    void save(T t);
    void update(T t);
    void delete(ID id);
}