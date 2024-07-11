package com.example.assemble;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import android.content.Context;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.model.User;
import com.example.assemble.service.UserManager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UserManagerTestWithSQLDB {

    private UserManager userManager;
    private Context mockContext;

    @Before
    public void setUp() {
        mockContext = Mockito.mock(Context.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.getPath()).thenReturn("mock/path");
        Mockito.when(mockContext.getFilesDir()).thenReturn(mockFile);

        DatabaseManager.setUseSQLDatabase(true);
        initializeDatabase();
        userManager = new UserManager(mockContext);
        // Clean users after each test
        userManager.cleanUsers();
    }

    private void initializeDatabase() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this.mockContext);
        try (Connection conn = dbManager.getConnection()) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL)";
            try (PreparedStatement stmt = conn.prepareStatement(createUserTable)) {
                stmt.executeUpdate();
            }

            String createNoteTable = "CREATE TABLE IF NOT EXISTS notes (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "content VARCHAR(65535))";
            try (PreparedStatement stmt = conn.prepareStatement(createNoteTable)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addUser_succeeds() {
        UUID userId = UUID.randomUUID();
        User newUser = new User(userId, "JISOO", "EZpassword");
        try {
            boolean result = userManager.addUser(newUser);
            assertTrue("User should be added successfully", result);
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void getUser_succeeds() {
        UUID userId = UUID.randomUUID();
        User newUser = new User(userId, "JISOO", "EZpassword");
        try {
            userManager.add(newUser);
            User retrievedUser = userManager.get(userId, User.class);
            assertNotNull("User should be retrieved", retrievedUser);
            assertEquals("Retrieved user should match added", newUser.getUsername(), retrievedUser.getUsername());
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void updateUser_succeeds() {
        UUID userId = UUID.randomUUID();
        User newUser = new User(userId, "JISOO", "EZpassword");
        try {
            userManager.add(newUser);
            newUser.setPassword("newpassword456");
            userManager.update(userId, newUser);
            User updatedUser = userManager.get(userId, User.class);
            assertEquals("Updated password should match", "newpassword456", updatedUser.getPassword());
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void deleteUser_succeeds() {
        UUID userId = UUID.randomUUID();
        User newUser = new User(userId, "JISOO", "EZpassword");
        try {
            userManager.add(newUser);
            userManager.delete(userId);
            User deletedUser = userManager.get(userId, User.class);
            assertNull("User should be deleted", deletedUser);
        } catch (Exception e) {
            fail("Should not throw an exception: " + e.getMessage());
        }
    }
}
