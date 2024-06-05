package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.model.User;
import com.example.myapplication.util.SharedPreferencesManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SharedPreferencesManagerTest {

    @Mock
    private Context mockContext;
    @Mock
    private SharedPreferences mockSharedPreferences;
    @Mock
    private SharedPreferences.Editor mockEditor;

    private SharedPreferencesManager sharedPreferencesManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockContext.getSharedPreferences(eq("AssemblePrefs"), eq(Context.MODE_PRIVATE))).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.commit()).thenReturn(true);

        sharedPreferencesManager = new SharedPreferencesManager(mockContext);
    }

    @Test
    public void testGetUser() {
        String testId = "000";
        String testUsername = "111";
        String testPassword = "222";

        when(mockSharedPreferences.getString(eq("id"), eq(""))).thenReturn(testId);
        when(mockSharedPreferences.getString(eq("username"), isNull())).thenReturn(testUsername);
        when(mockSharedPreferences.getString(eq("password"), isNull())).thenReturn(testPassword);

        User user = sharedPreferencesManager.getUser();

        assertNotNull(user);
        assertEquals(testId, user.getId());
        assertEquals(testUsername, user.getUsername());
        assertEquals(testPassword, user.getPassword());
    }

    @Test
    public void testSaveNewUser() {
        String testUsername = "111";
        String testPassword = "222";

        sharedPreferencesManager.saveNewUser(testUsername, testPassword);

        verify(mockEditor).putString(eq("username"), eq(testUsername));
        verify(mockEditor).putString(eq("password"), eq(testPassword));
        verify(mockEditor).apply();
    }

    @Test
    public void testGetID() {
        String testId = "000";
        when(mockSharedPreferences.getString(eq("id"), eq(""))).thenReturn(testId);

        String id = sharedPreferencesManager.getID();
        assertEquals(testId, id);
    }

    @Test
    public void testGetUsername() {
        String testUsername = "111";
        when(mockSharedPreferences.getString(eq("username"), isNull())).thenReturn(testUsername);

        String username = sharedPreferencesManager.getUsername();
        assertEquals(testUsername, username);
    }

    @Test
    public void testGetPassword() {
        String testPassword = "222";
        when(mockSharedPreferences.getString(eq("password"), isNull())).thenReturn(testPassword);

        String password = sharedPreferencesManager.getPassword();
        assertEquals(testPassword, password);
    }
}
