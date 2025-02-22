package com.example.demoo.controllers;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.Track;
import com.example.demoo.models.User;
import com.example.demoo.services.GenreService;
import com.example.demoo.services.PlayListDetailService;
import com.example.demoo.services.PlayListService;
import com.example.demoo.services.TrackService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@FxmlView("/fxml/admin.fxml")
@Component
public class AdminController implements Initializable {

    @Autowired
    private final TrackService trackService;
    @Autowired
    private final GenreService genreService;
    @Autowired
    private final PlayListService playListService;
    @Autowired
    private final PlayListDetailService playListDetailService;



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
    private TableView<TrackDto> playListTableView;
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
    private TableColumn<TrackDto, String> singer;
    @FXML
    private TableColumn<TrackDto, String> title;

    @FXML
    private Button updatePlayList;

    @FXML
    private Button updateTrack;

    private MediaPlayer mediaPlayer;

    @FXML
    private Icon playMusicBtn;

    @FXML
    private Icon stopMusicBtn;
    @FXML
    private Label adminName;
    public User user;

    @FXML
    public void playMusic(ActionEvent actionEvent) {
        Media media = new Media(new File(tracks.getSelectionModel().getSelectedItem().getLink()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
    }
    @FXML
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public AdminController(TrackService trackService, GenreService genreService, PlayListService playListService, PlayListDetailService playListDetailService) {
        this.trackService = trackService;
        this.genreService = genreService;
        this.playListService = playListService;
        this.playListDetailService = playListDetailService;
    }

    @FXML
    void addPlayList(ActionEvent event) {
        sceneManager.switchScene("/fxml/addPlayList.fxml");
    }

    @FXML
    void addTrack(ActionEvent event) {
        sceneManager.switchScene("/fxml/addTrack.fxml");
    }


    @FXML
    void deleteTrack(ActionEvent event) {
        trackService.delete(tracks.getSelectionModel().getSelectedItem());
        tracks.setItems(FXCollections.observableArrayList(trackService.loadTracksFromDb()));
    }

    @FXML
    void manageAccount(ActionEvent event) {
        sceneManager.switchScene("/fxml/manageAccount.fxml");
    }


    @FXML
    void updateTrack(ActionEvent event) {
        Track selectedTrack = tracks.getSelectionModel().getSelectedItem();
        if (selectedTrack == null) {
            return; // Ничего не выбрано
        }

        sceneManager.switchSceneWithController("/fxml/updateTrack.fxml", UpdateTrackController.class, controller -> {
            controller.setTrack(selectedTrack);
        });
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

        singer.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getSingerName()));
        title.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        playListChoicebox.setConverter(new StringConverter<PlayList>() {
            @Override
            public String toString(PlayList playList) {

                return playList != null ? playList.getTitle() : "";
            }

            @Override
            public PlayList fromString(String s) {
                return playListChoicebox.getItems().stream()
                        .filter(singer -> singer.getTitle().equals(s))
                        .findFirst()
                        .orElse(null);
            }
        });
        playListChoicebox.setItems(FXCollections.observableArrayList(playListService.loadAll()));
        playListChoicebox.getSelectionModel().selectedItemProperty().addListener((obs, oldPl, newPl) -> {
            if (newPl != null) {
                playListTableView.setItems(FXCollections.observableArrayList(playListDetailService.findByPlayList(newPl)));
            } else {
                playListTableView.getItems().clear();
            }
        });
    }


    public void settUser(User user) {
        this.user = user;
        adminName.setText(user.getUsername());
    }
}
