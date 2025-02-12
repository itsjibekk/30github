package com.example.demoo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "playlistdetail")
public class PlayListDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlistid", nullable = false)
    private PlayList playList;

    @ManyToOne
    @JoinColumn(name = "trackid", nullable = false)
    private Track track;

}
