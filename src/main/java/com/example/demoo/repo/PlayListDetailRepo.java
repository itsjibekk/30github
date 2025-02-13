package com.example.demoo.repo;

import com.example.demoo.dto.TrackDto;
import com.example.demoo.models.PlayList;
import com.example.demoo.models.PlayListDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListDetailRepo extends JpaRepository<PlayListDetail, Long> {
    List<PlayListDetail> findByPlayList(PlayList pl);

    @Query(value = "select t.title , s.singername\n" +
            "from track as t join singer as s on t.singerid = s.id join playlistdetail as pd on pd.trackid = t.id\n" +
            "where pd.playlistid = :id;", nativeQuery = true)
    List<TrackDto> findTrackDtosByPlayList(Long id);
}
