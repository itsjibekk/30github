package com.example.demoo.services;

import com.example.demoo.models.Role;
import com.example.demoo.models.User;
import com.example.demoo.models.UserType;
import com.example.demoo.repo.UserRepo;

import com.example.demoo.repo.UserTypeRepo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserService  {

    private final UserRepo userRepo;
    private final UserTypeRepo userTypeRepo;
    public UserService(UserRepo userRepo, UserTypeRepo userTypeRepo) {
        this.userRepo = userRepo;
        this.userTypeRepo = userTypeRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void registerUser(String login, String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(login);
        UserType userType = userTypeRepo.findById(Long.valueOf(2)).orElseThrow();
        user.setUserType(userType);
        userRepo.save(user);
    }

    public boolean authenticate(String username, String password) {
        return false;
    }


    public User getByUsernameAndPassword(String username, String password) {
        return userRepo.getByUsernameAndPassword(username, password).orElseThrow();
    }

    public boolean userExists(String emailText) {
        return userRepo.existsByUsername(emailText);
    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
       return userRepo.findAll();
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
