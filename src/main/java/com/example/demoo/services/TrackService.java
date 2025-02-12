package com.example.demoo.services;

import com.example.demoo.models.Track;
import com.example.demoo.repo.TrackRepo;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepo trackRepo;

    public TrackService(TrackRepo trackRepo) {
        this.trackRepo = trackRepo;
    }

    public String getWeatherForecast() {
        return "It's gonna snow a lot. Brace yourselves, the winter is coming.";
    }

    public List<Track> loadTracksFromDb() {
       return trackRepo.findAll();
    }
}
