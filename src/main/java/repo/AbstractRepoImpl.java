package repo;

import com.fasterxml.jackson.core.type.TypeReference;
import util.JsonIO;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * File-backed implementation of AbstractRepository that persists all entities
 * of type T in a single JSON array file.
 *
 * Usage: new AbstractRepoImpl<>(path, idGetter, new TypeReference<List<MyEntity>>() {});
 */
public class AbstractRepoImpl<T, ID> implements AbstractRepository<T, ID> {

    private final Path filePath;
    private final Function<T, ID> idGetter;
    private final TypeReference<List<T>> listType;

    // in-memory
    private final List<T> items;
    private final Map<ID, Integer> indexById = new HashMap<>();

    public AbstractRepoImpl(Path filePath,
                            Function<T, ID> idGetter,
                            TypeReference<List<T>> listType) {
        if (filePath == null) throw new IllegalArgumentException("filePath cannot be null");
        if (idGetter == null) throw new IllegalArgumentException("idGetter cannot be null");
        if (listType == null) throw new IllegalArgumentException("listType cannot be null");

        this.filePath = filePath;
        this.idGetter = idGetter;
        this.listType = listType;

        // load (or create empty array file)
        this.items = JsonIO.readOrCreate(filePath, "[]", listType);
        rebuildIndex();
    }

    private void rebuildIndex() {
        indexById.clear();
        for (int i = 0; i < items.size(); i++) {
            T e = items.get(i);
            if (e == null) continue;
            ID id = idGetter.apply(e);
            if (id == null) {
                throw new IllegalStateException("Found entity with null ID at index " + i);
            }
            if (indexById.containsKey(id)) {
                throw new IllegalStateException("Duplicate ID in storage: " + id);
            }
            indexById.put(id, i);
        }
    }

    private void flushToDisk() {
        JsonIO.writePretty(filePath, items);
    }

    // ------------------- CRUD -------------------

    @Override
    public synchronized T create(T entity) {
        if (entity == null) throw new IllegalArgumentException("entity cannot be null");
        ID id = idGetter.apply(entity);
        if (id == null) throw new IllegalArgumentException("entity ID cannot be null");
        if (indexById.containsKey(id)) {
            throw new IllegalArgumentException("Entity with ID '" + id + "' already exists");
        }

        items.add(entity);
        indexById.put(id, items.size() - 1);
        flushToDisk();
        return entity;
    }

    @Override
    public synchronized List<T> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public synchronized boolean update(ID id, T updated) {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        if (updated == null) throw new IllegalArgumentException("updated entity must not be null");

        ID updatedId = idGetter.apply(updated);
        if (updatedId == null) throw new IllegalArgumentException("updated entity ID must not be null");
        if (!id.equals(updatedId)) {
            throw new IllegalArgumentException("Updated entity ID must match the path ID");
        }

        Integer idx = indexById.get(id);
        if (idx == null) return false;

        items.set(idx, updated);
        flushToDisk();
        return true;
    }

    @Override
    public synchronized boolean delete(ID id) {
        if (id == null) throw new IllegalArgumentException("id must not be null");

        Integer idxObj = indexById.get(id);
        if (idxObj == null) return false;

        int idx = idxObj;
        int lastIndex = items.size() - 1;

        // remove mapping for deleted id
        indexById.remove(id);

        if (idx == lastIndex) {
            // last element -> simple remove
            items.remove(lastIndex);
        } else {
            // swap-remove
            T moved = items.get(lastIndex);
            items.set(idx, moved);
            items.remove(lastIndex);

            ID movedId = idGetter.apply(moved);
            if (movedId == null) {
                throw new IllegalStateException("Moved entity has null ID");
            }
            indexById.put(movedId, idx);
        }

        flushToDisk();
        return true;
    }

    // ------------------- Extras -------------------

    @Override
    public synchronized Optional<T> findById(ID id) {
        if (id == null) return Optional.empty();
        Integer idx = indexById.get(id);
        return idx == null ? Optional.empty() : Optional.of(items.get(idx));
    }

    @Override
    public synchronized boolean existsById(ID id) {
        if (id == null) return false;
        return indexById.containsKey(id);
    }

    @Override
    public synchronized long count() {
        return items.size();
    }

    @Override
    public synchronized void clear() {
        items.clear();
        indexById.clear();
        flushToDisk();
    }
}
