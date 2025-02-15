package com.example.demoo.controllers;

import com.example.demoo.models.User;
import com.example.demoo.models.UserType;
import com.example.demoo.services.UserService;
import com.example.demoo.services.UserTypeService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/fxml/updateUser.fxml")
public class UpdateUserController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private ChoiceBox<UserType> roleChoiceBox;

    @FXML
    private Button updateUser;

    @Autowired
    private FXMLSceneManager sceneManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;
    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/manageAccount.fxml");
    }

    @FXML
    void updateUser(ActionEvent event) {
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пользователь не выбран!");
            return;
        }

        String usernameInput = login.getText().trim();
        String passwordInput = password.getText().trim();
        UserType selectedRole = roleChoiceBox.getValue();

        // 1. Проверка на пустые строки
        if (usernameInput.isEmpty() || passwordInput.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Все поля должны быть заполнены!");
            return;
        }

        // 2. Проверка на уникальность логина (исключаем текущего пользователя)
        User existingUser = userService.findByUsername(usernameInput);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пользователь с таким логином уже существует!");
            return;
        }

        // 3. Проверка длины пароля
        if (passwordInput.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пароль должен содержать минимум 6 символов!");
            return;
        }

        // 4. Обновляем существующего пользователя, а не создаём нового!
        user.setUsername(usernameInput);
        user.setPassword(passwordInput); // Лучше хешировать пароль
        user.setUserType(selectedRole);

        userService.save(user);

        showAlert(Alert.AlertType.INFORMATION, "Успех", "Пользователь успешно обновлен!");
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private User user;

    public void setUser(User user) {
        this.user = user;
        login.setText(user.getUsername());
        password.setText(user.getPassword());
        roleChoiceBox.setValue(user.getUserType());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleChoiceBox.setItems(FXCollections.observableArrayList(userTypeService.findAll()));
        roleChoiceBox.setConverter(new StringConverter<UserType>() {
            @Override
            public String toString(UserType userType) {

                return userType != null ? userType.getName() : "";
            }

            @Override
            public UserType fromString(String s) {
                return roleChoiceBox.getItems().stream()
                        .filter(singer -> singer.getName().equals(s))
                        .findFirst()
                        .orElse(null);

            }
        });
    }
}
