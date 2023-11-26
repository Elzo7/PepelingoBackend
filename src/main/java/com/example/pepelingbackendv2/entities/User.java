package com.example.pepelingbackendv2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Entity
@Table(name  ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotBlank
    private String login;
    @Column
    @NotBlank
    private String password;
    @Column
    @NaturalId
    @Email
    private String email;
    @OneToMany(mappedBy = "user")
    private Set<UserTask> userTaskSet;

    public Set<UserTask> getUserTaskSet() {
        return userTaskSet;
    }

    public void setUserTaskSet(Set<UserTask> userTaskSet) {
        this.userTaskSet = userTaskSet;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }
}
