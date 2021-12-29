package com.example.service;

import com.example.common.Identifiable;

public interface Service<T extends Identifiable> {

    void save(T t);

    T get(Integer id);

    void delete(Integer id);
}
