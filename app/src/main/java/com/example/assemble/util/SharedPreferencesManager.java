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

    public boolean saveNewUser(String username, String password) {
        if (doesUsernameExist(username)) {
            return false;
        }

        String newId = UUID.randomUUID().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //adding suffix to create unique key value pairs
        editor.putString(username + "_id", newId);
        editor.putString(username + "_username", username);
        editor.putString(username + "_password", password);
        editor.apply();
        return true;
    }

    public String getID() {
        return sharedPreferences.getString("id", "");
    }

    public String getUsername(String username) {
        return sharedPreferences.getString(username + "_username", DatabaseManager.STUB_USER);
    }

    public String getPassword(String username) {
        return sharedPreferences.getString(username + "_password", DatabaseManager.STUB_PASSWORD);
    }

    public boolean doesUsernameExist(String username) {
        return sharedPreferences.contains(username + "_username");
    }
}
