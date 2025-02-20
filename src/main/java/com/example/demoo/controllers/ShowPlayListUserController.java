package com.example.demoo.controllers;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.Track;
import com.example.demoo.models.User;
import com.example.demoo.services.PlayListDetailService;
import com.example.demoo.services.PlayListService;
import com.example.demoo.services.UserService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("showPlaylistUser.fxml")
public class ShowPlayListUserController implements Initializable {

    public  User user;
    public void settUser(User user) {
        this.user = user;
        if (userName != null) {
            userName.setText(user.getUsername());
        }
        if (playListTableView != null) {
            playListTableView.setItems(FXCollections.observableArrayList(playListService.findByUser(user)));
        }
    }


    @FXML
    private Button back;

    @FXML
    private TableView<PlayList> playListTableView;

    @FXML
    private TableColumn<PlayList, String> playlistname;

    @FXML
    private Button report;

    @FXML
    private Label userName;

    @Autowired
    FXMLSceneManager sceneManager;
    @Autowired
    private PlayListService playListService;
    @Autowired
    private PlayListDetailService playListDetailService;
    @Autowired
    private UserService userService;

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/manageAccount.fxml");
    }

    @FXML
    void makeReport(ActionEvent event) {
        if (user == null) {
            System.out.println("Ошибка: пользователь не выбран!");
            return;
        }

        // Создаем документ
        XWPFDocument document = new XWPFDocument();
        try {
            FileOutputStream out = new FileOutputStream("UserPlaylistsReport.docx");

            // Заголовок документа
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Отчет по плейлистам пользователя: " + user.getUsername());
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.addBreak();

            // Получаем плейлисты пользователя
            List<PlayList> playlists = playListService.findByUser(user);

            for (PlayList playlist : playlists) {
                // Добавляем название плейлиста
                XWPFParagraph playlistParagraph = document.createParagraph();
                XWPFRun playlistRun = playlistParagraph.createRun();
                playlistRun.setText("Плейлист: " + playlist.getTitle());
                playlistRun.setBold(true);
                playlistRun.setFontSize(14);
                playlistRun.addBreak();

                // Создаем таблицу для треков
                XWPFTable table = document.createTable();
                XWPFTableRow tableHeader = table.getRow(0);
                tableHeader.getCell(0).setText("№");
                tableHeader.addNewTableCell().setText("Название трека");
                tableHeader.addNewTableCell().setText("Исполнитель");

                // Получаем треки в плейлисте
                List<TrackDto> tracks = playListDetailService.findByPlayList(playlist);

                for (int i = 0; i < tracks.size(); i++) {
                    TrackDto track = tracks.get(i);
                    XWPFTableRow tableRow = table.createRow();
                    tableRow.getCell(0).setText(String.valueOf(i + 1));
                    tableRow.getCell(1).setText(track.getTitle());
                    tableRow.getCell(2).setText(track.getSingerName());
                }

                playlistRun.addBreak();
            }

            document.write(out);
            out.close();
            document.close();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Отчет успешно создан!");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playlistname.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
    }

}
