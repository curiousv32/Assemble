package com.example.assemble.service;

import java.util.UUID;

public class SessionManager {
    private static SessionManager instance;
    private UUID currentUserID;
    private String currentUsername;
    private UUID defaultOwnerID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public UUID getCurrentUserID() {
        return currentUserID;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUserID(UUID userID) {
        this.currentUserID = userID;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public void clearSession() {
        currentUserID = null;
        currentUsername = null; // Clear the username as well
    }
    public UUID getDefaultOwnerID() {
        return defaultOwnerID;
    }
}
