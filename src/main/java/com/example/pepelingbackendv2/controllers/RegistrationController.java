package com.example.pepelingbackendv2.controllers;

import com.example.pepelingbackendv2.Responses.LoginResponse;
import com.example.pepelingbackendv2.Responses.RegistrationResponse;
import com.example.pepelingbackendv2.configs.JwtTokenProvider;
import com.example.pepelingbackendv2.entities.SignUpDTO;
import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.repositories.UserRepository;
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

import java.util.regex.Pattern;

@Controller
@CrossOrigin("*")
public class RegistrationController {
    @Autowired
    public UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticatorManger;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO)
    {
        RegistrationResponse response = new RegistrationResponse();
        Gson gson =new Gson();
        if (repo.existsByLogin(signUpDTO.getUsername()))
        {
            response.setMessage("registerPage.errors.usernameIsUsed");
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }
        if (repo.existsByEmail(signUpDTO.getEmail()))
        {
            response.setMessage("registerPage.errors.emailIsUsed");
            return new ResponseEntity<>(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        if (!isValidEmail(signUpDTO.getEmail()))
        {
            response.setMessage("registerPage.errors.wrongEmail");
            return new ResponseEntity<>(gson.toJson(response),HttpStatus.BAD_REQUEST);
        }
        LoginResponse responseSuccess = new LoginResponse();
        User user =new User();
        user.setEmail(signUpDTO.getEmail());
        user.setLogin(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        String old_password=user.getPassword();
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        repo.save(user);
        responseSuccess.setMessage("User is registered successfully!");
        responseSuccess.setUser_id(user.getId());
        Authentication authentication = authenticatorManger.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(),old_password));
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
