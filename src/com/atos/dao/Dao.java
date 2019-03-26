package com.atos.dao;

import com.atos.exception.BookNotBorrowed;
import com.atos.exception.BookNotFoundException;

import java.util.Map;

public interface Dao<T> {

    T get(Integer id) throws Exception;

    Map<Integer, T> getAll();

    void save(T t);

    void delete(Integer id) throws BookNotFoundException, BookNotBorrowed;
}