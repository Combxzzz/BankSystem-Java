package repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    void deleteById(ID id);
    List<T> findAll();
    Optional<T> findById(ID id);
}
