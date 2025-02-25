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
        if (primaryStage == null) {
            System.out.println("Error: primaryStage is null!");
            return; // Прерываем выполнение, если primaryStage не установлен
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean);
            loader.setLocation(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> void switchhSceneWithController(String fxmlPath, Class<T> controllerClass)  {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource(fxmlPath));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        T controller = loader.getController();

        if (controller instanceof AddPlayListUserController) {
            ((AddPlayListUserController) controller).refreshData();
        }
    }

    public <T> void switchSceneWithController(String fxmlPath, Class<T> controllerClass, ControllerInitializer<T> initializer) {
        if (primaryStage == null) {
            System.out.println("Error: primaryStage is null!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean);
            loader.setLocation(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Получаем контроллер и передаем данные через initializer
            T controller = loader.getController();
            initializer.init(controller);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Функциональный интерфейс для передачи данных контроллеру
     */
    @FunctionalInterface
    public interface ControllerInitializer<T> {
        void init(T controller);
    }
}
