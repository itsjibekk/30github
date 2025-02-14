package com.example.demoo.dto;

import com.example.demoo.models.Track;
import org.springframework.stereotype.Component;


public class TrackDto {

    private String title;

    private String SingerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    private String genre;

    public TrackDto( Long id,String title, String singerName, String genre) {
        this.id = id;
        this.title = title;
        SingerName = singerName;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingerName() {
        return SingerName;
    }

    public void setSingerName(String singerName) {
        SingerName = singerName;
    }
}
