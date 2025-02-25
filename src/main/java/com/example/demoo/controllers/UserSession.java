package com.example.demoo.controllers;

import com.example.demoo.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserSession {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    public User getUser() {
        return this.currentUser;
    }
}
