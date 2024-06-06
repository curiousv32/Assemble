package com.example.assemble.util;

import android.content.SharedPreferences;
import android.content.Context;

import com.example.assemble.database.DatabaseManager;

import java.util.UUID;

public class SharedPreferencesManager {
    private static final String SHARED_PREF_NAME = "AssemblePrefs";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
        return sharedPreferences.getString("username", DatabaseManager.STUB_USER);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", DatabaseManager.STUB_PASSWORD);
    }
}
