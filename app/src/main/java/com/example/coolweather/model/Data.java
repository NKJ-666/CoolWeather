package com.example.coolweather.model;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.coolweather.other.LoadListener;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Data {
    private Context context;

    private LocationClient locationClient;

    private LocationClientOption option;

    private String cityId;

    private String district;

    private Calendar calendar;

    public Data(Context context){
        this.context = context;
        init();
    }

    private void init(){
        locationClient = new LocationClient(context.getApplicationContext());
        option = new LocationClientOption();
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
        HeConfig.init("HE2109171945331354", "b77af25b97f84239ba47365ae68b0d17");
        HeConfig.switchToDevService();
    }

    public void loadAll(LoadListener listener){
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                calendar = Calendar.getInstance();
                district = bdLocation.getDistrict();
                listener.loadTitle(new Title(district, String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE))));
                getWeatherId(district, listener);
            }
        });
    }

    private void getWeatherId(String district, LoadListener listener){
        QWeather.getGeoCityLookup(context, district, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                List<GeoBean.LocationBean> list = geoBean.getLocationBean();
                if (list.size() > 0){
                    cityId = list.get(0).getId();
                }
                QWeather.getWeatherNow(context, cityId, new QWeather.OnResultWeatherNowListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(context, "加载温度失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(WeatherNowBean weatherNowBean) {
                        WeatherNowBean.NowBaseBean bean = weatherNowBean.getNow();
                        listener.loadNowWeather(new NowWeather(bean.getTemp(), bean.getText()));
                    }
                });
                QWeather.getWeather3D(context, cityId, Lang.ZH_HANS, Unit.METRIC,new QWeather.OnResultWeatherDailyListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(WeatherDailyBean weatherDailyBean) {
                        List<WeatherDailyBean.DailyBean> dailyBeans = weatherDailyBean.getDaily();
                        List<ForecastWeather> forecastWeathers = new ArrayList<>();
                        for(WeatherDailyBean.DailyBean dailyBean : dailyBeans){
                            forecastWeathers.add(new ForecastWeather(dailyBean.getFxDate(), dailyBean.getTextDay(), dailyBean.getTempMax(), dailyBean.getTempMin()));
                        }
                        listener.loadForecastWeather(forecastWeathers);
                    }
                });
                QWeather.getAirNow(context, cityId, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(AirNowBean airNowBean) {
                        AirNowBean.NowBean nowBean = airNowBean.getNow();
                        listener.loadAirQuality(new AirQuality(nowBean.getAqi(), nowBean.getPm2p5()));
                    }
                });
                List<IndicesType> indicesTypes = new ArrayList<>();
                indicesTypes.add(IndicesType.CW);
                indicesTypes.add(IndicesType.SPT);
                indicesTypes.add(IndicesType.COMF);
                QWeather.getIndices1D(context, cityId, Lang.ZH_HANS, indicesTypes, new QWeather.OnResultIndicesListener() {
                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(IndicesBean indicesBean) {
                        String comfortLevel;
                        String carWashLevel;
                        String sportLevel;
                        List<IndicesBean.DailyBean> dailyBeans = indicesBean.getDailyList();
                        comfortLevel = dailyBeans.get(2).getName() + ": " + dailyBeans.get(2).getText();
                        carWashLevel = dailyBeans.get(0).getName() + ": " + dailyBeans.get(0).getText();
                        sportLevel = dailyBeans.get(1).getName() + " " + dailyBeans.get(1).getText();
                        listener.loadSuggestion(new Suggestion(comfortLevel, sportLevel, carWashLevel));
                    }
                });
            }
        });
    }
}
