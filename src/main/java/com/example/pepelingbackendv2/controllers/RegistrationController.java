package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.entities.SignUpDTO;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
        if (repo.existsByLogin(signUpDTO.getUsername()))
        {
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }
        if (repo.existsByEmail(signUpDTO.getEmail()))
        {
            return new ResponseEntity<>("Email already exists",HttpStatus.BAD_REQUEST);
        }
        User user =new User();
        user.setEmail(signUpDTO.getEmail());
        user.setLogin(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        repo.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }
}
