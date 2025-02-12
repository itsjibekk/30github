package com.example.demoo.controllers;

import com.example.demoo.models.Genre;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.PlayListDetail;
import com.example.demoo.models.Track;
import com.example.demoo.services.GenreService;
import com.example.demoo.services.PlayListService;
import com.example.demoo.services.TrackService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlView("/fxml/admin.fxml")
@Component
public class AdminController implements Initializable {

    private final TrackService trackService;

    private final GenreService genreService;
    private final PlayListService playListService;

    @Autowired
    private FXMLSceneManager sceneManager;

    @FXML
    private Button back;
    @FXML
    private Button addPlayList;

    @FXML
    private Button addTrack;

    @FXML
    private Button deletePlayList;

    @FXML
    private Button deleteTrack;

    @FXML
    private Button manageAcc;

    @FXML
    private ChoiceBox<PlayList> playListChoicebox;

    @FXML
    private ListView<PlayListDetail> playListView;
    @FXML
    private TableView<Track> tracks;

    @FXML
    private TableColumn<Track, String> trackGenre;

    @FXML
    private TableColumn<Track, Long> trackId;

    @FXML
    private TableColumn<Track,String> trackName;

    @FXML
    private TableColumn<Track, String> trackSinger;

    @FXML
    private Button updatePlayList;

    @FXML
    private Button updateTrack;

    public AdminController(TrackService trackService, GenreService genreService, PlayListService playListService) {
        this.trackService = trackService;
        this.genreService = genreService;
        this.playListService = playListService;
    }

    @FXML
    void addPlayList(ActionEvent event) {

    }

    @FXML
    void addTrack(ActionEvent event) {
        sceneManager.switchScene("/fxml/addTrack.fxml");
    }

    @FXML
    void deletePlayList(ActionEvent event) {

    }

    @FXML
    void deleteTrack(ActionEvent event) {

    }

    @FXML
    void manageAccount(ActionEvent event) {

    }

    @FXML
    void updatePlayList(ActionEvent event) {

    }

    @FXML
    void updateTrack(ActionEvent event) {

    }

    public void goBack(ActionEvent actionEvent) {
        sceneManager.switchScene("/fxml/login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trackId.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));

        trackName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getName()));

        trackGenre.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getGenre().getName()));

        trackSinger.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getSinger().getSingername()));
        tracks.setItems(FXCollections.observableArrayList(trackService.loadTracksFromDb()));
    }
}
