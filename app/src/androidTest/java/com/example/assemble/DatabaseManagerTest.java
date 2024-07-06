package com.example.assemble;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.assemble.database.DatabaseManager;
import com.example.assemble.model.Note;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        dbManager = DatabaseManager.getInstance(context);
    }

    @Test
    public void testConnection() {
        try (Connection conn = dbManager.getConnection()) {
            Assert.assertNotNull("DB connection should not be null", conn);
        } catch (SQLException e) {
            Assert.fail("Should not have this SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserNotes() {
        String testUserUUID = UUID.randomUUID().toString();
        List<Note> notes = dbManager.getUserNotes(testUserUUID);

        Assert.assertFalse("Stub notes list should not be empty", notes.isEmpty());
        Assert.assertEquals("Should return stub note", DatabaseManager.STUB_NOTE_NAME, notes.get(0).getName());
    }
}
