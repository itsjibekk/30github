package com.example.demoo.controllers;

import com.example.demoo.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("/fxml/register.fxml")
public class RegisterController {

    @FXML private TextField password;
    @FXML private TextField email;

    @Autowired
    private UserService userService;
    @Autowired
    private FXMLSceneManager sceneManager;

    public void handleRegister(ActionEvent event) {
        String emailText = email.getText().trim();
        String passwordText = password.getText().trim();

        // Проверка на пустые поля
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            showAlert("Ошибка регистрации", "Пожалуйста, заполните все поля.", Alert.AlertType.ERROR);
            return;
        }

        // Проверка соответствия пароля требованиям (минимум 8 символов, буквы и цифры)
        if (!isValidPassword(passwordText)) {
            showAlert("Ошибка регистрации", "Пароль должен содержать минимум 8 символов, включая буквы и цифры.", Alert.AlertType.ERROR);
            return;
        }

        try {

            if (userService.userExists(emailText)) {
                showAlert("Ошибка регистрации", "Пользователь с таким email уже зарегистрирован.", Alert.AlertType.ERROR);
                return;
            }

            userService.registerUser(emailText, passwordText);

            showAlert("Успех!", "Регистрация прошла успешно.", Alert.AlertType.CONFIRMATION);
            sceneManager.switchScene("/fxml/login.fxml");

        } catch (Exception e) {
            showAlert("Ошибка", "Произошла ошибка при регистрации. Попробуйте снова.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    private void showAlert(String title, String message, Alert.AlertType alertype) {
        Alert alert = new Alert(alertype);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/login.fxml");
    }

}
