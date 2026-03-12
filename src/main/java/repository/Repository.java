package repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void save(T entity);
    void delete(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
}
