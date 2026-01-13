package repo;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface that defines
 * basic CRUD operations for any data type
 * and some additional auxiliary methods.
 */
public interface AbstractRepository<T, ID> {

    /** ------------------- CRUD OPERATIONS ------------------ */
    /**
     * creates a new entity if it doesn't exist
     * C = create
     *
     * @return the stored entity
     */
    T create(T entity);

    /**
     * Retrieves all entities in the repository
     * @return a list of all entities
     * R = read (get all)
     */
    List<T> findAll();

    /**
     * Updates an entity by its id
     * @param id - helps identify the entity, must be kept
     * @param updated - the updated entity
     * @return true if the entity was found and updated / false otherwise
     * U = Update (CRUD)
     */
    boolean update(ID id, T updated);

    /**
     * Deletes an entity by its ID
     * @param id - the id by which the deletion occurs
     * @return true if the entity was found and deleted / false otherwise
     * D = Delete
     */
    boolean delete(ID id);


    /** ------------------- ADDITIONAL METHODS ------------------ */
    /**
     * Finds an entity by its ID
     * @param id - the id by which the search occurs
     * @return the found object if such an object exists / nothing
     */
    Optional<T> findById(ID id);

    /**
     * Checks if an entity with the given ID exists
     * @param id - the id by which the search occurs
     * @return true if it exists / false otherwise
     */
    boolean existsById(ID id);

    /**
     * Counts the number of entities in the repository
     * @return the count number
     */
    long count();

    /**
     * Deletes all entities from the repository
     */
    void clear();

    /**
     * Returns a sublist of entities for pagination
     * @param offset - starting point
     * @param limit - endpoint
     * @return a sublist of all entities between the given offset and limit
     */
    default List<T> findAll(int offset, int limit) {
        List<T> all = findAll();
        int from = Math.max(0, Math.min(offset, all.size()));
        int to = Math.max(from, Math.min(from + limit, all.size()));
        return all.subList(from, to);
    }
}
