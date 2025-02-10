package com.example.demoo;

import com.example.demoo.controllers.LoginController;
import com.example.demoo.controllers.TrackController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(DemooApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(LoginController.class);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error during application startup: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

}
