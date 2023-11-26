package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.Responses.RegistrationResponse;
import com.example.pepelingbackendv2.entities.SignUpDTO;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.repositories.UserRepository;
import com.google.gson.Gson;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.regex.Pattern;

@Controller
@CrossOrigin("*")
public class RegistrationController {
    @Autowired
    public UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO)
    {
        RegistrationResponse response = new RegistrationResponse();
        Gson gson =new Gson();
        if (repo.existsByLogin(signUpDTO.getUsername()))
        {
            response.setMessage("Username already exist");
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }
        if (repo.existsByEmail(signUpDTO.getEmail()))
        {
            response.setMessage("Email already exists");
            return new ResponseEntity<>(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        if (!isValidEmail(signUpDTO.getEmail()))
        {
            response.setMessage("Email is incorrect");
            return new ResponseEntity<>(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user =new User();
        user.setEmail(signUpDTO.getEmail());
        user.setLogin(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        repo.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }
    private  boolean isValidEmail(String email){
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")
                .matcher(email)
                .matches();
    }

}
