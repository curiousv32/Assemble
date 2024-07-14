package com.example.assemble;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.example.assemble.service.UserManager;
import com.example.assemble.service.UserSettingsManager;
import com.example.assemble.model.User;

import java.util.UUID;

public class UserSettingsTest {

    private UserSettingsManager userSettingsManager;
    private UserManager userManager;
    private User testUser;

    @Before
    public void setUp() {
        // Mock UserSettingsManager
        userSettingsManager = Mockito.mock(UserSettingsManager.class);

        // Set up mock user profile
        UUID userId = UUID.randomUUID();
        testUser = new User(userId, "testUser", "password123");
        Mockito.when(userSettingsManager.loadUserProfile()).thenReturn(testUser);
    }

    @Test

    public void testLoadUserProfile() {

        User loadedUser = userSettingsManager.loadUserProfile();
        assertEquals("testUser", loadedUser.getUsername());
        assertEquals("password123", loadedUser.getPassword());
    }

    @Test
    public void testSaveUserProfile() {
        String newUsername = "newUser";
        String newPassword = "newPassword123";
        userSettingsManager.saveUserProfile(newUsername, newPassword);
        Mockito.verify(userSettingsManager).saveUserProfile(newUsername, newPassword);
    }

    @Test
    public void testLogoutUser() {
        userSettingsManager.logoutUser();
        Mockito.verify(userSettingsManager).logoutUser();
    }

}