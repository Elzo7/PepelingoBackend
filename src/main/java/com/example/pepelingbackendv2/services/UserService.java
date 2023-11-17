package com.example.pepelingbackendv2.services;

import com.example.pepelingbackendv2.entities.User;
import com.example.pepelingbackendv2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (repo.existsByLogin(username))
        {
            return (UserDetails) repo.findByLogin(username);
        }
        else
            throw new UsernameNotFoundException("User not found");
    }
}
