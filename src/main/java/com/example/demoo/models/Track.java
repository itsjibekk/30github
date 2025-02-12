package com.example.demoo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "singerid", nullable = false)
    private Singer singer;

    private String title;

    @ManyToOne
    @JoinColumn(name = "genre", nullable = false)
    private Genre genre;

    public String getName() {
        return this.title;
    }
}
