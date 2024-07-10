package com.example.assemble.service;

import static com.example.assemble.database.DatabaseManager.usingSQLDatabase;

import android.content.Context;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidTaskException;
import com.example.assemble.interfaces.ITaskManager;
import com.example.assemble.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TaskManager implements ITaskManager {

    private static TaskManager REFERENCE;
    private DatabaseManager dbManager;
    private HashMap<UUID, Task> tasks;
    private boolean useSQLDatabase;

    public static final String STUB_TASK_NAME = "stub";

    private TaskManager(Context context) {
        this.tasks = new HashMap<>();
        this.dbManager = DatabaseManager.getInstance(context);
        this.useSQLDatabase = usingSQLDatabase();
    }

    public static synchronized TaskManager getInstance(Context context) {
        if (REFERENCE == null) {
            REFERENCE = new TaskManager(context);
        }
        return REFERENCE;
    }

    public HashMap<UUID, Task> init(String ownerUUID) {
        tasks.clear();
        List<Task> taskList;
        if (!useSQLDatabase) {
            taskList = stubTask;
        } else {
            taskList = this.getUserTasks(ownerUUID);
        }
        for (Task task : taskList) {
            tasks.put(task.getId(), task);
        }
        return tasks;
    }

    public Task addTask(String title, String description, String deadline, String priority, String status) throws InvalidTaskException {
        if (!useSQLDatabase) {
            Task task = new Task(UUID.randomUUID(), title, description, new java.util.Date(), priority, status);
            tasks.put(task.getId(), task);
            return task;
        } else {
            Task task = new Task(UUID.randomUUID(), title, description, new java.util.Date(), priority, status);
            try {
                add(task);
            } catch (InvalidTaskException e) {
                e.printStackTrace();
            }
            return task;
        }
    }

    @Override
    public void add(Task task) throws InvalidTaskException {
        if(!useSQLDatabase) {
            tasks.put(task.getId(), task);
            return;
        }
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tasks (id, title, description, deadline, priority, status) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, task.getId().toString());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setTimestamp(4, new java.sql.Timestamp(task.getDeadline().getTime()));
            pstmt.setString(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public Task get(UUID taskId, Class<Task> type) {
        if (!useSQLDatabase) {
            return tasks.get(taskId);
        }
        Task task = null;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, title, description, deadline, priority, status FROM tasks WHERE id = ?")) {
            pstmt.setString(1, taskId.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    task = new Task(UUID.fromString(rs.getString("id")),
                            rs.getString("title"), rs.getString("description"),
                            rs.getTimestamp("deadline"), rs.getString("priority"), rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public void update(UUID taskId, Task task) {
        if (!useSQLDatabase) {
            tasks.put(taskId, task);
            return;
        }
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE tasks SET title = ?, description = ?, deadline = ?, priority = ?, status = ? WHERE id = ?")) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setTimestamp(3, new java.sql.Timestamp(task.getDeadline().getTime()));
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());
            pstmt.setString(6, taskId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tasks.put(taskId, task);
    }

    @Override
    public void delete(UUID taskId) {
        if (!useSQLDatabase) {
            tasks.remove(taskId);
            return;
        }
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            pstmt.setString(1, taskId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tasks.remove(taskId);
    }

    public boolean contains(String taskTitle) {
        return tasks.values().stream().anyMatch(task -> task.getTitle().equals(taskTitle));
    }

    public int getTasksSize() {
        return tasks.size();
    }

    public void clearTasks() {
        tasks.clear();
    }

    public List<Task> getUserTasks(String ownerUUID) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, title, description, deadline, priority, status FROM tasks")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(UUID.fromString(rs.getString("id")),
                            rs.getString("title"), rs.getString("description"),
                            rs.getTimestamp("deadline"), rs.getString("priority"), rs.getString("status"));
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private List<Task> stubTask = new ArrayList<Task>() {{
        add(new Task(UUID.randomUUID(), "Stub Task", "This is a stub task description", new java.util.Date(), "High", "Pending"));
    }};

    public List<Task> getAllTasks(String ownerUUID) {
        tasks.clear();
        List<Task> taskList = updateTaskListFromDB(ownerUUID);
        for (Task task : taskList) {
            tasks.put(task.getId(), task);
        }
        return taskList;
    }

    private List<Task> updateTaskListFromDB(String ownerUUID) {
        tasks.clear();
        List<Task> taskList = getUserTasks(ownerUUID);
        for (Task task : taskList) {
            tasks.put(task.getId(), task);
        }
        return taskList;
    }
}
