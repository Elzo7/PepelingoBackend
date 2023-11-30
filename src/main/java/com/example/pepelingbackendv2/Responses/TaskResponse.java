package com.example.pepelingbackendv2.Responses;

import jakarta.persistence.Column;

import java.util.List;

public class TaskResponse {
    private long id;
    private String pictureUrl;
    private String question;
    private String answer;
    private String type;
    private boolean isCompleted;
    private List<String> possible_answer;

    public List<String> getPossible_answer() {
        return possible_answer;
    }

    public void setPossible_answer(List<String> possible_answer) {
        this.possible_answer = possible_answer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    private String difficulty;

    public long getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
