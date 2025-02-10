package com.example.demoo.controllers;

import com.example.demoo.services.TrackService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("track.fxml")
public class TrackController {
    private TrackService weatherService;

    @FXML
    private Label weatherLabel;

    @Autowired
    public TrackController(TrackService weatherService) {
        this.weatherService = weatherService;
    }
    public void loadWeatherForecast(ActionEvent actionEvent) {
        this.weatherLabel.setText(weatherService.getWeatherForecast());
    }
}
