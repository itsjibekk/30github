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
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FxmlView("/fxml/addTrackUser.fxml")
@Component
public class AddTrackUserController implements Initializable {

    @Autowired
    private FXMLSceneManager sceneManager;

    @Autowired
    private TrackService trackService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private GenreService genreService;

    @FXML
    private Button addGenre, addSinger, back, importMusic, saveBtn;

    @FXML
    private ChoiceBox<Genre> genreChoiceBox;

    @FXML
    private ChoiceBox<Singer> singerChoiceBox;

    @FXML
    private TextField trackName;

    private String filePath = ""; // Полный путь к выбранному файлу

    @FXML
    void save(ActionEvent event) {
        String title = trackName.getText().trim();
        Singer singer = singerChoiceBox.getValue();
        Genre genre = genreChoiceBox.getValue();

        // 🔹 Проверка на пустые поля
        if (title.isEmpty() || singer == null || genre == null || filePath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Заполните все поля!");
            return;
        }

        // 🔹 Проверка на дубликаты (по названию и исполнителю)
        List<Track> existingTracks = trackService.findByTitleAndSinger(title, singer);
        if (!existingTracks.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Трек с таким названием уже существует!");
            return;
        }

        // 🔹 Создание и сохранение трека
        Track track = new Track();
        track.setTitle(title);
        track.setSinger(singer);
        track.setGenre(genre);
        track.setLink(filePath);
        trackService.save(track);

        showAlert(Alert.AlertType.CONFIRMATION,"Трек успешно добавлен!");
    }
    @FXML
    private void handleSelectMP3() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

        Stage stage = (Stage) importMusic.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            filePath = file.getAbsolutePath();
            importMusic.setText("Выбран: " + file.getName());
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/user.fxml");
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

    private void showAlert(Alert.AlertType alertype, String message) {
        Alert alert = new Alert(alertype, message);
        alert.show();
    }
    @FXML
    void addGenre(ActionEvent event) {
        sceneManager.switchScene("/fxml/addGenre.fxml");
    }

    @FXML
    void addSinger(ActionEvent event) {
        sceneManager.switchScene("/fxml/addSinger.fxml");
    }

}
