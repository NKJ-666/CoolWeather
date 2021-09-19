package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.coolweather.viewModel.MainViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public MainViewModel viewModel;

    public ScrollView weatherLayout;

    public TextView  titleCity;

    public TextView titleUpdateTime;

    public TextView degreeText;

    public TextView weatherInfoText;

    public LinearLayout forecastLayout;

    public TextView aqiText;

    public TextView comfortText;

    public TextView carWashText;

    public TextView sportText;

    public TextView pm25Text;

    public ImageView bingPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() throws InterruptedException {
        viewModel = new MainViewModel(this);
        weatherInfoText = findViewById(R.id.weather_info_text);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherLayout = findViewById(R.id.weather_layout);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        pm25Text = findViewById(R.id.pm25_text);
        sportText = findViewById(R.id.sport_text);
        bingPic = findViewById(R.id.bingPic);
        viewModel.loadAll();
    }
}