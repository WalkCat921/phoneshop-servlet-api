package com.es.phoneshop.dao;

import com.es.phoneshop.model.entity.Entity;

import java.util.List;

public interface DAO<T extends Entity> {
    T get(Long id);

    List<T> findAll();

    void save(T object);

    void delete(Long id);
}
