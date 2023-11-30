package com.example.pepelingbackendv2.entities;

import jakarta.persistence.*;
import org.aspectj.weaver.ast.Not;
import org.hibernate.id.factory.spi.GenerationTypeStrategy;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @URL
    private String pictureUrl;
    @Column
    private String question;
    @Column
    private String answer;
    @Column
    private String type;
    @Column
    private String difficulty;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @ManyToOne
    @JoinColumn(name = "course_id",referencedColumnName = "id",nullable = false)
    private Course course;
    @OneToMany(mappedBy = "task")
    private Set<UserTask> tasks;

    public long getId() {
        return id;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<UserTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<UserTask> tasks) {
        this.tasks = tasks;
    }
}
