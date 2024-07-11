package com.example.assemble.service;

import static com.example.assemble.database.DatabaseManager.usingSQLDatabase;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidUserException;
import com.example.assemble.interfaces.IUserManager;
import com.example.assemble.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserManager implements IUserManager{
    private static final String SHARED_PREF_NAME = "AssemblePrefs";
    private SharedPreferences sharedPreferences;
    private DatabaseManager dbManager;
    private boolean useSQLDatabase;

    public UserManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.dbManager = DatabaseManager.getInstance(context);
        this.useSQLDatabase = usingSQLDatabase();
    }

    public boolean addUser(User user) {
        try {
            add(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void add(User user) throws Exception {
        if (doesUsernameExist(user.getUsername())) {
            throw new InvalidUserException("Username already exists");
        }
        if (useSQLDatabase) {
            dbManager.runUpdateQuery("INSERT INTO users (id, username, password) VALUES (?, ?, ?)",
                    user.getId().toString(),
                    user.getUsername(),
                    user.getPassword()
            );
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(user.getId().toString() + "_username", user.getUsername()); // For finding username by id
            editor.putString(user.getUsername() + "_id", user.getId().toString());
            editor.putString(user.getUsername() + "_username", user.getUsername());
            editor.putString(user.getUsername() + "_password", user.getPassword());
            editor.apply();
        }
    }


    @Override
    public User get(UUID userId, Class<User> type) {
        if (useSQLDatabase) {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT id, username, password FROM users WHERE id = ?")) {
                pstmt.setString(1, userId.toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                                UUID.fromString(rs.getString("id")),
                                rs.getString("username"),
                                rs.getString("password")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String username = getUsernameByUUID(userId);
            if (username != null) {
                String password = getPassword(username);
                return new User(userId, username, password);
            }
        }
        return null;
    }

    @Override
    public void delete(UUID userId) {
        if (useSQLDatabase) {
            dbManager.runUpdateQuery("DELETE FROM users WHERE id=?", userId.toString());
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(userId.toString() + "_username");
            editor.remove(userId.toString() + "_password");
            editor.apply();
        }
    }

    @Override
    public void update(UUID userId, User user) {
        if (useSQLDatabase) {
            dbManager.runUpdateQuery(
                    "UPDATE users SET username = ?, password = ? WHERE id = ?",
                    user.getUsername(),
                    user.getPassword(),
                    userId.toString()
            );
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(userId.toString() + "_username", user.getUsername());
            editor.putString(userId.toString() + "_password", user.getPassword());
            editor.apply();
        }
    }

    public boolean validateLogin(String username, String password) {
        if (useSQLDatabase) {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        return storedPassword.equals(password);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            String storedPassword = getPassword(username);
            if(storedPassword == null)
                return false;
            return storedPassword.equals(password);
        }
    }

    private boolean doesUsernameExist(String username) {
        if (useSQLDatabase) {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT username FROM users WHERE username = ?")) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return sharedPreferences.contains(username + "_username");
        }
    }

    public String getID(String username) {
        if (useSQLDatabase) {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("id");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "";
        } else {
            return sharedPreferences.getString(username + "_id", "");
        }
    }

    public String getUsername(String userName) {
        if (useSQLDatabase) {
            return userName;
        }else{
            return sharedPreferences.getString(userName + "_username", "DatabaseManager.STUB_USER");
        }
    }

    private String getUsernameByUUID(UUID userId) {
        if (useSQLDatabase) {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT username FROM users WHERE id = ?")) {
                pstmt.setString(1, userId.toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String username = sharedPreferences.getString(userId.toString() + "_username", null);
            return username != null ? username : DatabaseManager.STUB_USER;
        }
        return "";
    }

    private String getPassword(String userName) {
        if (useSQLDatabase) {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
                pstmt.setString(1, userName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("password");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return sharedPreferences.getString(userName + "_password", null);
        }
        return "";
    }

    public void cleanUsers() {
        if (useSQLDatabase) {
            dbManager.runUpdateQuery("DELETE FROM users");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    public UUID getUUID(String username) {
        String id = getID(username);
        return id.isEmpty() ? null : UUID.fromString(id);
    }

    public static String getSHARED_PREF_NAME(){
        return SHARED_PREF_NAME;
    }
}
