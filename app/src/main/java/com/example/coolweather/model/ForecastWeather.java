package com.example.coolweather.model;

public class ForecastWeather {
    private String time;

    private String weatherText;

    private String maxTemperature;

    private String minTemperature;

    public ForecastWeather(String time, String weatherText, String maxTemperature, String minTemperature) {
        this.time = time;
        this.weatherText = weatherText;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }
}
