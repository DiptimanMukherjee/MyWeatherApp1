package com.example.android.myweatherapp.test;

import com.example.android.myweatherapp.Model.TemperatureModel;
import com.example.android.myweatherapp.Network.NetworkUtil;
import com.example.android.myweatherapp.Network.WeatherAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkUtilTest {

    @Mock
    NetworkUtil mNetworkUtil;


    @Mock
    TemperatureModel mTemperatureModel;


    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
        getTemperatureModel();
    }

    @Test
    public void test_fetchCurrentWeatherTemperature_withValid_data() {
        mNetworkUtil.fetchCurrentWeatherTemperature("Bangalore");
        verify(mNetworkUtil).fetchCurrentWeatherTemperature("Bangalore");
    }

    @Test
    public void test_fetchTemperatureForecast_with_valid_data() {
        mNetworkUtil.fetchTemperatureForecast("Bangalore");
        verify(mNetworkUtil).fetchTemperatureForecast("Bangalore");
    }

    @Test
    public void test_temperature_model() {
        assertEquals("clear",mTemperatureModel.getWeatherName());
    }

    private void getTemperatureModel() {
        when(mTemperatureModel.getWeatherName()).thenReturn("clear");
    }
}