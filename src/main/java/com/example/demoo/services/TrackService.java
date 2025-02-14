package com.example.demoo.services;

import com.example.demoo.models.Singer;
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

    public void save(Track track) {
        trackRepo.save(track);
    }

    public List<Track> findByTitleAndSinger(String title, Singer singer) {
        return trackRepo.findByTitleAndSinger(title,singer);
    }

    public void delete(Track selectedItem) {
        trackRepo.delete(selectedItem);
    }


    public void update(Track track) {
       trackRepo.save(track);

    }


}
