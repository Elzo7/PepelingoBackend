package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.Responses.ErrorResponse;
import com.example.pepelingbackendv2.Responses.LoginResponse;
import com.example.pepelingbackendv2.Responses.RegistrationResponse;
import com.example.pepelingbackendv2.configs.JwtTokenProvider;
import com.example.pepelingbackendv2.entities.SignUpDTO;
import com.example.pepelingbackendv2.entities.Task;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.entities.UserTask;
import com.example.pepelingbackendv2.repositories.TaskRepository;
import com.example.pepelingbackendv2.repositories.UserRepository;
import com.example.pepelingbackendv2.repositories.UserTaskRepository;
import com.google.gson.Gson;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Controller
@CrossOrigin("*")
public class RegistrationController {
    @Autowired
    public UserRepository repo;
    @Autowired
    public TaskRepository taskRepo;
    @Autowired
    public UserTaskRepository userTaskRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticatorManger;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO) throws InterruptedException {
        ErrorResponse errorResponse = new ErrorResponse();
        Gson gson =new Gson();
        List<String> errors = new ArrayList<String>();
        if (repo.existsByLogin(signUpDTO.getUsername()))
        {
            errors.add("registerPage.errors.usernameIsUsed");
            errorResponse.setError(true);
        }
        if (repo.existsByEmail(signUpDTO.getEmail()))
        {
            errors.add("registerPage.errors.emailIsUsed");
            errorResponse.setError(true);
        }
        if (!isValidEmail(signUpDTO.getEmail()))
        {
            errors.add("registerPage.errors.wrongEmail");
            errorResponse.setError(true);
        }
        if(errorResponse.isError())
        {
            errorResponse.setMessages(errors);
            return new ResponseEntity<>(gson.toJson(errorResponse), HttpStatus.OK);
        }
        LoginResponse responseSuccess = new LoginResponse();
        User user =new User();
        user.setEmail(signUpDTO.getEmail());
        user.setLogin(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        repo.save(user);
        List<Task> tasks = (List<Task>) taskRepo.findAll();
        for (Task task: tasks)
        {
            UserTask userTask;
            if(!userTaskRepo.existsByUserAndTask(user,task))
            {
                userTask = new UserTask(user, task, false);
                userTaskRepo.save(userTask);
            }
        }
        responseSuccess.setMessage("User is registered successfully!");
        responseSuccess.setUser_id(user.getId());
        responseSuccess.setEmail(user.getEmail());
        Authentication authentication = authenticatorManger.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(),signUpDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        responseSuccess.setJwt(jwt);
        return new ResponseEntity<>(gson.toJson(responseSuccess), HttpStatus.OK);
    }
    private  boolean isValidEmail(String email){
        return Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                .matcher(email)
                .matches();
    }

}
