package com.example.demoo.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FXMLSceneManager {
    @Setter
    private Stage primaryStage;
    private final ApplicationContext context;

    public FXMLSceneManager(ApplicationContext context) {
        this.context = context;
    }

    public void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean);
            loader.setLocation(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (primaryStage == null) {
                System.out.println("Error: primaryStage is null!");
            } else {
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace
            throw new RuntimeException(e);
        }
    }


}
