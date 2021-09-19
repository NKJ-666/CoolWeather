package com.example.coolweather.other;

import com.example.coolweather.model.AirQuality;
import com.example.coolweather.model.ForecastWeather;
import com.example.coolweather.model.NowWeather;
import com.example.coolweather.model.Suggestion;
import com.example.coolweather.model.Title;

import java.util.List;

public interface LoadListener {
    void loadTitle(Title title);
    void loadNowWeather(NowWeather nowWeather);
    void loadForecastWeather(List<ForecastWeather> forecastWeathers);
    void loadAirQuality(AirQuality airQuality);
    void loadSuggestion(Suggestion suggestion);
    void loadFailed(String message);
}
