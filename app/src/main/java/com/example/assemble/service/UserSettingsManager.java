package com.example.assemble.service;

import android.content.Context;
import android.util.Log;

import com.example.assemble.model.User;
import com.example.assemble.service.UserManager;

import java.util.UUID;

public class UserSettingsManager {
    private static final String TAG = "UserSettingsManager";
    private UserManager userManager;
    private UUID currentUserId;

    public UserSettingsManager(Context context, UUID userId) {
        this.userManager = new UserManager(context);
        this.currentUserId = userId;
    }

    public User loadUserProfile() {
        Log.d(TAG, "Loading user profile for ID: " + currentUserId);
        User userProfile = userManager.get(currentUserId, User.class);
        if (userProfile == null) {
            Log.e(TAG, "User profile not found");
            return null;
        }
        return userProfile;
    }

    public boolean saveUserProfile(String username, String password) {
        Log.d(TAG, "Saving user profile: " + username);
        User userProfile = new User(currentUserId, username, password);
        userManager.update(currentUserId, userProfile);
        return true; // Return true on success
    }

    public boolean updateUserProfile(String username, String password) {
        Log.d(TAG, "Updating user profile for ID: " + currentUserId);
        User userProfile = new User(currentUserId, username, password);
        userManager.update(currentUserId, userProfile);
        return true; // Return true on success
    }

    public void logoutUser() {
        Log.d(TAG, "Logging out user: " + currentUserId);
        userManager.logout(); // Clear the current user ID
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
