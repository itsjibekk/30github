package com.example.demoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/user.fxml")
public class UserController {

    @Autowired
    private FXMLSceneManager fxmlSceneManager;

    @FXML
    private Button back;

    @FXML
    void goBack(ActionEvent event) {
        fxmlSceneManager.switchScene("/fxml/login.fxml");
    }
}
