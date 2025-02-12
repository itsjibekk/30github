package com.example.demoo.repo;

import com.example.demoo.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepo extends JpaRepository<Track, Long> {

}
