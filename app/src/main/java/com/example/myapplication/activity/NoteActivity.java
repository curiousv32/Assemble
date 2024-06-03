package com.example.myapplication.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_page);

        Button goBackButton = findViewById(R.id.notes_go_back);
        TextView textContents = findViewById(R.id.note_contents);

        textContents.setText("".toCharArray(), 0, 0);
        textContents.requestFocus();

        goBackButton.setOnClickListener(v -> {
            Log.d("NoteActivity", "contents: " + textContents.getText().toString());
            //Intent intent = new Intent(null, HomePageActivity.class);
            //startActivity(intent);

            Toast.makeText(this, "Returned to Home Page", Toast.LENGTH_SHORT).show();
        });
    }
}
