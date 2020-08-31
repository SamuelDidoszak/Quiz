package com.gmail.truecitizenquiz;

import java.io.Serializable;

public class Question implements Serializable {
    private boolean answer;
    private String questionValue;
    public Question(String questionValue, boolean answer) {
        this.questionValue = questionValue;
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getQuestionValue() {
        return questionValue;
    }
}
