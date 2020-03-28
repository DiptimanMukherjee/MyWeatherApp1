package com.example.android.myweatherapp.Network;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<JsonElement> getCurrentWeatherTemperature(@Query("q") String cityName,@Query("appid") String apiKey);
}
