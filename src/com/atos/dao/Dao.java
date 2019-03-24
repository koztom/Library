package com.atos.dao;

import java.util.Map;

public interface Dao<T> {

    T get(Integer id) throws Exception;

    Map<Integer, T> getAll();

    void save(T t);

    void delete(Integer id);
}