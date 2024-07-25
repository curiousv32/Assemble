package com.example.assemble;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.assemble.activity.NoteListsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteListsTest {

    @Rule
    public ActivityTestRule<NoteListsActivity> activityTestRule = new ActivityTestRule<>(NoteListsActivity.class);

    @Test
    public void search_note() {
        onView(withId(R.id.search_note_text)).perform(click()).perform(typeText("test"));
        onView(withId(R.id.search_note_button)).perform(click());

        //onData(anything()).inAdapterView(withId(R.id.notes)).atPosition(0).check(!ViewMatchers.isDisplayed())
    }
}
