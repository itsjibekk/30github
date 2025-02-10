package com.example.demoo.services;

import org.springframework.stereotype.Service;

@Service
public class TrackService {
    public String getWeatherForecast() {
        return "It's gonna snow a lot. Brace yourselves, the winter is coming.";
    }
}
