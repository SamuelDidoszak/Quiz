package com.gmail.truecitizenquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class newQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private LinkedList<Question> newQuestions = new LinkedList<Question>();
    private EditText questionValue;
    private CheckBox isTrue;
    private Button goBack, addQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        questionValue = findViewById(R.id.questionValueTextView);
        isTrue = findViewById(R.id.isTrueCheckBox);
        goBack = findViewById(R.id.goBackButton);
        addQuestion = findViewById(R.id.addQuestionButton);

        addQuestion.setOnClickListener(this);
        goBack.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.addQuestionButton:
                addQuestion();
                break;
            case R.id.goBackButton:
                fuckGoBack();
        }

    }

    public void addQuestion()
    {
        String questionTextValue = questionValue.getText().toString();
        if(questionTextValue == "")
            return;
        newQuestions.add(new Question(questionTextValue, isTrue.isChecked()));
        questionValue.setText("");
        if(isTrue.isChecked())
            isTrue.setChecked(false);
    }

    public void fuckGoBack()    //kek
    {
        Intent returnIntent = new Intent(newQuestionActivity.this, MainActivity.class);
        returnIntent.putExtra("Questions", newQuestions);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
