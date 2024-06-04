package com.example.myapplication.model;

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
    }

    public void setCreationDate() {
        this.creationDate = new Date();
    }

    public void setLastUpdatedDate() {
        this.lastUpdatedDate = new Date();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() { return text; }

    public String getName() {
        return this.name;
    }

    public boolean equals(Note other) {
        return noteUUID.toString().equals(other.noteUUID.toString());
    }
}
