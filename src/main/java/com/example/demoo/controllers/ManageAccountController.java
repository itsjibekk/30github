package com.example.demoo.controllers;

import com.example.demoo.models.User;
import com.example.demoo.services.UserService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.demoo.controllers.AddPlayListUserController.user;

@Component
@FxmlView("/fxml/manageAccount.fxml")
public class ManageAccountController implements Initializable {

    @Autowired
    private FXMLSceneManager sceneManager;

    @Autowired
    private UserService userService;

    @FXML
    private Button addUser;

    @FXML
    private Button back;

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, Long> userId;

    @FXML
    private TableColumn<User, Button> useraction;

    @FXML
    private TableColumn<User, String> username;

    @FXML
    private TableColumn<User, String> userrole;

    @FXML
    void addUser(ActionEvent event) {
        sceneManager.switchScene("/fxml/addUser.fxml");
    }

    @FXML
    void goBack(ActionEvent event) {
        sceneManager.switchScene("/fxml/admin.fxml");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userId.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));

        username.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        userrole.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getUserType().getName()));

        useraction.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Обновить");
            private final Button deleteButton = new Button("Удалить");

            {
                updateButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    updateUser(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
                updateButton.setStyle("-fx-background-color:  #f6b2ff; -fx-text-fill: black;");
                deleteButton.setStyle("-fx-background-color:  #f6b2ff; -fx-text-fill: black;");
                updateButton.getStyleClass().add("glow-button");
                deleteButton.getStyleClass().add("glow-button");
                usersTableView.getScene().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, updateButton, deleteButton));
                }
            }
        });

        usersTableView.setItems(FXCollections.observableArrayList(userService.findAll()));
    }
    private void updateUser(User user) {
        System.out.println("Обновление пользователя: " + user.getUsername());
        sceneManager.switchSceneWithController("/fxml/updateUser.fxml", UpdateUserController.class, controller -> {
            controller.setUser(user);
        });
    }

    @FXML
    public void showPlaylist(){
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            sceneManager.switchSceneWithController("/fxml/showPlaylistUser.fxml", ShowPlayListUserController.class, controller -> {
                controller.settUser(selectedUser);
            });
        } else {
            System.out.println("Ошибка: пользователь не выбран!");
        }
    }

    private void deleteUser(User user) {
        userService.delete(user);
        usersTableView.getItems().remove(user);
        System.out.println("Удален пользователь: " + user.getUsername());
    }
}
