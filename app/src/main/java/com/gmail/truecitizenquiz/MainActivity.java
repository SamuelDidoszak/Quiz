package com.gmail.truecitizenquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button falseButton, trueButton;
    private TextView questionTextView, progressTextView;
    private boolean wait = false;

    private int currentQuestionID;
    private int correct=0;
    private int initialQuestionDBLength;

    private ArrayList<Question> questionDB = new ArrayList<Question>();

    final int REQUEST_VALUE = 2137;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addQuestionDBValues();

        falseButton = findViewById(R.id.falseButton);
        trueButton = findViewById(R.id.trueButton);
        questionTextView = findViewById(R.id.questionTextView);
        progressTextView = findViewById(R.id.progressTextView);

        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);

        showQuestion();
    }

    private void addQuestionDBValues()
    {
        questionDB.add(new Question(getResources().getString(R.string.question1), true));
        questionDB.add(new Question(getResources().getString(R.string.question2), true));
        questionDB.add(new Question(getResources().getString(R.string.question3), false));
        questionDB.add(new Question(getResources().getString(R.string.question4), false));
        questionDB.add(new Question(getResources().getString(R.string.question5), true));
        initialQuestionDBLength = questionDB.size();
    };

    private BundleCompat newQuestions;

    public void onClick(View v) {
        if(wait)
            return;
        if(questionDB.size() == 0) {
            buttonsAfterFinish(v);
            return;
        }
        if(v.getId() == R.id.trueButton)
            checkCorrectness(true);
        else
            checkCorrectness(false);
    }

    public void showQuestion()
    {
        if(questionDB.size() != 0)
        {
            currentQuestionID = (int)(Math.random() * questionDB.size());
            questionTextView.setText(questionDB.get(currentQuestionID).getQuestionValue());
        }
        else
            showResults();
    }

    public boolean checkCorrectness(boolean answer){
        if(questionDB.get(currentQuestionID).isAnswer() == answer)
        {
            correct++;
            questionTextView.setText(R.string.correctAnswer);
                questionDB.remove(currentQuestionID);           //removes current question
            questionTextView.setTextSize(32);
            questionTextView.setTextColor(getResources().getColor(R.color.trueColor));
            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    questionTextView.setTextSize(24);
                    questionTextView.setTextColor(getResources().getColor(android.R.color.white));
                    showQuestion();
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() { wait = false; }
                    }, 300); }
            }, 750);
            showProgress();
            return true;
        }
        questionTextView.setText(R.string.wrongAnswer);
            questionDB.remove(currentQuestionID);           //removes current question
        questionTextView.setTextSize(32);
        questionTextView.setTextColor(getResources().getColor(R.color.falseColor));
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                questionTextView.setTextSize(24);
                questionTextView.setTextColor(getResources().getColor(android.R.color.white));
                showQuestion();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() { wait = false; }
                }, 300); }
        }, 750);
        showProgress();
        return false;
    }

    public void showProgress()
    {
        progressTextView.setText(initialQuestionDBLength - questionDB.size() + "/" + initialQuestionDBLength);
    }

    public void showResults()
    {
        float resultPercentage = ((float) correct / initialQuestionDBLength) * 100;
        questionTextView.setTextSize(32);
        if(resultPercentage < 50)
            questionTextView.setTextColor(getResources().getColor(R.color.scoreLow));
        else if(resultPercentage < 75)
            questionTextView.setTextColor(getResources().getColor(R.color.scoreMedium));
        else
            questionTextView.setTextColor(getResources().getColor(R.color.scoreHigh));
        questionTextView.setText("you have finished the test with the final score of " + (int) resultPercentage + "%");
        changeButtonValues();
    }

    public void changeButtonValues()
    {
        falseButton.setText(getResources().getText(R.string.closeApp));
        trueButton.setText(getResources().getText(R.string.retake));
    }

    public void buttonsAfterFinish(View v)
    {
        switch (v.getId())
        {
            case R.id.falseButton:
                System.exit(0);
                break;
            case R.id.trueButton:
                addQuestionDBValues();
                correct = 0;
                questionTextView.setTextSize(24);
                questionTextView.setTextColor(getResources().getColor(android.R.color.white));
                falseButton.setText(getResources().getText(R.string.falseText));
                trueButton.setText(getResources().getText(R.string.trueText));
                showQuestion();
                showProgress();
                break;
        }
    }

    public void goToNewQuestionActivity(View v)
    {
        Intent nextActivity = new Intent(MainActivity.this, newQuestionActivity.class);
        startActivityForResult(nextActivity, REQUEST_VALUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Question> newQuestionDB;
        questionTextView.setTextSize(24);
        questionTextView.setTextColor(getResources().getColor(android.R.color.white));
        if(requestCode == REQUEST_VALUE)
        {
            if(resultCode == RESULT_OK)
            {
                newQuestionDB = (ArrayList<Question>) data.getSerializableExtra("Questions");
            }
            else
                return;
        }
        else
            return;
        questionDB.addAll(newQuestionDB);
        initialQuestionDBLength = questionDB.size();
        if(trueButton.getText().toString() == getResources().getText(R.string.trueText))
        {
            falseButton.setText(getResources().getText(R.string.falseText));
            trueButton.setText(getResources().getText(R.string.trueText));
        }
        showQuestion();
        showProgress();
    }
}
