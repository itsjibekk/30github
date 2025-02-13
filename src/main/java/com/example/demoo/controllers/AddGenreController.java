package com.example.demoo.controllers;

import com.example.demoo.models.Genre;
import com.example.demoo.services.GenreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@FxmlView("/fxml/addGenre.fxml")
@Component
public class AddGenreController {
    @Autowired
    private FXMLSceneManager sceneManager;

    @Autowired
    private GenreService genreService;
    @FXML
    private Button back;

    @FXML
    private TextField genreName;

    @FXML
    private Button saveBtn;

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/addTrack.fxml");
    }

    @FXML
    void saveGenre(ActionEvent event) {
        Genre genre = new Genre();
        genre.setName(genreName.getText());
        genreService.save(genre);
        showAlert(Alert.AlertType.CONFIRMATION, "Успешно сохранено");
    }

    public void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType, message);
        alert.show();
    }
}
