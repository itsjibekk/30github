package com.example.demoo.services;

import com.example.demoo.models.PlayList;
import com.example.demoo.models.PlayListDetail;
import com.example.demoo.repo.PlayListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayListService {
    @Autowired
    private PlayListRepo playListRepo;
    public List<PlayList> loadAll() {
        return playListRepo.findAll();
    }
}
