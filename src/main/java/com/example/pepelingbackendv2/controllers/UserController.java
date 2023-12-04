package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.Responses.RegistrationResponse;
import com.example.pepelingbackendv2.configs.JwtTokenProvider;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.entities.UserDTO;
import com.example.pepelingbackendv2.repositories.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticatorManger;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @PostMapping("/api/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDTO)
    {
        Gson gson = new Gson();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("User not authenticated");
            return new ResponseEntity(gson.toJson(response), HttpStatus.UNAUTHORIZED);
        }
        if(!repo.existsById(userDTO.getId()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setMessage("Podany uzytkownik nie istnieje");
            response.setError(true);
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user = repo.findById(userDTO.getId()).get();
        if(!passwordEncoder.matches(userDTO.getOldPassword(),user.getPassword()))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setMessage("Podano zle stare haslo");
            response.setError(true);
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        repo.save(user);
        RegistrationResponse response = new RegistrationResponse();
        response.setMessage("Haslo poprawnie zmienione");
        return new ResponseEntity(gson.toJson(response),HttpStatus.OK);
    }
    @DeleteMapping("/api/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id)
    {
        Gson gson = new Gson();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            RegistrationResponse response = new RegistrationResponse();
            response.setError(true);
            response.setMessage("User not authenticated");
            return new ResponseEntity(gson.toJson(response), HttpStatus.UNAUTHORIZED);
        }
        if(!repo.existsById(id))
        {
            RegistrationResponse response = new RegistrationResponse();
            response.setMessage("Podany uzytkownik nie istnieje");
            return new ResponseEntity(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        User user = repo.findById(id).get();
        repo.delete(user);
        RegistrationResponse response = new RegistrationResponse();
        response.setMessage("Uzytkownik zostal usuniety");
        return new ResponseEntity(gson.toJson(response),HttpStatus.OK);
    }

}
