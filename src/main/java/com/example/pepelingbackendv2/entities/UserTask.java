package com.example.pepelingbackendv2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_tasks")
public class UserTask {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    private Task task;

    @Column
    private boolean completed = false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
