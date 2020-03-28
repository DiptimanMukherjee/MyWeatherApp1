package com.example.android.myweatherapp.Model;

public class TemperatureModel {
    private double temperature;
    private double maxTemperature;
    private double minTemperature;
    private String weatherName;
    private String weatherDescription;
    private String weatherIcon;

    public TemperatureModel(double currentTemp, double max, double min, String weatherName, String weatherDescription, String weatherIcon) {
        this.temperature = currentTemp;
        this.maxTemperature = max;
        this.minTemperature = min;
        this.weatherName = weatherName;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMinTemperature() {
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
