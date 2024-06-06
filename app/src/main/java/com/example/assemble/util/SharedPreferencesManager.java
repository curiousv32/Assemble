package com.example.assemble.util;

import android.content.SharedPreferences;
import android.content.Context;
import com.example.assemble.model.User;

import java.util.UUID;

public class SharedPreferencesManager {
    private static final String SHARED_PREF_NAME = "AssemblePrefs";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public User getUser() {
        String id = sharedPreferences.getString("id", ""); // empty string if not found
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        return new User(id, username, password);
    }

    public void saveNewUser(String username, String password) {
        String newId = UUID.randomUUID().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", newId);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public String getID() {
        return sharedPreferences.getString("id", "");
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", null);
    }
}
