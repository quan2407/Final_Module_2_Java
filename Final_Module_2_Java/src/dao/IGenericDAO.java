package dao;

import java.util.List;

public interface IGenericDAO<T, ID> {
    List<T> findAll();
    T findById(ID id);
    void save(T t);
    void update(T t);
    void delete(ID id);
}