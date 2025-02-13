package com.example.demoo.services;

import com.example.demoo.models.Genre;
import com.example.demoo.repo.GenreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepo genreRepo;
    public List<Genre> getAll() {
        return genreRepo.findAll();
    }

    public Genre findByName(String name) {
        return genreRepo.findByName(name);
    }

    public void save(Genre genre) {
        genreRepo.save(genre);
    }
}
