package com.example.demoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FxmlView("/fxml/admin.fxml")
@Component
public class AdminController {

    @Autowired
    private FXMLSceneManager sceneManager;

    @FXML
    private Button back;

    public void goBack(ActionEvent actionEvent) {
        sceneManager.switchScene("/fxml/login.fxml");
    }
}
