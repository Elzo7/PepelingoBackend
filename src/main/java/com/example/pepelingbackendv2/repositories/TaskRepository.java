package com.example.pepelingbackendv2.repositories;

import com.example.pepelingbackendv2.entities.Course;
import com.example.pepelingbackendv2.entities.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task,Long>{
List<Task> findAllByCourseAndDifficulty(Course course, String difficulty);
}
