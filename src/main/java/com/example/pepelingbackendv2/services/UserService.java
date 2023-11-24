package com.example.pepelingbackendv2.services;

import com.example.pepelingbackendv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.pepelingbackendv2.entities.User user = repo.findByLogin(username);
        if (user != null)
        {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getLogin())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        }
        else
            throw new UsernameNotFoundException("User not found");
    }
}
