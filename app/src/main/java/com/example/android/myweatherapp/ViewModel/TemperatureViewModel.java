package com.example.android.myweatherapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.android.myweatherapp.Model.TemperatureModel;
import com.example.android.myweatherapp.Network.NetworkUtil;

import java.util.List;
import android.os.Handler;

public class TemperatureViewModel extends AndroidViewModel {

    private final NetworkUtil networkUtil;

    public TemperatureViewModel(@NonNull Application application) {
        super(application);
        networkUtil = new NetworkUtil(currentTemperature, weatherList);
    }

    MutableLiveData<TemperatureModel> currentTemperature = new MutableLiveData<>();

    MutableLiveData<List<TemperatureModel>> weatherList = new MutableLiveData<>();

    public MutableLiveData<TemperatureModel> getCurrentTemperature() {
        return currentTemperature;
    }

    public MutableLiveData<List<TemperatureModel>> getWeatherList() {
        return weatherList;
    }

    public void fetchCurrentTemperature(final String cityName) {
        networkUtil.fetchCurrentWeatherTemperature(cityName);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                networkUtil.fetchTemperatureForecast(cityName);
            }
        }, 100);
    }
}
