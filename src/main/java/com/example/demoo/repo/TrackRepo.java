package com.example.demoo.repo;

import com.example.demoo.models.Genre;
import com.example.demoo.models.Singer;
import com.example.demoo.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepo extends JpaRepository<Track, Long> {

    List<Track> findByTitleAndSinger(String title, Singer singer);
}
