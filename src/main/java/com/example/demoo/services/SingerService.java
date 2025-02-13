package com.example.demoo.services;

import com.example.demoo.models.Singer;
import com.example.demoo.repo.SingerRepo;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SingerService {

    @Autowired
    private SingerRepo singerRepo;
    public List<Singer> getAll() {
        return singerRepo.findAll();
    }

    public void save(Singer singer) {
        singerRepo.save(singer);
    }
}
