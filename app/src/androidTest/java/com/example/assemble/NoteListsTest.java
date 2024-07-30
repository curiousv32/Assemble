package com.example.assemble;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.matches;

import android.content.Context;
import org.mockito.Mockito;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.example.assemble.activity.NoteListsActivity;
import com.example.assemble.exceptions.InvalidNoteException;
import com.example.assemble.model.Note;
import com.example.assemble.service.NoteManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteListsTest {

    private Context mockContext;
    private NoteManager noteManager;

    @Rule
    public ActivityTestRule<NoteListsActivity> activityTestRule = new ActivityTestRule<>(NoteListsActivity.class);

    @Before
    public void set_up() {
        mockContext = Mockito.mock(Context.class);
        noteManager = NoteManager.getInstance(mockContext);
        noteManager.init("");
        noteManager.clearNotes();
    }
    @Test
    public void search_note() {

        try {
            Note note1 = noteManager.createNote("test1");
            Note note2 = noteManager.createNote("test2");
            Note note3 = noteManager.createNote("test3");

            note1.setText("this has test in it");
            note2.setText("this does not have te st in it");
            note3.setText("test test test");

            onView(withId(R.id.search_note_text)).perform(click()).perform(typeText("test"));
            onView(withId(R.id.search_note_button)).perform(click());

            onView(withId(R.id.notes)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("this has test in it"))));

        } catch (InvalidNoteException exception) {
            exception.printStackTrace();

        }
    }
}
