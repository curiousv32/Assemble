package com.example.assemble;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.contrib.PickerActions.setDate;

import android.content.Intent;
import android.widget.DatePicker;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.assemble.activity.TodoListActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TodoListTest {

    @Rule
    public ActivityTestRule<TodoListActivity> activityTestRule = new ActivityTestRule<>(TodoListActivity.class, true, false);

    @Before
    public void setUp() {
        Intent startIntent = new Intent();
        activityTestRule.launchActivity(startIntent);
    }

    @Test
    public void testAddNewTask() {
        clickAddTaskButton();
        addNewTask();

        onView(withId(R.id.taskTitle)).check(matches(withText("a Task Title")));
        onView(withId(R.id.taskDescription)).check(matches(withText("a new task")));
        onView(withId(R.id.taskDeadline)).check(matches(withText("01/01/2025")));

        deleteTask();
    }

    @Test
    public void testUpdateExistingTask() {
        clickAddTaskButton();
        addNewTask();
        // Click on the first item's edit button
        onView(withId(R.id.editButton)).perform(click());
        // Update the task
        onView(withId(R.id.taskTitle)).perform(typeText("Updated"), closeSoftKeyboard());
        onView(withId(R.id.taskDescription)).perform(typeText("Updated des"), closeSoftKeyboard());
        onView(withId(R.id.taskDeadline)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2026, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.submitButton)).perform(click());

        // Check if the task is updated
        onView(withText("Updated")).check(matches(isDisplayed()));
        onView(withText("Updated des")).check(matches(isDisplayed()));
        onView(withText("01/01/2026")).check(matches(isDisplayed()));

        deleteTask();
    }

    @Test
    public void testDeleteTask() {
        clickAddTaskButton();
        addNewTask();

        onView(withId(R.id.taskTitle)).check(matches(withText("a Task Title")));
        onView(withId(R.id.deleteButton)).perform(click());

        onView(withText("a Task Title")).check(doesNotExist());
    }

    private void clickAddTaskButton() {
        onView(withId(R.id.addTaskButton)).perform(click());
    }

    private void addNewTask() {
        onView(withId(R.id.taskTitle)).perform(typeText("a Task Title"), closeSoftKeyboard());
        onView(withId(R.id.taskDescription)).perform(typeText("a new task"), closeSoftKeyboard());
        onView(withId(R.id.taskDeadline)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2025, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.submitButton)).perform(click());
    }

    private void deleteTask() {
        onView(withId(R.id.deleteButton)).perform(click());
    }
}
