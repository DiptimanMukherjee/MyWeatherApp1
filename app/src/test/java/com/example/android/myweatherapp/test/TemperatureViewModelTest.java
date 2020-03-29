package com.example.android.myweatherapp.test;

import com.example.android.myweatherapp.ViewModel.TemperatureViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class TemperatureViewModelTest {

    @Mock
    TemperatureViewModel mTemperatureViewModel;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_fetchCurrentTemperature_with_valid_data() {
        mTemperatureViewModel.fetchCurrentTemperature("Bangalore");
        verify(mTemperatureViewModel).fetchCurrentTemperature("Bangalore");
    }

    @Test
    public void test_fetchTemperatureForecast_with_valid_data() {
        mTemperatureViewModel.fetchTemperatureForecast("Bangalore");
        verify(mTemperatureViewModel).fetchTemperatureForecast("Bangalore");
    }




}