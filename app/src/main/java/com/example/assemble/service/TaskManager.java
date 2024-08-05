package com.example.assemble.service;

import static com.example.assemble.database.DatabaseManager.usingSQLDatabase;

import android.content.Context;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidTaskException;
import com.example.assemble.interfaces.ITaskManager;
import com.example.assemble.model.Task;
import com.example.assemble.model.Note;

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
    private final DatabaseManager dbManager;
    private final HashMap<UUID, Task> tasks;
    private final boolean useSQLDatabase;
    private String ownerId;
    private final UUID defaultTaskId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final String DEFAULT_TASK_NAME = "DEFAULT TASK";
    public static final String DEFAULT_TASK_DESCRIPTION = "DEFAULT TASK DESCRIPTION";
    public static final String DEFAULT_TASK_PRIORITY = "High";
    public static final String DEFAULT_TASK_STATUS = "Pending";

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

    public void init(String ownerUUID) {
        tasks.clear();
        List<Task> taskList;
        this.ownerId = ownerUUID;
        addDefaultItem();

        if (useSQLDatabase) {
            taskList = this.getUserTasksFromDB(ownerUUID);
            for (Task task : taskList) {
                tasks.put(task.getId(), task);
            }
        }
    }

    public Task createTask(String title, String description, String deadline, String priority, String status) throws InvalidTaskException {
        Task task = new Task(UUID.randomUUID(), title, description, new java.util.Date(), priority, status, new ArrayList<>());
        if (!useSQLDatabase) {
            tasks.put(task.getId(), task);
            return task;
        } else {
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

        dbManager.runUpdateQuery(
                "INSERT INTO tasks (id, title, description, deadline, priority, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                new java.sql.Timestamp(task.getDeadline().getTime()),
                task.getPriority(),
                task.getStatus(),
                ownerId
        );
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
                            rs.getTimestamp("deadline"), rs.getString("priority"), rs.getString("status"), new ArrayList<>());
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

        dbManager.runUpdateQuery(
                "UPDATE tasks SET title = ?, description = ?, deadline = ?, priority = ?, status = ? WHERE id = ?",
                task.getTitle(),
                task.getDescription(),
                new java.sql.Timestamp(task.getDeadline().getTime()),
                task.getPriority(),
                task.getStatus(),
                taskId.toString()
        );
        tasks.put(taskId, task);
    }

    @Override
    public void delete(UUID taskId) {
        if (!useSQLDatabase) {
            tasks.remove(taskId);
            return;
        }

        dbManager.runUpdateQuery("DELETE FROM tasks WHERE id=?", taskId.toString());
        tasks.remove(taskId);
    }

    @Override
    public void addDefaultItem() {
        try {
            Task defaultTask = new Task(defaultTaskId, DEFAULT_TASK_NAME, DEFAULT_TASK_DESCRIPTION, new java.util.Date(), DEFAULT_TASK_PRIORITY, DEFAULT_TASK_STATUS, new ArrayList<>());
            add(defaultTask);
        } catch (InvalidTaskException e) {
            e.printStackTrace();
        }
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
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, title, description, deadline, priority, status FROM tasks WHERE owner_id = ?")) {
            pstmt.setString(1, ownerUUID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(UUID.fromString(rs.getString("id")),
                            rs.getString("title"), rs.getString("description"),
                            rs.getTimestamp("deadline"), rs.getString("priority"), rs.getString("status"), new ArrayList<>());
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> getAllTasks(String ownerUUID) {
        if(useSQLDatabase){
            List<Task> taskList = getUserTasksFromDB(ownerUUID);
            for (Task task : taskList) {
                tasks.put(task.getId(), task);
            }
        }
        return new ArrayList<>(tasks.values());
    }

    private List<Task> getUserTasksFromDB(String ownerUUID) {
        List<Task> taskList = getUserTasks(ownerUUID);
        for (Task task : taskList) {
            tasks.put(task.getId(), task);
        }
        return taskList;
    }

    public void linkNoteToTask(UUID taskId, UUID noteId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("MERGE INTO tasknotelinks USING (VALUES(?, ?)) AS vals(task_id, note_id)\n" +
                     "    ON tasknotelinks.task_id = vals.task_id AND tasknotelinks.note_id = vals.note_id\n" +
                     "    WHEN MATCHED THEN UPDATE SET note_id = vals.note_id\n" +
                     "    WHEN NOT MATCHED THEN INSERT (task_id, note_id) VALUES (vals.task_id, vals.note_id);\n")) {
            pstmt.setString(1, taskId.toString());
            pstmt.setString(2, noteId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Note> getLinkedNotesForTask(UUID taskId) throws SQLException {
        List<Note> linkedNotes = new ArrayList<>();
        if (!useSQLDatabase) return linkedNotes;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT n.* FROM notes n INNER JOIN tasknotelinks tnl ON n.id = tnl.note_id WHERE tnl.task_id = ?")) {
            pstmt.setString(1, taskId.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    linkedNotes.add(new Note(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("name")
                    ));
                }
            }
        }
        return linkedNotes;
    }

    public void removeLinkedNote(UUID taskId, UUID noteId) {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tasknotelinks WHERE task_id = ? AND note_id = ?")) {
            pstmt.setString(1, taskId.toString());
            pstmt.setString(2, noteId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}
