package com.example.pepelingbackendv2.entities;

public class TypeProgress {
    private String typeName;
    private Double progress;
    private Double complete;
    private Double total;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }
}
