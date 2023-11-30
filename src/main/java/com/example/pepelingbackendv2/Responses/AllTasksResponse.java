package com.example.pepelingbackendv2.Responses;

import com.example.pepelingbackendv2.entities.Task;

import java.util.List;

public class AllTasksResponse {
    private List<TaskResponse> tasksResponse;
    private String message;

    public List<TaskResponse> getTasksResponse() {
        return tasksResponse;
    }

    public void setTasksResponse(List<TaskResponse> tasksResponse) {
        this.tasksResponse = tasksResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
