package com.example.assemble.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assemble.activity.UserProfileActivity;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.util.SharedPreferencesManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserProfileTest {

    private UserProfileActivity userProfileActivity;

    @Mock
    private EditText mockNewUsernameEditText;
    @Mock
    private EditText mockNewPasswordEditText;
    @Mock
    private EditText mockConfirmPasswordEditText;
    @Mock
    private Button mockUpdateButton;
    @Mock
    private DatabaseManager mockDatabaseManager;
    @Mock
    private SharedPreferencesManager mockSharedPreferencesManager;
    @Mock
    private Context mockContext;
    @Mock
    private Toast mockToast;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mock the context
        when(mockContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mockToast);

        // Create mocks for DatabaseManager and SharedPreferencesManager
        mockDatabaseManager = Mockito.mock(DatabaseManager.class);
        mockSharedPreferencesManager = Mockito.mock(SharedPreferencesManager.class);

        userProfileActivity = Mockito.spy(new UserProfileActivity());
        Mockito.doReturn(mockNewUsernameEditText).when(userProfileActivity).findViewById(Mockito.anyInt());
        Mockito.doReturn(mockNewPasswordEditText).when(userProfileActivity).findViewById(Mockito.anyInt());
        Mockito.doReturn(mockConfirmPasswordEditText).when(userProfileActivity).findViewById(Mockito.anyInt());

        // Mocking direct access to the DatabaseManager and SharedPreferencesManager
        try {
            java.lang.reflect.Field field = UserProfileActivity.class.getDeclaredField("databaseManager");
            field.setAccessible(true);
            field.set(userProfileActivity, mockDatabaseManager);

            field = UserProfileActivity.class.getDeclaredField("sharedPreferencesManager");
            field.setAccessible(true);
            field.set(userProfileActivity, mockSharedPreferencesManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Mockito.doReturn(mockContext).when(userProfileActivity).getApplicationContext();

        // Stub methods as needed
        when(mockNewUsernameEditText.getText()).thenReturn((Editable) Mockito.mock(CharSequence.class));
        when(mockNewPasswordEditText.getText()).thenReturn((Editable) Mockito.mock(CharSequence.class));
        when(mockConfirmPasswordEditText.getText()).thenReturn((Editable) Mockito.mock(CharSequence.class));
    }

    @Test
    public void testUpdateUserProfile_Success() {
        // Mock inputs
        when(mockNewUsernameEditText.getText().toString()).thenReturn("newUsername");
        when(mockNewPasswordEditText.getText().toString()).thenReturn("newPassword");
        when(mockConfirmPasswordEditText.getText().toString()).thenReturn("newPassword");
        when(mockSharedPreferencesManager.getCurrentUser()).thenReturn("currentUsername");

        // Mock the delete and add operations
        Mockito.doNothing().when(mockDatabaseManager).deleteUserProfile(Mockito.anyString());
        Mockito.doNothing().when(mockDatabaseManager).addUserProfile(Mockito.anyString(), Mockito.anyString());

        // Mock the shared preferences save
        Mockito.doNothing().when(mockSharedPreferencesManager).saveCurrentUser(Mockito.anyString());

        // Mock the startActivity method
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        Mockito.doNothing().when(userProfileActivity).startActivity(intentCaptor.capture());
        Mockito.doNothing().when(userProfileActivity).finish();

        // Call the method under test
        userProfileActivity.updateUserProfile();

        // Verify interactions
        verify(mockToast).makeText(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        verify(mockDatabaseManager).deleteUserProfile("currentUsername");
        verify(mockDatabaseManager).addUserProfile("newUsername", "newPassword");
        verify(mockSharedPreferencesManager).saveCurrentUser("newUsername");

        // Verify navigation
        Intent capturedIntent = intentCaptor.getValue();
        assertEquals(HomePageActivity.class.getName(), capturedIntent.getComponent().getClassName());
    }

    @Test
    public void testUpdateUserProfile_PasswordMismatch() {
        // Mock inputs
        when(mockNewUsernameEditText.getText().toString()).thenReturn("newUsername");
        when(mockNewPasswordEditText.getText().toString()).thenReturn("newPassword");
        when(mockConfirmPasswordEditText.getText().toString()).thenReturn("differentPassword");

        // Call the method under test
        userProfileActivity.updateUserProfile();

        // Verify toast message for password mismatch
        verify(mockToast).makeText(userProfileActivity, "Passwords do not match", Toast.LENGTH_SHORT);
    }
}
