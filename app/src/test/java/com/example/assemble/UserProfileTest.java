package com.example.assemble.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.model.User;
import com.example.assemble.service.UserManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

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
    private UserManager mockUserManager;
    @Mock
    private DatabaseManager mockDatabaseManager;
    @Mock
    private Context mockContext;
    @Mock
    private Toast mockToast;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mock the context and other necessary components
        userProfileActivity = Mockito.spy(new UserProfileActivity());
        Mockito.doReturn(mockNewUsernameEditText).when(userProfileActivity).findViewById(Mockito.anyInt());
        Mockito.doReturn(mockNewPasswordEditText).when(userProfileActivity).findViewById(Mockito.anyInt());
        Mockito.doReturn(mockConfirmPasswordEditText).when(userProfileActivity).findViewById(Mockito.anyInt());
        Mockito.doReturn(mockUpdateButton).when(userProfileActivity).findViewById(R.id.buttonUpdate);
        Mockito.doReturn(mockContext).when(userProfileActivity).getApplicationContext();

        // Set up UserManager mock
        userProfileActivity.userManager = mockUserManager;

        // Stub methods as needed
        when(mockNewUsernameEditText.getText()).thenReturn((Editable) Mockito.mock(CharSequence.class));
        when(mockNewUsernameEditText.getText().toString()).thenReturn("newUsername");
        when(mockNewPasswordEditText.getText()).thenReturn((Editable) Mockito.mock(CharSequence.class));
        when(mockNewPasswordEditText.getText().toString()).thenReturn("newPassword");
        when(mockConfirmPasswordEditText.getText()).thenReturn((Editable) Mockito.mock(CharSequence.class));
        when(mockConfirmPasswordEditText.getText().toString()).thenReturn("newPassword");
    }

    @Test
    public void testUpdateUserProfile_Success() {
        // Mock currentUserId
        String currentUserId = "123e4567-e89b-12d3-a456-556642440000";

        // Set currentUserId in activity
        Intent intent = new Intent();
        intent.putExtra("CURRENT_USER_ID", currentUserId);
        Mockito.doReturn(intent).when(userProfileActivity).getIntent();

        // Call the method under test
        userProfileActivity.updateUserProfile();

        // Verify UserManager interactions
        UUID userId = UUID.fromString(currentUserId);
        verify(mockUserManager).update(userId, new User(userId, "newUsername", "newPassword"));

        // Verify toast message for successful update
        verify(mockToast).makeText(userProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT);

        // Verify navigation
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        Mockito.verify(userProfileActivity).startActivity(intentCaptor.capture());
        Intent capturedIntent = intentCaptor.getValue();
        assertEquals(HomePageActivity.class.getName(), capturedIntent.getComponent().getClassName());
        assertEquals("newUsername", capturedIntent.getStringExtra("USER_NAME"));
    }

    @Test
    public void testUpdateUserProfile_PasswordMismatch() {
        // Mock inputs
        when(mockConfirmPasswordEditText.getText().toString()).thenReturn("differentPassword");

        // Call the method under test
        userProfileActivity.updateUserProfile();

        // Verify toast message for password mismatch
        verify(mockToast).makeText(userProfileActivity, "Passwords do not match", Toast.LENGTH_SHORT);
    }
}
