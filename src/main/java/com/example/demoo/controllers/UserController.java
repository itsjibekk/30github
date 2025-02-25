package com.example.demoo.controllers;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.Singer;
import com.example.demoo.models.Track;
import com.example.demoo.models.User;
import com.example.demoo.services.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("/fxml/user.fxml")
public class UserController implements Initializable{

    @Autowired
    private FXMLSceneManager fxmlSceneManager;

    @FXML
    private Button back;

    @FXML
    private Button accountButton;

    @FXML
    private Button addTrack;

    @FXML
    private Label adminName;

    @FXML
    private AnchorPane artists;

    @FXML
    private AnchorPane tracks;

    @FXML
    private Button artistsButton;

    @FXML
    private TableColumn<Track, String> genreName;
    @FXML
    private TableColumn<Track, String> singerName;
    @FXML
    private TableColumn<Track, String> trackName;
    @FXML
    private TableColumn<Track, Void> action;
    @FXML
    private TableColumn<Singer, String> singer;
    @FXML
    private TableColumn<PlayList, String> playListC;
    @FXML
    private TableColumn<TrackDto, String> trackPL;
    @FXML
    private TableColumn<TrackDto, String> singerPL;
    @FXML
    private TableColumn<TrackDto, String> genrePL;

    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();

    @FXML
    private Button logout;

    private MediaPlayer mediaPlayer;

    @FXML
    private Button playlistButton;

    @FXML
    private AnchorPane playlists;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;
    @FXML
    private TextField searchFieldA;

    @FXML
    private TableView<Track> tracksTB;

    @FXML
    private TableView<Singer> artistsTB;

    @FXML
    private TableView<PlayList> playlistsTB;
    @FXML
    private TableView<TrackDto> tracksInPL;
    private User user;
    @FXML
    private Button trackButton;

    @FXML
    private Button updateTrack;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayListDetailService playListDetailService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserSession userSession; // Внедряем сессию


    @FXML
    void addTrack(ActionEvent event) {
        fxmlSceneManager.switchScene("/fxml/addTrackUser.fxml");
    }

    @FXML
    void updateTrack(ActionEvent event) {
        Track selectedTrack = tracksTB.getSelectionModel().getSelectedItem();
        if (selectedTrack == null) {
            return;
        }
        fxmlSceneManager.switchSceneWithController("/fxml/updateTrackUser.fxml", UpdateTrackUserController.class, controller -> {
            controller.setTrack(selectedTrack);
        });
    }

    @FXML
    void goBack(ActionEvent event) {
        fxmlSceneManager.switchScene("/fxml/login.fxml");
    }


    private void showPane(AnchorPane paneToShow) {
        artists.setVisible(false);
        playlists.setVisible(false);
        tracks.setVisible(false);

        paneToShow.setVisible(true);
    }

    @FXML
    void showArtists(ActionEvent event) {
        showPane(artists);
    }

    @FXML
    void showPlaylists(ActionEvent event) {
        showPane(playlists);
    }

    @FXML
    void showTracks(ActionEvent event) {
        showPane(tracks);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        user = userSession.getUser(); // Получение текущего пользователя
        if (user != null) {
            adminName.setText(user.getUsername()); // Устанавливаем имя пользователя
        } else {
            System.out.println("Ошибка: пользователь не найден в сессии.");
        }
        trackName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getName()));

        genreName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getGenre().getName()));

        singerName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getSinger().getSingername()));

        singer.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getSingername()));

        playListC.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));

        trackPL.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        singerPL.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getSingerName()));
        genrePL.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getGenre()));
        action.setCellFactory(param -> new TableCell<>() {
            private final Button playButton = new Button("Play");
            private final Button stopButton = new Button("Stop");
            private final Button deleteButton = new Button("Delete");

            {
                playButton.setOnAction(event -> {
                    playMusic();
                });

                stopButton.setOnAction(event -> {
                    stopMusic();
                });

                deleteButton.setOnAction(event -> {
                    deleteMusic();
                });

                playButton.setStyle("-fx-background-color: #f6b2ff; -fx-text-fill: black;");
                stopButton.setStyle("-fx-background-color: #f6b2ff; -fx-text-fill: black;");
                deleteButton.setStyle("-fx-background-color: #f6b2ff; -fx-text-fill: black;");
                playButton.getStyleClass().add("glow-button");
                deleteButton.getStyleClass().add("glow-button");
                stopButton.getStyleClass().add("glow-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(10, playButton, stopButton, deleteButton);
                    setGraphic(buttonsBox);
                }
            }
        });

        tracksTB.setItems(FXCollections.observableArrayList(trackService.loadTracksFromDb()));
        artistsTB.setItems(FXCollections.observableArrayList(singerService.getAll()));

        List<String> list = new ArrayList<>();
        list.add("Трек");
        list.add("Исполнитель");
        list.add("Жанр");
        choiceBox.setItems(FXCollections.observableArrayList(list));
        choiceBox.setValue("Трек");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTracks());
        searchFieldA.textProperty().addListener((observable, oldValue, newValue) -> filterArtists());
    }


    @FXML
    void addPlaylist() {
        fxmlSceneManager.switchhSceneWithController("/fxml/addPlayListUser.fxml", AddPlayListUserController.class);
    }
    @FXML
    public void showTrackss(){
        tracksInPL.setItems(FXCollections.observableArrayList(playListDetailService.findByPlayList(playlistsTB.getSelectionModel().getSelectedItem())));
    }

    @FXML
    public void updatePlaylist(){

    }
    private void filterArtists() {
        String searchText = searchFieldA.getText().toLowerCase().trim();
        List<Singer> allTracks = singerService.getAll();
        List<Singer> filteredTracks = allTracks.stream()
                .filter(track -> {
                    if (searchText.isEmpty()) {
                        return true;
                    }
                        return track.getSingername().toLowerCase().startsWith(searchText);

                })
                .toList();
        artistsTB.setItems(FXCollections.observableArrayList(filteredTracks));
    }

    private void filterTracks() {
        String searchText = searchField.getText().toLowerCase().trim();

        List<Track> allTracks = trackService.loadTracksFromDb();

        List<Track> filteredTracks = allTracks.stream()
                .filter(track -> {
                    if (searchText.isEmpty()) {
                        return true;
                    }
                    return track.getName().toLowerCase().contains(searchText) ||
                            track.getSinger().getSingername().toLowerCase().contains(searchText) ||
                            track.getGenre().getName().toLowerCase().contains(searchText);
                })
                .toList();

        tracksTB.setItems(FXCollections.observableArrayList(filteredTracks));
    }

    private void deleteMusic() {
        Track track = tracksTB.getSelectionModel().getSelectedItem();
        trackService.delete(track);
        tracksTB.setItems(FXCollections.observableArrayList(trackService.loadTracksFromDb()));
    }

    public void playMusic() {
        // Stop any currently playing track before starting a new one
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Track selectedTrack = tracksTB.getSelectionModel().getSelectedItem();
        if (selectedTrack == null) {
            return;
        }

        Media media = new Media(new File(selectedTrack.getLink()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });

        mediaPlayer.play();
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    public void settUser(User user) {
        this.user = user;
        userSession.setCurrentUser(user);
        adminName.setText(user.getUsername());
        playlistsTB.setItems(FXCollections.observableArrayList(playListService.findByUser(user)));
    }

}
