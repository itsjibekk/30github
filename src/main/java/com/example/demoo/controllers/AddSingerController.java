package com.example.demoo.controllers;

import com.example.demoo.models.Singer;
import com.example.demoo.services.SingerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FxmlView("/fxml/addSinger.fxml")
@Component
public class AddSingerController {

    @Autowired
    private FXMLSceneManager sceneManager;

    @Autowired
    private SingerService singerService;
    @FXML
    private Button back;

    @FXML
    private TextField singerName;

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/addTrack.fxml");
    }

    @FXML
    void saveSinger(ActionEvent event) {
        Singer singer = new Singer();
        singer.setSingername(singerName.getText());
        singerService.save(singer);
        showAlert(Alert.AlertType.CONFIRMATION, "Успешно сохранено");
    }

    public void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType, message);
        alert.show();
    }
}
