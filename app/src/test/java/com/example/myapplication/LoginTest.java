package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.util.SharedPreferencesManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class LoginTest {

    private LoginActivity loginActivity;

    private SharedPreferencesManager sharedPreferencesManager;
    @Mock
    private SharedPreferences mockSharedPreferences;
    @Mock
    private Context mockContext;
    @Mock
    private SharedPreferences.Editor mockEditor;


    @Before
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        when(mockContext.getSharedPreferences(eq("AssemblePrefs"), eq(Context.MODE_PRIVATE))).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.commit()).thenReturn(true);

        sharedPreferencesManager = new SharedPreferencesManager(mockContext);
        loginActivity = new LoginActivity();
        loginActivity.sharedPreferencesManager = this.sharedPreferencesManager;
    }

    @Test
    public void testValidateLogin_CorrectCredentials_ReturnsTrue() {
        String username = "testUser";
        String password = "testPass";

        when(sharedPreferencesManager.getUsername()).thenReturn(username);
        when(sharedPreferencesManager.getPassword()).thenReturn(password);

        assertTrue(loginActivity.validateLogin(username, password));
    }

    @Test
    public void testValidateLogin_IncorrectCredentials_ReturnsFalse() {
        String username = "testUser";
        String password = "wrongPass";

        when(sharedPreferencesManager.getUsername()).thenReturn("testUser");
        when(sharedPreferencesManager.getPassword()).thenReturn("testPass");

        assertFalse(loginActivity.validateLogin(username, password));
    }
    @Test
    public void testValidateLogin_EmptyCredentials_ReturnsFalse() {
        String username = "";
        String password = "";

        when(sharedPreferencesManager.getUsername()).thenReturn("testUser");
        when(sharedPreferencesManager.getPassword()).thenReturn("testPass");

        assertFalse(loginActivity.validateLogin(username, password));
    }
}
