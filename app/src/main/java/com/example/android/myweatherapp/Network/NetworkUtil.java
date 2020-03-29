package com.example.android.myweatherapp.Network;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android.myweatherapp.Model.TemperatureModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkUtil implements Callback<JsonElement> {

    private final String TAG = "NetworkUtil";

    private final String API_KEY = "aaea007c8ae69c5d3e62021bc265b1cd";
    private String BASE_URl = "https://api.openweathermap.org/data/2.5/";
    private final double KELVIN_TEMP = 273.15;
    private WeatherAPI weatherApi;
    private String mCityName;
    private WeakReference<MutableLiveData<TemperatureModel>> currentTempWeakRef;
    private WeakReference<MutableLiveData<List<TemperatureModel>>> weatherListWeakRef;
    String[] DAYS_IN_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public NetworkUtil(MutableLiveData<TemperatureModel> currentTemperature, MutableLiveData<List<TemperatureModel>> weatherList) {
        currentTempWeakRef = new WeakReference<>(currentTemperature);
        weatherListWeakRef = new WeakReference<>(weatherList);
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
        mCityName = cityName;
        Call<JsonElement> call = weatherApi.getCurrentWeatherTemperature(mCityName, API_KEY);
        call.enqueue(this);
    }

    public void fetchTemperatureForecast(String cityName) {
        Call<JsonElement> call = weatherApi.getWeatherForecast(cityName, API_KEY);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
        if(response.isSuccessful()) {
            JsonElement result = response.body();
            if (result.getAsJsonObject().has("cnt")) {
                Set<String> dateSet = new HashSet<>();
                List<TemperatureModel> weatherList = new ArrayList<>();

                JsonArray list = result.getAsJsonObject().get("list").getAsJsonArray();
                for(JsonElement content : list) {
                    long dateInMillis = content.getAsJsonObject().get("dt").getAsLong() * 1000;
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
                    String date = dateFormat.format(new Date(dateInMillis));
                    if(!dateSet.contains(date)) {
                        dateSet.add(date);
                        TemperatureModel temperatureModel = createTemperatureModel(content);
                        weatherList.add(temperatureModel);
                    }
                }
                weatherListWeakRef.get().postValue(weatherList);
                Log.d(TAG, "onResponse: weatherList = "+weatherList.size());
            } else {

                TemperatureModel temperatureModel = createTemperatureModel(result);
                currentTempWeakRef.get().postValue(temperatureModel);
            }
        } else {
            Log.d(TAG, "onResponse: failure= "+response.errorBody());
            currentTempWeakRef.get().postValue(null);
        }
    }

    @Override
    public void onFailure(Call<JsonElement> call, Throwable t) {
        Log.d(TAG, "onFailure: ");
        currentTempWeakRef.get().postValue(null);
    }

    public TemperatureModel createTemperatureModel(JsonElement result) {
        long dateInMillis = result.getAsJsonObject().get("dt").getAsLong() * 1000;
        Date date = new Date(dateInMillis);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String day = DAYS_IN_WEEK[dayOfWeek];

        double temperature = result.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble() - KELVIN_TEMP;
        double maxTemperature = result.getAsJsonObject().get("main").getAsJsonObject().get("temp_max").getAsDouble() - KELVIN_TEMP;
        double minTemperature = result.getAsJsonObject().get("main").getAsJsonObject().get("temp_min").getAsDouble() - KELVIN_TEMP;


        JsonElement weather = result.getAsJsonObject().get("weather").getAsJsonArray().get(0);
        String weatherName = weather.getAsJsonObject().get("main").getAsString();
        String weatherDesc = weather.getAsJsonObject().get("description").getAsString();
        String weatherIcon = weather.getAsJsonObject().get("icon").getAsString();

        TemperatureModel temperatureModel = new TemperatureModel(Math.round(temperature), maxTemperature, minTemperature, weatherName, weatherDesc, weatherIcon, day);
        return temperatureModel;
    }
}
