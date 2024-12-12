package campus.data.repository;

import java.util.Set;

import campus.data.domain.Entity;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface Repository<E extends Entity> {
    E persist(E entity);
    void delete(E entity);

    Set<E> getAll();
}
