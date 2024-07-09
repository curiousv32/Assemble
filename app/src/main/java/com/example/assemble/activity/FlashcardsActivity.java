package com.example.assemble.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assemble.R;
import com.example.assemble.service.FlashcardManager;
import com.example.assemble.model.Flashcard;
import com.example.assemble.util.FlashcardAdapter;
import com.example.assemble.database.FlashcardsStub;

import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answerEditText;
    private Button addFlashcardButton;
    private ListView flashcardsListView;
    private FlashcardsStub flashcardStub;
    private FlashcardManager flashcardManager;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        addFlashcardButton = findViewById(R.id.addFlashcardButton);
        flashcardsListView = findViewById(R.id.flashcardsListView);
        flashcardStub = new FlashcardsStub();
        flashcardManager = new FlashcardManager(this);
        username = getIntent().getStringExtra("USER_NAME");
        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = "Question: " + questionEditText.getText().toString();
                String answer = "Answer: " + answerEditText.getText().toString();
                flashcardManager.addFlashcard(username,question,answer);
                Toast.makeText(FlashcardsActivity.this, "Flashcard added", Toast.LENGTH_SHORT).show();
                loadFlashcards();
            }
        });

        loadFlashcards();
    }

    private void loadFlashcards() {
        List<Flashcard> flashcards = flashcardManager.getFlashcards(username);
        FlashcardAdapter adapter = new FlashcardAdapter(this, flashcards);
        flashcardsListView.setAdapter(adapter);
    }
}
