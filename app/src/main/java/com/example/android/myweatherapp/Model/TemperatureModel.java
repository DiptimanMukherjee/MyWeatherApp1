package com.example.android.myweatherapp.Model;

public class TemperatureModel {
    private float temperature;
    private float maxTemperature;
    private float minTemperature;
    private String weatherName;
    private String weatherDescription;
    private String weatherIcon;

    public TemperatureModel(float currentTemp, float max, float min, String weatherName, String weatherDescription, String weatherIcon) {
        this.temperature = currentTemp;
        this.maxTemperature = max;
        this.minTemperature = min;
        this.weatherName = weatherName;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public String getWeatherName() {
        return weatherName;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }
}
