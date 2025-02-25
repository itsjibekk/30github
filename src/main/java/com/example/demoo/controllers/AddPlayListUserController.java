package com.example.demoo.controllers;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.PlayListDetail;
import com.example.demoo.models.Track;
import com.example.demoo.models.User;
import com.example.demoo.services.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlView("/fxml/addPlayList.fxml")
@Component
public class AddPlayListUserController implements Initializable {

    @Autowired
    private TrackService trackService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private PlayListDetailService playListDetailService;

    @Autowired
    private UserService userService;

    @FXML
    private Button addPlayList;

    @FXML
    private TableColumn<PlayList, Long> idPlayList;
    @FXML
    private TableColumn<PlayList, String> playListName;

    @FXML
    private TableColumn<TrackDto, Long> idTrack;
    @FXML
    private TableColumn<TrackDto, String> trackGenre;

    @FXML
    private TableColumn<TrackDto, String> trackName;

    @FXML
    private TableColumn<TrackDto, String> trackSinger;

    @Autowired
    private FXMLSceneManager sceneManager;

    @FXML
    private Button addSong;

    @FXML
    private Button back;

    @FXML
    private TextField playList;

    @FXML
    private TableView<PlayList> playListTableView;

    @FXML
    private Button showPlayList;

    @FXML
    private Button showTracksInPl;

    @FXML
    private ChoiceBox<Track> songChoiceBox;

    @FXML
    private TableView<TrackDto> tracksTableView;

    @Autowired
    private SingerService singerService;

    @Autowired
    private GenreService genreService;

    public  User user;
    @Autowired
    private UserSession userSession; // Получаем доступ к сессии

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchSceneWithController("/fxml/user.fxml", UserController.class, controller -> {
            controller.settUser(userSession.getUser());
        });
    }

    @FXML
    void showPlayList(ActionEvent event) {
        playListTableView.setItems(FXCollections.observableArrayList(playListService.findByUser(userSession.getUser())));
    }

    @FXML
    void showTracks(ActionEvent event) {
        PlayList pl = playListTableView.getSelectionModel().getSelectedItem();
        idTrack.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        trackGenre.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getGenre()));
        trackName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        trackSinger.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getSingerName()));
        tracksTableView.setItems(FXCollections.observableArrayList(playListDetailService.findByPlayList(pl)));
    }

    public void refreshData() {
        User user = userSession.getCurrentUser();
        if (user != null) {
            playListTableView.setItems(FXCollections.observableArrayList(
                    playListService.findByUser(user)
            ));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = userSession.getCurrentUser();
        if (user == null) {
            System.out.println("User not found in session!");
        } else {
            playListTableView.setItems(FXCollections.observableArrayList(
                    playListService.findByUser(user)
            ));
        }
        playListName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        songChoiceBox.setItems(FXCollections.observableArrayList(trackService.loadTracksFromDb()));

        songChoiceBox.setConverter(new StringConverter<Track>() {
            @Override
            public String toString(Track track) {
                return track != null ? track.getTitle() : "";
            }

            @Override
            public Track fromString(String s) {
                return songChoiceBox.getItems().stream()
                        .filter(singer -> singer.getTitle().equals(s))
                        .findFirst()
                        .orElse(null);
            }
        });

    }
    @FXML
    void addPlayList(ActionEvent event) {
        String title = playList.getText().trim();
        if (title.isEmpty()) {
            showAlert("Ошибка", "Название плейлиста не может быть пустым");
            return;
        }

        User currentUser = userSession.getCurrentUser();

        if (playListService.existsByTitleAndUser(title, currentUser)) {
            showAlert("Ошибка", "У вас уже есть плейлист с таким названием");
            return;
        }

        PlayList playListt = new PlayList();
        playListt.setTitle(title);
        playListt.setUser(currentUser);
        playListService.save(playListt);

        playListTableView.setItems(FXCollections.observableArrayList(playListService.findByUser(currentUser)));
    }

    @FXML
    void addSong(ActionEvent event) {
        Track track = songChoiceBox.getValue();
        PlayList pl = playListTableView.getSelectionModel().getSelectedItem();

        if (pl == null) {
            showAlert("Ошибка", "Выберите плейлист для добавления трека");
            return;
        }

        if (track == null) {
            showAlert("Ошибка", "Выберите трек для добавления в плейлист");
            return;
        }

        if (playListDetailService.existsByPlayListAndTrack(pl, track)) {
            showAlert("Ошибка", "Этот трек уже добавлен в данный плейлист");
            return;
        }

        PlayListDetail playListDetail = new PlayListDetail();
        playListDetail.setPlayList(pl);
        playListDetail.setTrack(track);
        playListDetailService.save(playListDetail);
        tracksTableView.setItems(FXCollections.observableArrayList(playListDetailService.findByPlayList(pl)));
    }

    @FXML
    void deletePlayList(ActionEvent event) {
        PlayList pl = playListTableView.getSelectionModel().getSelectedItem();
        playListService.delete(pl);
        playListTableView.setItems(FXCollections.observableArrayList(playListService.findByUser(userSession.getUser())));
    }

    @FXML
    void deleteSong(ActionEvent event) {
        PlayList pl = playListTableView.getSelectionModel().getSelectedItem();
        TrackDto trackDto =  tracksTableView.getSelectionModel().getSelectedItem();
        Track track = trackService.findByTitleAndSinger(trackDto.getTitle(), singerService.findBySingerName((trackDto.getSingerName()))).get(0);
        playListDetailService.delete(track,pl);
        tracksTableView.setItems(FXCollections.observableArrayList(playListDetailService.findByPlayList(pl)));

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUser(User user) {
        this.user = user;
    }

}
