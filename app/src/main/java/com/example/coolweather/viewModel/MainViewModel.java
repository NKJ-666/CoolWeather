package com.example.coolweather.viewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.coolweather.view.MainActivity;
import com.example.coolweather.R;
import com.example.coolweather.model.AirQuality;
import com.example.coolweather.model.Data;
import com.example.coolweather.model.ForecastWeather;
import com.example.coolweather.model.NowWeather;
import com.example.coolweather.model.Suggestion;
import com.example.coolweather.model.Title;
import com.example.coolweather.other.LoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel {
    private MainActivity activity;

    private Data data;

    private SharedPreferences sharedPreferences;

    private Suggestion suggestion1;

    public MainViewModel(MainActivity activity) {
        this.activity = activity;
        init();
    }

    private void init(){
        activity.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAll(true);
                activity.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadAll(boolean update){
        sharedPreferences = activity.getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        String bingPic = sharedPreferences.getString("bing_pic", null);
        if (bingPic != null){
            Glide.with(activity).load(bingPic).into(activity.bingPic);
        } else {
            loadBingPic();
        }
        if(!update && sharedPreferences.getString("currentTime",null) != null){
            loadFromPref();
        }else{
            data = new Data(activity);
            data.loadAll(new LoadListener() {
                @Override
                public void loadTitle(Title title) {
                    String titleName = title.getCityName();
                    String currentTime = title.getCurrentTime();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("cityName", titleName);
                    editor.putString("currentTime", currentTime);
                    editor.apply();
                    activity.titleCity.setText(titleName);
                    activity.titleUpdateTime.setText(currentTime);
                }

                @Override
                public void loadNowWeather(NowWeather nowWeather) {
                    String temperature = nowWeather.getTemperature() + "℃";
                    String weatherText = nowWeather.getWeatherText();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("temperature", temperature);
                    editor.putString("weatherText", weatherText);
                    editor.apply();
                    activity.degreeText.setText(temperature);
                    activity.weatherInfoText.setText(weatherText);
                }

                @Override
                public void loadForecastWeather(List<ForecastWeather> forecastWeathers) {
                    activity.forecastLayout.removeAllViews();
                    int i = 0;
                    for(ForecastWeather forecastWeather : forecastWeathers){
                        View view = LayoutInflater.from(activity).inflate(R.layout.forecast_item, activity.forecastLayout, false);
                        TextView dateText = view.findViewById(R.id.date_text);
                        TextView infoText = view.findViewById(R.id.info_text);
                        TextView maxText = view.findViewById(R.id.max_text);
                        TextView minText = view.findViewById(R.id.min_text);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String dateText1 = forecastWeather.getTime();
                        String infoText1 = forecastWeather.getWeatherText();
                        String max = forecastWeather.getMaxTemperature();
                        String min = forecastWeather.getMinTemperature();
                        editor.putString("dateText" + i, dateText1);
                        editor.putString("infoText" + i, infoText1);
                        editor.putString("max" + i, max);
                        editor.putString("min" + i, min);
                        editor.apply();
                        dateText.setText(dateText1);
                        infoText.setText(infoText1);
                        maxText.setText(max);
                        minText.setText(min);
                        activity.forecastLayout.addView(view);
                        i++;
                    }
                }

                @Override
                public void loadAirQuality(AirQuality airQuality) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String aqiText = airQuality.getAqi();
                    String pm25Text = airQuality.getPM();
                    editor.putString("aqi", aqiText);
                    editor.putString("pm25", pm25Text);
                    editor.apply();
                    activity.aqiText.setText(aqiText);
                    activity.pm25Text.setText(pm25Text);
                }

                @Override
                public void loadSuggestion(Suggestion suggestion) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String comfortLevel = suggestion.getComfortLevel();
                    String exerciseLevel = suggestion.getExerciseLevel();
                    String carWashLevel = suggestion.getClearCarLevel();
                    editor.putString("comfortLevel", comfortLevel);
                    editor.putString("sportLevel", exerciseLevel);
                    editor.putString("carWashLevel", carWashLevel);
                    editor.apply();
                    activity.comfortText.setText(comfortLevel);
                    activity.sportText.setText(exerciseLevel);
                    activity.carWashText.setText(carWashLevel);
                }

                @Override
                public void loadFailed(String message) {
                    Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadFromPref(){
        activity.titleCity.setText(sharedPreferences.getString("cityName", null));
        activity.titleUpdateTime.setText(sharedPreferences.getString("currentTime", null));
        activity.degreeText.setText(sharedPreferences.getString("temperature", null));
        activity.weatherInfoText.setText(sharedPreferences.getString("weatherText", null));
        activity.forecastLayout.removeAllViews();
        for(int i = 0; i < 3; i++){
            View view = LayoutInflater.from(activity).inflate(R.layout.forecast_item, activity.forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(sharedPreferences.getString("dateText" + i, null));
            infoText.setText(sharedPreferences.getString("infoText" + i, null));
            maxText.setText(sharedPreferences.getString("max" + i,null));
            minText.setText(sharedPreferences.getString("min" + i, null));
            activity.forecastLayout.addView(view);
        }
        activity.aqiText.setText(sharedPreferences.getString("aqi", null));
        activity.pm25Text.setText(sharedPreferences.getString("pm25", null));
        activity.comfortText.setText(sharedPreferences.getString("comfortLevel", null));
        activity.sportText.setText(sharedPreferences.getString("sportLevel", null));
        activity.carWashText.setText(sharedPreferences.getString("carWashLevel", null));
        loadBingPic();
    }

    private void loadBingPic(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String bingPicUrl = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(bingPicUrl)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            JSONArray array = object.getJSONArray("images");
                            String bingPic = array.getJSONObject(0).getString("url");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(activity).load("https://www.bing.com/" + bingPic).into(activity.bingPic);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("bing_pic", "https://www.bing.com/" + bingPic);
                                    editor.apply();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}
