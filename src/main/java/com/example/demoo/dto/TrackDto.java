package com.example.demoo.dto;

import org.springframework.stereotype.Component;


public class TrackDto {

    private String title;

    private String SingerName;

    public TrackDto(String title, String singerName) {
        this.title = title;
        SingerName = singerName;
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
