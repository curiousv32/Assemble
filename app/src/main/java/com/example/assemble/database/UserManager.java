package com.example.assemble.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.assemble.model.User;

public class UserManager {
    private static final String SHARED_PREF_NAME = "AssemblePrefs";
    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean saveNewUser(User user) {
        if (doesUsernameExist(user.getUsername())) {
            return false;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(user.getUsername() + "_id", user.getId().toString());
        editor.putString(user.getUsername() + "_username", user.getUsername());
        editor.putString(user.getUsername() + "_password", user.getPassword());
        editor.apply();
        return true;
    }

    public boolean doesUsernameExist(String username) {
        return sharedPreferences.contains(username + "_username");
    }

    public User getUser(String username) {
        if (doesUsernameExist(username)) {
            String userId = sharedPreferences.getString(username + "_id", "");
            String password = sharedPreferences.getString(username + "_password", "");
            return new User(username, password);
        }
        return null;
    }

    public String getID(String username) {
        return sharedPreferences.getString(username + "_id", "");
    }

    public String getUsername(String username) {
        return sharedPreferences.getString(username + "_username", DatabaseManager.STUB_USER);
    }

    public String getPassword(String username) {
        return sharedPreferences.getString(username + "_password", DatabaseManager.STUB_PASSWORD);
    }
}
