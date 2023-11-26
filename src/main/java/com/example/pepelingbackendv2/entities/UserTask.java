package com.example.pepelingbackendv2.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_tasks")
public class UserTask {
    @EmbeddedId
    private UserTaskId id;
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    public UserTask(User user, Task task, boolean completed) {
        this.user = user;
        this.task = task;
        this.completed = completed;
        this.id = new UserTaskId(user.getId(), task.getId());
    }

    @MapsId("taskId")
    @ManyToOne
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    private Task task;

    @Column
    private boolean completed = false;

    public UserTask() {

    }

    @Embeddable
    public static class UserTaskId implements Serializable {
        private Long user_id;
        private Long task_id;

        // default constructor, getters, and equals/hashCode methods

        public UserTaskId() {
        }

        public UserTaskId(Long user, Long task) {
            this.user_id = user;
            this.task_id = task;
        }

        public Long getUser() {
            return user_id;
        }

        public void setUser(Long user) {
            this.user_id = user;
        }

        public Long getTask() {
            return task_id;
        }

        public void setTask(Long task) {
            this.task_id = task;
        }

        // implement equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserTaskId that = (UserTaskId) o;
            return user_id.equals(that.user_id) && task_id.equals(that.task_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user_id, task_id);
        }
    }
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
