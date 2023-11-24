package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.Responses.LoginResponse;
import com.example.pepelingbackendv2.configs.JwtTokenProvider;
import com.example.pepelingbackendv2.entities.LoginDTO;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.Gson;
import java.awt.*;

@Controller
public class LoginController {
    @Autowired
    public UserRepository repo;
    @Autowired
    private AuthenticationManager authenticatorManger;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO)
    {
        System.out.println(loginDTO.getUsername());
        System.out.println(loginDTO.getPassword());
        Authentication authentication = authenticatorManger.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse response = new LoginResponse();
        String jwt = tokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        response.setMessage("User logged succesfully!");
        response.setJwt(jwt);
        User user=repo.findByLogin(loginDTO.getUsername());
        response.setUser_id(user.getId());
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
    }
}
