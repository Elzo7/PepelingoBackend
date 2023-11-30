package com.example.pepelingbackendv2.entities;

import java.util.HashMap;

public class CourseProgress {
    private String courseName;
    private HashMap<String, DifficultyProgress> difficultyProgressList;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public HashMap<String, DifficultyProgress> getDifficultyProgressList() {
        return difficultyProgressList;
    }

    public void setDifficultyProgressList(HashMap<String, DifficultyProgress> difficultyProgressList) {
        this.difficultyProgressList = difficultyProgressList;
    }
}
