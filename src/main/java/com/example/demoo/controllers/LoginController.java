package com.example.demoo.controllers;

import com.example.demoo.models.User;
import com.example.demoo.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        sceneManager.setPrimaryStage(stage);
    }

    public void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка входа", "Поля не могут быть пустыми!", Alert.AlertType.ERROR);
            return;
        }

        try {
            User user = userService.getByUsername(username);

            if (user == null) {
                showAlert("Ошибка входа", "Пользователь не найден!", Alert.AlertType.ERROR);
                return;
            }

            if (!user.getPassword().equals(password)) {
                showAlert("Ошибка входа", "Неверный пароль!", Alert.AlertType.ERROR);
                return;
            }

            if (user.getUserType().getId() == 1) {
                sceneManager.switchSceneWithController("/fxml/admin.fxml", AdminController.class, controller -> {
                    controller.settUser(user);
                });
            } else if (user.getUserType().getId() == 2) {
                sceneManager.switchSceneWithController("/fxml/user.fxml", UserController.class,controller -> {
                    controller.settUser(user);
                });
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка соединения с базой данных!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void handleRegister(ActionEvent event) {
        sceneManager.switchScene("/fxml/register.fxml");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
