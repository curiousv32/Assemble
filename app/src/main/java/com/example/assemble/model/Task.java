package com.example.assemble.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private Date deadline;
    private String priority;
    private String status;
    private List<UUID> relatedNotes;

    public Task(UUID id, String title, String description, Date deadline, String priority, String status, List<UUID> relatedNotes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.relatedNotes = relatedNotes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UUID> getRelatedNotes() {
        return relatedNotes;
    }

    public void setRelatedNotes(List<UUID> relatedNotes) {
        this.relatedNotes = relatedNotes;
    }
}
