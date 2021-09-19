package com.example.coolweather.viewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.coolweather.MainActivity;
import com.example.coolweather.R;
import com.example.coolweather.model.AirQuality;
import com.example.coolweather.model.Data;
import com.example.coolweather.model.ForecastWeather;
import com.example.coolweather.model.NowWeather;
import com.example.coolweather.model.Suggestion;
import com.example.coolweather.model.Title;
import com.example.coolweather.other.LoadListener;
import com.example.coolweather.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainViewModel {
    private MainActivity activity;

    private Data data;

    private SharedPreferences sharedPreferences;

    public MainViewModel(MainActivity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("bingPic", Context.MODE_PRIVATE);
    }

    public void loadAll(){
        String bingPic = sharedPreferences.getString("bing_pic", null);
        if (bingPic != null){
            Glide.with(activity).load(bingPic).into(activity.bingPic);
        } else {
            loadBingPic();
        }
        data = new Data(activity);
        data.loadAll(new LoadListener() {
            @Override
            public void loadTitle(Title title) {
                activity.titleCity.setText(title.getCityName());
                activity.titleUpdateTime.setText(title.getCurrentTime());
            }

            @Override
            public void loadNowWeather(NowWeather nowWeather) {
                activity.degreeText.setText(nowWeather.getTemperature() + "℃");
                activity.weatherInfoText.setText(nowWeather.getWeatherText());
            }

            @Override
            public void loadForecastWeather(List<ForecastWeather> forecastWeathers) {
                for(ForecastWeather forecastWeather : forecastWeathers){
                    View view = LayoutInflater.from(activity).inflate(R.layout.forecast_item, activity.forecastLayout, false);
                    TextView dateText = view.findViewById(R.id.date_text);
                    TextView infoText = view.findViewById(R.id.info_text);
                    TextView maxText = view.findViewById(R.id.max_text);
                    TextView minText = view.findViewById(R.id.min_text);
                    dateText.setText(forecastWeather.getTime());
                    infoText.setText(forecastWeather.getWeatherText());
                    maxText.setText(forecastWeather.getMaxTemperature());
                    minText.setText(forecastWeather.getMinTemperature());
                    activity.forecastLayout.addView(view);
                }
            }

            @Override
            public void loadAirQuality(AirQuality airQuality) {
                activity.aqiText.setText(airQuality.getAqi());
                activity.pm25Text.setText(airQuality.getPM());
            }

            @Override
            public void loadSuggestion(Suggestion suggestion) {
                activity.comfortText.setText(suggestion.getComfortLevel());
                activity.sportText.setText(suggestion.getExerciseLevel());
                activity.carWashText.setText(suggestion.getClearCarLevel());
            }

            @Override
            public void loadFailed(String message) {
                Toast.makeText(activity, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
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
