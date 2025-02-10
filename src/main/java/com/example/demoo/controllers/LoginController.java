package com.example.demoo.controllers;

import com.example.demoo.models.User;
import com.example.demoo.repo.UserRepo;
import com.example.demoo.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/login.fxml")
public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Autowired
    private UserService userService;

    @Autowired
    private FXMLSceneManager sceneManager;

    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = null;

        try {
            user = userService.getByUsernameAndPassword(username, password);
            System.out.println("User: " + user); // Log the user object

            if (user.getUserType().getId() == 1) {
                System.out.println("Switching to admin scene...");
                sceneManager.switchScene("/fxml/admin.fxml");  // Admin page
            } else if (user.getUserType().getId() == 2) {
                System.out.println("Switching to user scene...");
                sceneManager.switchScene("/fxml/user.fxml");   // User page
            }
        } catch (Exception e) {
            System.out.println("Ошибка соединения! " + username + " " + password);
            e.printStackTrace(); // Print the exception
        }
    }


    public void handleRegister(ActionEvent event) {
        sceneManager.switchScene("/fxml/register.fxml");
    }
}


