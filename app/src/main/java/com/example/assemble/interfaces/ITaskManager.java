package com.example.assemble.interfaces;

import com.example.assemble.model.Task;

import java.util.UUID;

public interface ITaskManager extends IDatabaseManager<Task> {
    @Override
    void add(Task task) throws Exception;

    @Override
    Task get(UUID taskId, Class<Task> type);

    @Override
    void update(UUID taskId, Task task);

    @Override
    void delete(UUID taskId);
}
