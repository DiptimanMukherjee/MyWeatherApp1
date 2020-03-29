package com.example.android.myweatherapp.Model;

public class TemperatureModel {
    private double temperature;
    private double maxTemperature;
    private double minTemperature;
    private String weatherName;
    private String weatherDescription;
    private String weatherIcon;
    private String day;

    private final String IMAGE_SOURCE = "http://openweathermap.org/img/wn/";
    private final String IMAGE_TYPE = ".png";

    public TemperatureModel(double currentTemp, double max, double min, String weatherName, String weatherDescription, String weatherIcon, String day) {
        this.temperature = currentTemp;
        this.maxTemperature = max;
        this.minTemperature = min;
        this.weatherName = weatherName;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = IMAGE_SOURCE + weatherIcon + IMAGE_TYPE;
        this.day = day;
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

    public String getDay() {
        return day;
    }
}
