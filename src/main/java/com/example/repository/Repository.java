package com.example.repository;

import java.util.Map;
import java.util.Set;

;
public interface Repository<T> {

    void save(T t);

    default T get(Integer id) {
        return getAll().get(id);
    }

    Map<Integer, T> getAll();

    default void delete(Integer id) {
        deleteAll(Set.of(id));
    }

    void deleteAll(Set<Integer> ids);

    int getSize();
}
