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
import javafx.stage.FileChooser;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
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

        // Открываем диалог выбора пути и имени файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить отчет");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Документ Word", "*.docx"));
        fileChooser.setInitialFileName("UserPlaylistsReport.docx"); // Имя по умолчанию

        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            System.out.println("Сохранение отменено");
            return;
        }

        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(file)) {
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
            StringBuilder reportContent = new StringBuilder();

            for (PlayList playlist : playlists) {
                reportContent.append("Плейлист: ").append(playlist.getTitle()).append("\n");

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

                    reportContent.append(i + 1).append(". ")
                            .append(track.getTitle()).append(" - ")
                            .append(track.getSingerName()).append("\n");
                }
                playlistRun.addBreak();
            }

            document.write(out);
            document.close();

            saveReportToDatabase(user.getId(), reportContent.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Отчет успешно сохранен в: " + file.getAbsolutePath());
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при сохранении отчета!");
            alert.show();
        }
    }

    private void saveReportToDatabase(Long userId, String reportData) {
        String url = "jdbc:postgresql://localhost:5432/music_db"; // Замените на ваш URL
        String username = "postgres"; // Замените на ваш логин
        String password = "postgres"; // Замените на ваш пароль

        String insertReportSQL = "INSERT INTO reports (user_id, report_type_id, created_at) VALUES (?, ?, NOW()) RETURNING id";
        String insertReportDetailsSQL = "INSERT INTO report_details (report_id, data) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            conn.setAutoCommit(false); // Включаем транзакцию

            // 1. Вставляем отчет в reports и получаем его ID
            Long reportId;
            try (PreparedStatement reportStmt = conn.prepareStatement(insertReportSQL, Statement.RETURN_GENERATED_KEYS)) {
                reportStmt.setLong(1, userId);
                reportStmt.setLong(2, 2); // ID типа отчета (можно параметризовать)
                reportStmt.executeUpdate();

                try (ResultSet generatedKeys = reportStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reportId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Не удалось получить ID созданного отчета.");
                    }
                }
            }

            // 2. Вставляем данные отчета в report_details
            try (PreparedStatement detailsStmt = conn.prepareStatement(insertReportDetailsSQL)) {
                detailsStmt.setLong(1, reportId);
                detailsStmt.setString(2, reportData);
                detailsStmt.executeUpdate();
            }

            conn.commit(); // Завершаем транзакцию
            System.out.println("Отчет успешно сохранен в БД с ID: " + reportId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при сохранении отчета в БД");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playlistname.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
    }
}
