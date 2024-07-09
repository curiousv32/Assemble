package com.example.assemble;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.service.UserManager;
import com.example.assemble.model.User;

import java.io.File;
import java.util.UUID;

public class UserManagerTestWithStubDB {

    private UserManager userManager;
    private Context mockContext;
    private SharedPreferences mockSharedPreferences;
    private SharedPreferences.Editor mockEditor;

    @Before
    public void setUp() {
        mockContext = org.mockito.Mockito.mock(Context.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.getPath()).thenReturn("mock/path");
        Mockito.when(mockContext.getFilesDir()).thenReturn(mockFile);

        mockSharedPreferences = org.mockito.Mockito.mock(SharedPreferences.class);
        mockEditor = org.mockito.Mockito.mock(SharedPreferences.Editor.class);
        org.mockito.Mockito.when(mockContext.getSharedPreferences(UserManager.getSHARED_PREF_NAME(), Context.MODE_PRIVATE))
                .thenReturn(mockSharedPreferences);
        org.mockito.Mockito.when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        org.mockito.Mockito.when(mockEditor.putString(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString()))
                .thenReturn(mockEditor);

        DatabaseManager.setUseSQLDatabase(false);
        userManager = new UserManager(mockContext);
    }

    @After
    public void cleanUp() {
        userManager.cleanUsers();
    }

    @Test
    public void addUser_WithUniqueUsername_Succeeds() {
        String username = "JISOO";
        String password = "EZpassword";
        UUID userId = UUID.randomUUID();

        User newUser = new User(userId, username, password);

        org.mockito.Mockito.when(mockSharedPreferences.contains(username + "_username")).thenReturn(false);
        assertTrue("User should be added successfully", userManager.addUser(newUser));
    }

    @Test
    public void getUser_ReturnsCorrectUserData() {
        String username = "JISOO";
        String password = "EZpassword";
        UUID userId = UUID.randomUUID();

        org.mockito.Mockito.when(mockSharedPreferences.getString(userId.toString() + "_username", null)).thenReturn(username);
        org.mockito.Mockito.when(mockSharedPreferences.getString(username + "_id", null)).thenReturn(userId.toString());
        org.mockito.Mockito.when(mockSharedPreferences.getString(username + "_username", null)).thenReturn(username);
        org.mockito.Mockito.when(mockSharedPreferences.getString(username + "_password", null)).thenReturn(password);

        User user = userManager.get(userId, User.class);
        assertNotNull("User should not be null", user);
        assertEquals("Username should match", username, user.getUsername());
        assertEquals("Password should match", password, user.getPassword());
    }


    @Test
    public void deleteUser_RemovesUserData() {
        UUID userId = UUID.randomUUID();
        String username = "JISOO";

        org.mockito.Mockito.when(mockSharedPreferences.getString(username + "_id", null)).thenReturn(userId.toString());
        org.mockito.Mockito.when(mockEditor.remove(username + "_id")).thenReturn(mockEditor);
        org.mockito.Mockito.when(mockEditor.remove(username + "_username")).thenReturn(mockEditor);
        org.mockito.Mockito.when(mockEditor.remove(username + "_password")).thenReturn(mockEditor);

        userManager.delete(userId);
        org.mockito.Mockito.verify(mockEditor).apply();
    }
}
