package com.example.assemble.interfaces;

import com.example.assemble.model.User;

import java.util.UUID;

public interface IUserManager extends IDatabaseManager<User> {
    @Override
    void add(User user) throws Exception;

    @Override
    User get(UUID userId, Class<User> type);

    @Override
    void update(UUID userId, User user);

    @Override
    void delete(UUID userId);
}

