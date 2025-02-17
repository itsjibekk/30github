package com.example.demoo.controllers;

import com.example.demoo.models.Genre;
import com.example.demoo.models.Singer;
import com.example.demoo.models.Track;
import com.example.demoo.services.GenreService;
import com.example.demoo.services.SingerService;
import com.example.demoo.services.TrackService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Data;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@FxmlView("/fxml/updateTrackUser.fxml")
@Component
@Data
public class UpdateTrackUserController implements Initializable {

        @Autowired
        private SingerService singerService;

        @Autowired
        private FXMLSceneManager sceneManager;

        @Autowired
        private GenreService genreService;

        @Autowired
        private TrackService trackService;

        @FXML
        private Button back;

        private Track currentTrack;

        @FXML
        private ChoiceBox<Genre> genreChoiceBox = new ChoiceBox<>();

        @FXML
        private Button importMusic = new Button();

        @FXML
        private Button updateBtn;

        @FXML
        private ChoiceBox<Singer> singerChoiceBox = new ChoiceBox<>();

        @FXML
        private TextField trackName = new TextField();

        @FXML
        void goBack(ActionEvent event) {
            sceneManager.switchScene("/fxml/user.fxml");
        }

        @FXML
        void handleSelectMP3(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

            Stage stage = (Stage) importMusic.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                importMusic.setText(file.getAbsolutePath());
            }
        }

    @FXML
    void update(ActionEvent event) {
        if (currentTrack == null) return;

        String title = trackName.getText().trim();
        Singer singer = singerChoiceBox.getValue();
        Genre genre = genreChoiceBox.getValue();
        if (title.isEmpty() || singer == null || genre == null || importMusic.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Заполните все поля!");
            return;
        }
        currentTrack.setTitle(trackName.getText().trim());
        currentTrack.setSinger(singerChoiceBox.getValue());
        currentTrack.setGenre(genreChoiceBox.getValue());
        currentTrack.setLink(importMusic.getText());

        trackService.update(currentTrack);
        showAlert(Alert.AlertType.CONFIRMATION, "Трек успешно обновлён!");
    }

    private void showAlert(Alert.AlertType alertype, String message) {
        Alert alert = new Alert(alertype, message);
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        singerChoiceBox.setConverter(new StringConverter<Singer>() {
            @Override
            public String toString(Singer singer) {
                return singer != null ? singer.getSingername() : "";
            }

            @Override
            public Singer fromString(String s) {
                return singerChoiceBox.getItems().stream()
                        .filter(singer -> singer.getSingername().equals(s))
                        .findFirst()
                        .orElse(null);
            }
        });
        genreChoiceBox.setConverter(new StringConverter<Genre>() {
            @Override
            public String toString(Genre genre) {
                return genre != null ? genre.getName() : "";
            }

            @Override
            public Genre fromString(String s) {
                return genreChoiceBox.getItems().stream()
                        .filter(singer -> singer.getName().equals(s))
                        .findFirst()
                        .orElse(null);
            }
        });
        singerChoiceBox.setItems(FXCollections.observableArrayList(singerService.getAll()));
        genreChoiceBox.setItems(FXCollections.observableArrayList(genreService.getAll()));

    }

    public void setTrack(Track track) {
        this.currentTrack = track;
        trackName.setText(track.getName());
        genreChoiceBox.setValue(track.getGenre());
        singerChoiceBox.setValue(track.getSinger());
        importMusic.setText(track.getLink());
    }
}


