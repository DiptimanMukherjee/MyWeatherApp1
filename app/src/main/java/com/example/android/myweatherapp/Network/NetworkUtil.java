package com.example.android.myweatherapp.Network;

import android.util.Log;

import com.example.android.myweatherapp.Model.TemperatureModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkUtil implements Callback<JsonElement> {

    private final String TAG = "NetworkUtil";

    private final String API_KEY = "aaea007c8ae69c5d3e62021bc265b1cd";
    private String BASE_URl = "https://api.openweathermap.org/data/2.5/";
    private final WeatherAPI weatherApi;

    public NetworkUtil() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        weatherApi = retrofit.create(WeatherAPI.class);

    }


    public void fetchCurrentWeatherTemperature(String cityName) {
        Call<JsonElement> call = weatherApi.getCurrentWeatherTemperature(cityName, API_KEY);
        call.enqueue(this);
    }





    @Override
    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
        if(response.isSuccessful()) {
           // Log.d(TAG, "onResponse: success = "+response.body());
            JsonElement result = response.body();
            float temperature = (float) (result.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsFloat() - 273.15);
            float maxTemperature = result.getAsJsonObject().get("main").getAsJsonObject().get("temp_max").getAsFloat();
            float minTemperature = result.getAsJsonObject().get("main").getAsJsonObject().get("temp_min").getAsFloat();

            JsonElement weather = result.getAsJsonObject().get("weather").getAsJsonArray().get(0);
            String weatherName = weather.getAsJsonObject().get("main").getAsString();
            String weatherDesc = weather.getAsJsonObject().get("description").getAsString();
            String weatherIcon = weather.getAsJsonObject().get("icon").getAsString();

            TemperatureModel temperatureModel = new TemperatureModel(Math.round(temperature), maxTemperature, minTemperature, weatherName, weatherDesc, weatherIcon);
            Log.d(TAG, "onResponse: success temperature = "+Math.round(temperature));
        } else {
            Log.d(TAG, "onResponse: failure= "+response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<JsonElement> call, Throwable t) {
        Log.d(TAG, "onFailure: ");
    }
}
