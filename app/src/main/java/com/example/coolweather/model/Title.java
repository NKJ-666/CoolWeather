package com.example.coolweather.model;

public class Title {
    private String cityName;

    private String currentTime;

    public Title(String cityName, String currentTime){
        this.cityName = cityName;
        this.currentTime = currentTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
