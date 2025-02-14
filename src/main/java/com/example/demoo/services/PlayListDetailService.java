package com.example.demoo.services;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.PlayListDetail;
import com.example.demoo.models.Track;
import com.example.demoo.repo.PlayListDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayListDetailService {

    @Autowired
    private PlayListDetailRepo playListDetailRepo;
    public List<TrackDto> findByPlayList(PlayList pl) {
        return playListDetailRepo.findTrackDtosByPlayList(pl.getId());
    }

    public void save(PlayListDetail pl) {
        playListDetailRepo.save(pl);
    }

    public boolean existsByPlayListAndTrack(PlayList pl, Track track) {
        return playListDetailRepo.existsByPlayListAndTrack(pl,track);
    }
}
