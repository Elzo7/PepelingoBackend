package com.example.pepelingbackendv2.repositories;

import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.entities.UserTask;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User,Long> {
    User findByLogin(String login);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
}
