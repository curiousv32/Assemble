package com.example.myapplication.util;

import android.content.SharedPreferences;
import android.content.Context;
import com.example.myapplication.model.User;

public class SharedPreferencesManager {
    private static final String SHARED_PREF_NAME = "AssemblePrefs";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public User getUser() {
        int id = sharedPreferences.getInt("id", -1); // -1 if not found
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        return new User(id, username, password);
    }

    public void saveNewUser(String username, String password) {
        int newId = sharedPreferences.getInt("user_count", 0) + 1; // Generate a new user ID
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", newId);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}
