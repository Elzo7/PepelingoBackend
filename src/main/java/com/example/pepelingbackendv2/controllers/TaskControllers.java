package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.Responses.AllTasksResponse;
import com.example.pepelingbackendv2.Responses.RegistrationResponse;
import com.example.pepelingbackendv2.Responses.TaskResponse;
import com.example.pepelingbackendv2.entities.*;
import com.example.pepelingbackendv2.repositories.CourseRepository;
import com.example.pepelingbackendv2.repositories.TaskRepository;
import com.example.pepelingbackendv2.repositories.UserRepository;
import com.example.pepelingbackendv2.repositories.UserTaskRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@CrossOrigin("*")
public class TaskControllers {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserTaskRepository userTaskRepo;
    @RequestMapping("/api/task/id")
    public ResponseEntity<?> getTaskById(@RequestBody TaskDTO taskDTO)
    {
        Gson gson = new Gson();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("User not authenticated");
            return new ResponseEntity(gson.toJson(response), HttpStatus.UNAUTHORIZED);
        }
        if(!userRepo.existsById(taskDTO.getUser_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setMessage("Podany uzytkownik nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        if(!taskRepo.existsById(taskDTO.getTask_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setMessage("Podane zadanie nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user = userRepo.findById(taskDTO.getUser_id()).get();
        Task task = taskRepo.findById(taskDTO.getTask_id()).get();
        UserTask userTask;
        if(!userTaskRepo.existsByUserAndTask(user,task))
        {
            userTask = new UserTask(user,task,false);
            userTaskRepo.save(userTask);
        }
        else
        {
            userTask = userTaskRepo.findByUserAndTask(user,task);
        }
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setAnswer(task.getAnswer());
        response.setType(task.getType());
        response.setQuestion(task.getQuestion());
        response.setPictureUrl(task.getPictureUrl());
        response.setCompleted(userTask.isCompleted());
        return new ResponseEntity(gson.toJson(response), HttpStatus.OK);
    }
    @RequestMapping("/api/tasks")
    public ResponseEntity<?> getAllTaskByID(@RequestBody AllTaskDTO allTaskDTO)
    {
        Gson gson = new Gson();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("User not authenticated");
            return new ResponseEntity(gson.toJson(response), HttpStatus.UNAUTHORIZED);
        }
        if(!userRepo.existsById(allTaskDTO.getUser_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Podany uzytkownik nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user = userRepo.findById(allTaskDTO.getUser_id()).get();
        if(!courseRepository.existsById(allTaskDTO.getCourse_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Podany kurs nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        Course course = courseRepository.findById(allTaskDTO.getCourse_id()).get();
        List<Task> tasks = taskRepo.findAllByCourseAndDifficulty(course, allTaskDTO.getDifficulty());
        if(tasks.size() == 0)
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Kurs nie ma zadnych zadan");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        AllTasksResponse allTasksResponse = new AllTasksResponse();
        List<TaskResponse> tasksResponse = new ArrayList();
        for (Task task :tasks)
        {
            UserTask userTask;
            if(!userTaskRepo.existsByUserAndTask(user,task))
            {
                userTask = new UserTask(user, task, false);
                userTaskRepo.save(userTask);
            }
            else
            {
                userTask =userTaskRepo.findByUserAndTask(user,task);
            }
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setId(task.getId());
            taskResponse.setCompleted(userTask.isCompleted());
            taskResponse.setQuestion(task.getQuestion());
            taskResponse.setPictureUrl(task.getPictureUrl());
            taskResponse.setType(task.getType());
            taskResponse.setAnswer(task.getAnswer());
            taskResponse.setDifficulty(task.getDifficulty());
            tasksResponse.add(taskResponse);
        }
        allTasksResponse.setTasksResponse(tasksResponse);
        allTasksResponse.setMessage("Poprawnie pobrano tabele zadan");
        System.out.println(tasksResponse.size());
        return new ResponseEntity(gson.toJson(allTasksResponse), HttpStatus.OK);
    }
}
