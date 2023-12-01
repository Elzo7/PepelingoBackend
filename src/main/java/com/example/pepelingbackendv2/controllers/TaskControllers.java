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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@CrossOrigin(origins="*")
public class TaskControllers {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserTaskRepository userTaskRepo;
    @PostMapping("/api/task/id")
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
    @PostMapping("/api/tasks")
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
        if(!courseRepository.existsByName(allTaskDTO.getCourse_name()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Podany kurs nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        Course course = courseRepository.findByName(allTaskDTO.getCourse_name());
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
            System.out.println(task.getType());
            if(task.getType().equals("radius"))
            {
                List<String> answers = List.of(task.getAnswer().split(";"));
                System.out.println(answers);
                taskResponse.setPossible_answer(answers);
                taskResponse.setAnswer(answers.get(0));
            }
            else
            {
                taskResponse.setAnswer(task.getAnswer());
            }

            taskResponse.setDifficulty(task.getDifficulty());
            tasksResponse.add(taskResponse);
        }
        allTasksResponse.setTasksResponse(tasksResponse);
        allTasksResponse.setMessage("Poprawnie pobrano tabele zadan");
        System.out.println(tasksResponse.size());
        return new ResponseEntity(gson.toJson(allTasksResponse), HttpStatus.OK);
    }
    @PostMapping("/api/task/complete")
    public ResponseEntity<?> setTaskCompletion(@RequestBody TaskDTO taskDTO)
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
            response.setError(true);
            response.setMessage("Podany uzytkownik nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        if(!taskRepo.existsById(taskDTO.getTask_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Podane zadanie nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user= userRepo.findById(taskDTO.getUser_id()).get();
        Task task = taskRepo.findById(taskDTO.getTask_id()).get();
        UserTask userTask;
        if(!userTaskRepo.existsByUserAndTask(user,task))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Uzytkownik nie pobral danego zadania");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        else
        {
            userTask =userTaskRepo.findByUserAndTask(user,task);
        }
        userTaskRepo.save(userTask);
        RegistrationResponse response = new RegistrationResponse();
        response.setMessage("Poprawnie ustawiono zadanie jako ukonczone");
        return new ResponseEntity<>(gson.toJson(response),HttpStatus.OK);
    }
    @PostMapping("/api/course")
    public ResponseEntity<?> getCourseProgres(@RequestBody CourseDTO courseDTO)
    {
        Gson gson = new Gson();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("User not authenticated");
            return new ResponseEntity(gson.toJson(response), HttpStatus.UNAUTHORIZED);
        }
        if(!userRepo.existsById(courseDTO.getUser_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Podany uzytkownik nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user = userRepo.findById(courseDTO.getUser_id()).get();
        Course course = courseRepository.findByName(courseDTO.getCourse_name());
        Set<UserTask> userTasks=user.getUserTaskSet();
        Map<String, Map<String, Map<String, Integer>>> progressMap = new HashMap<>();
        for (UserTask userTask : userTasks) {
            Task task = userTask.getTask();
            if (task.getCourse().equals(course)) {
                String difficulty = task.getDifficulty();
                String type = task.getType();
                progressMap.computeIfAbsent(difficulty, k -> new HashMap<>());
                progressMap.get(difficulty).computeIfAbsent(type, k -> new HashMap<>());
                progressMap.get(difficulty).get(type).merge("completed", userTask.isCompleted() ? 1 : 0, Integer::sum);
                progressMap.get(difficulty).get(type).merge("total", 1, Integer::sum);

            }
        }
        System.out.println(progressMap);
        Map<String, Map<String, Map<String, Double>>> completionPercentageMap = new HashMap<>();

        progressMap.forEach((difficulty, typeMap) -> {
            completionPercentageMap.put(difficulty, new HashMap<>());

            typeMap.forEach((type, taskMap) -> {
                int completedTasks = (int) taskMap.get("completed");
                int totalTasks = (int) taskMap.get("total");

                double completionPercentage = totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100 : 0;
                completionPercentageMap.get(difficulty).put(type, Map.of("completed", (double)completedTasks, "total",(double) totalTasks, "completionPercentage",   Math.round(completionPercentage*100.)/100.));
            });
        });
        return new ResponseEntity(completionPercentageMap, HttpStatus.OK);
    }
    @PostMapping("/api/course/all")
    public ResponseEntity<?> getAllCoursesProgres(@RequestBody AllCourseDTO allCourseDTO)
    {
        Gson gson = new Gson();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("User not authenticated");
            return new ResponseEntity(gson.toJson(response), HttpStatus.UNAUTHORIZED);
        }
        if(!userRepo.existsById(allCourseDTO.getUser_id()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("Podany uzytkownik nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user = userRepo.findById(allCourseDTO.getUser_id()).get();
        Set<UserTask> userTasks=user.getUserTaskSet();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> courseProgressMap = new HashMap<>();
        for (UserTask userTask: userTasks)
        {
            Task task = userTask.getTask();
            Course course = task.getCourse();
            String courseId = course.getName();
            String difficulty = task.getDifficulty();
            String type = task.getType();
            courseProgressMap.computeIfAbsent(courseId, k -> new HashMap<>());
            courseProgressMap.get(courseId).computeIfAbsent(difficulty, k -> new HashMap<>());
            courseProgressMap.get(courseId).get(difficulty).computeIfAbsent(type, k -> new HashMap<>());
            courseProgressMap.get(courseId).get(difficulty).get(type).merge("completed", userTask.isCompleted() ? 1 : 0, Integer::sum);
            courseProgressMap.get(courseId).get(difficulty).get(type).merge("total", 1, Integer::sum);
        }
        Map<String, Map<String, Map<String, Map<String, Double>>>> courseCompletionPercentageMap = new HashMap<>();

        courseProgressMap.forEach((courseId, progressMap) -> {
            courseCompletionPercentageMap.put(courseId, new HashMap<>());

            progressMap.forEach((difficulty, typeMap) -> {
                typeMap.forEach((type, taskMap) -> {
                    int completedTasks = taskMap.get("completed");
                    int totalTasks =  taskMap.get("total");

                    double completionPercentage = totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100 : 0;
                    courseCompletionPercentageMap.get(courseId).put(difficulty, Map.of(type, Map.<String, Double>of("completed",(double) completedTasks, "total",(double) totalTasks, "completionPercentage",Math.round(completionPercentage*100.)/100.)));
                });
            });
        });
        return new ResponseEntity(gson.toJson(courseCompletionPercentageMap), HttpStatus.OK);

    }
}
