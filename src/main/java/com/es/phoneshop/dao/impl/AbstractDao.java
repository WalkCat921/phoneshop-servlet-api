package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.DAO;
import com.es.phoneshop.model.entity.Entity;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractDao<T extends Entity> implements DAO<T> {

    protected static final Object LOCK = new Object();
    private List<T> list;

    protected AbstractDao() {
        list = new ArrayList<>();
    }

    @Override
    public T get(Long id) {
        synchronized (LOCK) {
            return list.stream()
                    .filter(object -> id.equals(object.getId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("Not found!"));
        }
    }

    @Override
    public List<T> findAll() {
        synchronized (LOCK) {
            return list;
        }
    }

    @Override
    public void save(@NonNull T object) {
        synchronized (LOCK) {
            if (object.getId() != null) {
                try {
                    T objectFromList = get(object.getId());
                    list.set(list.indexOf(objectFromList), object);
                } catch (NoSuchElementException ex) {
                    list.add(object);
                }
            } else {
                object.setId(getLastId() + 1);
                list.add(object);
            }
        }
    }

    @Override
    public void delete(Long id) {
        synchronized (LOCK) {
            if (id != null) {
                list.remove(get(id));
            }
        }
    }

    private Long getLastId() {
        return list.stream()
                .mapToLong(Entity::getId)
                .max()
                .orElse(0L);
    }
}