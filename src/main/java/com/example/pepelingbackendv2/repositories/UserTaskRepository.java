package com.example.pepelingbackendv2.repositories;

import com.example.pepelingbackendv2.entities.Task;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.entities.UserTask;
import org.springframework.data.repository.CrudRepository;

public interface UserTaskRepository extends CrudRepository<UserTask,Long> {
    Boolean existsByUserAndTask(User user, Task task);
    UserTask findByUserAndTask(User user,Task task);
}
