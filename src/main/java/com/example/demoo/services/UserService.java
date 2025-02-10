package com.example.demoo.services;

import com.example.demoo.models.User;
import com.example.demoo.repo.UserRepo;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService  {

    private final UserRepo userRepo;


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow();
    }

    public void registerUser(String text, String text1) {

    }

    public boolean authenticate(String username, String password) {
        return false;
    }


    public User getByUsernameAndPassword(String username, String password) {
        return userRepo.getByUsernameAndPassword(username, password).orElseThrow();
    }
}
