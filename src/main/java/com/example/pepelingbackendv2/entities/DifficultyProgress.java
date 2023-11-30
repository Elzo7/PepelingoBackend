package com.example.pepelingbackendv2.entities;

import java.util.HashMap;
import java.util.List;

public class DifficultyProgress {
    private String difficultyName;
    private HashMap<String,TypeProgress> typeProgressMap;
    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    public List<TypeProgress> getTypeProgressList() {
        return typeProgressList;
    }

    public void setTypeProgressList(List<TypeProgress> typeProgressList) {
        this.typeProgressList = typeProgressList;
    }

    private List<TypeProgress> typeProgressList;
}
