package com.example.coolweather.model;

public class NowWeather {
    private String temperature;

    private String weatherText;

    public NowWeather(String temperature, String weatherText) {
        this.temperature = temperature;
        this.weatherText = weatherText;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }
}
