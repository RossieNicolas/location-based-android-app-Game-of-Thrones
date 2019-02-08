package com.example.testapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cz.mendelu.busItWeek.library.SimplePuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;

public class SimplePuzzleActivity extends AppCompatActivity {
    private TextView questionTextView;
    private EditText answerEditText;
    private Button doneButton;

    private StoryLine storyLine;
    private Task currentTask;
    private SimplePuzzle puzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_puzzle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //new stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        questionTextView = findViewById(R.id.question);
        answerEditText = findViewById(R.id.answer);
        doneButton = findViewById(R.id.angry_btn);

        //questionTextView.setText("Text of the question");
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        storyLine = StoryLine.open(this,BusITWeekDatabaseHelper.class);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentTask = storyLine.currentTask();
        if (currentTask != null){
            puzzle = (SimplePuzzle) currentTask.getPuzzle();
            questionTextView.setText(puzzle.getQuestion());
        }
    }
    private void checkAnswer(){
        String userAnswer = answerEditText.getText().toString();
        String correctAnswer = puzzle.getAnswer();
        if (userAnswer.equalsIgnoreCase(correctAnswer)){
            storyLine.currentTask().finish(true);
            Intent show = new Intent(this, Question1RightActivity.class);
            startActivity(show);
            //finish();
        }
        else {
            storyLine.currentTask().finish(true);
            Intent show = new Intent(this, Question1WrongActivity.class);
            startActivity(show);
            //finish();
        }
    }
}
