package com.example.assemble.model;

import java.util.Date;
import java.util.UUID;

public class Note {

    private UUID noteUUID;
    private String name;
    private Date creationDate;
    private Date lastUpdatedDate;
    private String text;

    public Note(UUID noteUUID, String name) {
        this.noteUUID = noteUUID;
        this.name = name;
        this.creationDate = new Date();
        this.lastUpdatedDate = new Date();
    }

    public void setCreationDate() {
        this.creationDate = new Date();
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    public void setLastUpdatedDate() {
        this.lastUpdatedDate = new Date();
    }

    public void setLastUpdatedDate(Date date) {
        this.lastUpdatedDate = date;
    }

    public void setText(String text) {
        this.text = text;
        setLastUpdatedDate();
    }

    public UUID getID() { return noteUUID; }

    public String getText() { return text == null ? "" : text; }

    public String getName() {
        return this.name;
    }

    public Date getLastUpdatedDate() { return lastUpdatedDate; }

    public Date getCreationDate() { return creationDate; }

    public boolean equals(Note other) {
        return noteUUID.toString().equals(other.noteUUID.toString());
    }
}
