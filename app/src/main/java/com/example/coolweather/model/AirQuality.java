package com.example.coolweather.model;

public class AirQuality {
    private String aqi;

    private String PM;

    public AirQuality(String aqi, String PM) {
        this.aqi = aqi;
        this.PM = PM;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPM() {
        return PM;
    }

    public void setPM(String PM) {
        this.PM = PM;
    }
}
