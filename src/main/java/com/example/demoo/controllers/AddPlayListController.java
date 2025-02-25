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
import java.util.List;
import java.util.ResourceBundle;

@FxmlView("/fxml/addPlayList.fxml")
@Component
public class AddPlayListController implements Initializable {

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
    private TableColumn<PlayList, String> playListUser;

    @FXML
    private TableColumn<TrackDto, Long> idTrack;

    @FXML
    private TextField searchField;
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

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/admin.fxml");
    }

    @FXML
    void showPlayList(ActionEvent event) {
        playListTableView.setItems(FXCollections.observableArrayList(playListService.loadAll()));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idPlayList.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));

        playListName.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        playListUser.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getUser().getUsername()));

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
        playListTableView.setItems(FXCollections.observableArrayList(playListService.loadAll()));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTracks());
    }

    private void filterTracks() {
        String searchText = searchField.getText().toLowerCase().trim();

        List<PlayList> allPlayLists = playListService.loadAll();

        List<PlayList> filteredPlayLists = allPlayLists.stream()
                .filter(playList -> {
                    if (searchText.isEmpty()) {
                        return true;
                    }
                    return playList.getTitle().toLowerCase().contains(searchText) ||
                            playList.getUser().getUsername().toLowerCase().contains(searchText);
                })
                .toList();

        playListTableView.setItems(FXCollections.observableArrayList(filteredPlayLists));
    }


    @FXML
    void addPlayList(ActionEvent event) {
        String title = playList.getText().trim();
        if (title.isEmpty()) {
            showAlert("Ошибка", "Название плейлиста не может быть пустым");
            return;
        }

        User currentUser = userService.getByUsername("admin"); // Получаем текущего пользователя

        // Проверяем, есть ли у этого пользователя плейлист с таким же названием
        if (playListService.existsByTitleAndUser(title, currentUser)) {
            showAlert("Ошибка", "У вас уже есть плейлист с таким названием");
            return;
        }

        PlayList playListt = new PlayList();
        playListt.setTitle(title);
        playListt.setUser(currentUser);
        playListService.save(playListt);

        // Обновляем таблицу
        playListTableView.setItems(FXCollections.observableArrayList(playListService.loadAll()));
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
        playListTableView.setItems(FXCollections.observableArrayList(playListService.loadAll()));
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

}
