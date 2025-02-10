package com.example.demoo.controllers;

import com.example.demoo.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;

    @Autowired
    private UserService userService;
    @Autowired
    private FXMLSceneManager sceneManager;

    public void handleRegister(ActionEvent event) {
        try {
            userService.registerUser(
                    usernameField.getText(),
                    passwordField.getText()
            );
            sceneManager.switchScene("/fxml/login.fxml");
        } catch (Exception e) {
            // Show error message
        }
    }
}
