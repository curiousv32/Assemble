package com.example.assemble.interfaces;

import java.util.UUID;

public interface IDatabaseManager <T> {
    void add(T item) throws Exception;
    T get(UUID id, Class<T> type);
    void update(UUID id, T item);
    void delete(UUID id);
}