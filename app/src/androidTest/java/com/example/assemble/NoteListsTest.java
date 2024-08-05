package com.example.assemble;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import org.hamcrest.Matcher;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.example.assemble.activity.NoteListsActivity;
import com.example.assemble.database.DatabaseManager;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteListsTest {

    private Context mockContext;
    private NoteManager noteManager;
    private List<Note> notes;

    @Before
    public void set_up() {
        mockContext = ApplicationProvider.getApplicationContext();
        DatabaseManager.setUseSQLDatabase(false);

        noteManager = NoteManager.getInstance(mockContext);
        noteManager.init("");
        noteManager.clearNotes();

        try {

            noteManager.createNote("test1");
            noteManager.createNote("test2");
            noteManager.createNote("test3");

            notes = new ArrayList<>(noteManager.getNotes());
        } catch (InvalidNoteException exception) {
            exception.printStackTrace();
            Log.e("NoteListsTest", "Invalid creation");
        }
        activityTestRule.launchActivity(new Intent());
    }

    @Rule
    public ActivityTestRule<NoteListsActivity> activityTestRule = new ActivityTestRule<>(NoteListsActivity.class, true, false);

    @Test
    public void create_note() {
        onView(withId(R.id.new_note_name)).perform(click()).perform(typeText("test"));
        onView(withId(R.id.note_create)).perform(click());
        onView(withId(R.id.note_contents)).check(matches(isDisplayed()));
    }

    @Test
    public void create_and_update_saved_note() {
        noteManager.clearNotes();

        onView(withId(R.id.new_note_name)).perform(click()).perform(typeText("test"));
        onView(withId(R.id.note_create)).perform(click());
        onView(withId(R.id.note_contents)).check(matches(isDisplayed())).perform(typeText("testing"));
        onView(withId(R.id.notes_go_back)).perform(click());
        onView(withId(R.id.note_create)).check(matches(isDisplayed()));
        onView(withId(R.id.notes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.note_contents)).check(matches(withText("testing")));
    }

    @Test
    public void search_note() {

        notes.get(0).setText("this has test in it");
        notes.get(1).setText("this does not have te st in it");
        notes.get(2).setText("test test test");

        onView(withId(R.id.search_note_text)).perform(click()).perform(typeText("this has"));
        onView(withId(R.id.search_note_button)).perform(click());

        onView(withId(R.id.notes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.note_contents)).check(matches(withText("this has test in it")));
    }

    @Test
    public void delete_note() {
        notes.get(1).setText("confirmation text");

        onView(withId(R.id.notes)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return null;
                    }

                    @Override
                    public String getDescription() {
                        return "Click on delete button";
                    }

                    @Override
                    public void perform(UiController controller, View recyclerView) {
                        View delete = recyclerView.findViewById(R.id.delete_button);
                        delete.performClick();
                    }
                }));

        onView(ViewMatchers.withText("Yes")).inRoot(isDialog()).perform(click());
        onView(withId(R.id.notes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.note_contents)).check(matches(withText("confirmation text")));
    }
}
