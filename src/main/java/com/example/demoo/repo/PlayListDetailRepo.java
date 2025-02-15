package com.example.demoo.repo;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.PlayListDetail;
import com.example.demoo.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListDetailRepo extends JpaRepository<PlayListDetail, Long> {
    List<PlayListDetail> findByPlayList(PlayList pl);

    @Query(value = "SELECT t.id, t.title, s.singername, g.name " +
            "FROM track t " +
            "JOIN singer s ON t.singerid = s.id " +
            "JOIN playlistdetail pd ON pd.trackid = t.id " +
            "JOIN genre g ON g.id = t.genre " +
            "WHERE pd.playlistid = :id", nativeQuery = true)
    List<TrackDto> findTrackDtosByPlayList(@Param("id") Long id);

    boolean existsByPlayListAndTrack(PlayList pl, Track track);

    void deleteByTrackAndPlayList(Track track, PlayList pl);
}
