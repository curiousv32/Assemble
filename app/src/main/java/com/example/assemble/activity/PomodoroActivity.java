package com.example.assemble.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.assemble.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class PomodoroActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView timerTextView;
    private Button startButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 0; // Time left in milliseconds
    private static final long MAX_TIME_IN_MILLIS = 60 * 60 * 1000; // Maximum time of 60 minutes in milliseconds

    // Preset intervals in minutes
    private final int[] minuteValues = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);

        timerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomTimePickerDialog();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    pauseTimer();
                } else {
                    if (timeLeftInMillis == 0) {
                        Toast.makeText(PomodoroActivity.this, "Please set a time.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateTimerText();
    }

    private void showCustomTimePickerDialog() {
        final Dialog dialog = new Dialog(PomodoroActivity.this);
        dialog.setContentView(R.layout.dialog_time_picker);

        final NumberPicker minutePicker = dialog.findViewById(R.id.minutePicker);
        Button okButton = dialog.findViewById(R.id.okButton);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(minuteValues.length - 1);
        minutePicker.setDisplayedValues(new String[]{"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"});
        minutePicker.setWrapSelectorWheel(true);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedValueIndex = minutePicker.getValue();
                int selectedMinutes = minuteValues[selectedValueIndex];
                timeLeftInMillis = selectedMinutes * 60 * 1000; // Convert minutes to milliseconds
                updateTimerText();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                startButton.setText("Start");
                Toast.makeText(PomodoroActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                playNotificationSound();
            }
        }.start();

        startButton.setText("Pause");
        isRunning = true;
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning = false;
        startButton.setText("Start");
    }

    private void resetTimer() {
        if (isRunning) {
            countDownTimer.cancel();
        }
        isRunning = false;
        timeLeftInMillis = 0;
        updateTimerText();
        startButton.setText("Start");
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    private void playNotificationSound() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ringtone.play();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomePageActivity.class));
        } else if (id == R.id.nav_todo) {
            startActivity(new Intent(this, TodoListActivity.class));
        } else if (id == R.id.nav_notes) {
            startActivity(new Intent(this, NoteListsActivity.class));
        } else if (id == R.id.nav_flashcards) {
            startActivity(new Intent(this, FlashcardsActivity.class));
        } else if (id == R.id.nav_timer) {
            // already on timer
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
