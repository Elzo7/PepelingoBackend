package com.example.pepelingbackendv2.repositories;

import com.example.pepelingbackendv2.entities.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task,Long>{

}
