package repository;

import model.Entity;

import java.util.Collection;

public interface Repository<ID,T extends Entity<ID>> {
    void add(T elem);
    void delete(ID id);
    void update (T elem, ID id);
    T findById (ID id);
    Iterable<T> findAll();

}
