package com.example.android.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.myweatherapp.Model.TemperatureModel;
import com.example.android.myweatherapp.ViewModel.TemperatureViewModel;
import com.example.android.myweatherapp.ViewModel.ViewModelFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final static String TAG = "MainActivity";
    private final int LOCATION_REQUEST_CODE = 100;
    private final int MINIMUM_TIME = 5000;
    private final int MINIMUM_DISTANCE = 10;
    private boolean isLocationFetched = false;
    private TemperatureViewModel temperatureViewModel;
    private TextView text;
    private TextView forecastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        forecastText = findViewById(R.id.weather_forecast);
        ViewModelFactory factory = new ViewModelFactory(getApplication());
        temperatureViewModel = ViewModelProviders.of(this, factory).get(TemperatureViewModel.class);
        observerForCurrentTemp();
        observerForWeatherForecast();
        accessLocation();
    }

    private void accessLocation() {
        LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
        if (locationMgr != null) {
            locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);
        }
    }

    private void observerForCurrentTemp() {
        temperatureViewModel.getCurrentTemperature().observe(this, new Observer<TemperatureModel>() {
            @Override
            public void onChanged(TemperatureModel temperatureModel) {
                Log.d(TAG, "onChanged: observerForCurrentTemp "+temperatureModel.getTemperature());
                long temp = Math.round(temperatureModel.getTemperature());
                text.setText(temp + " \u00B0");
            }
        });
    }

    private void observerForWeatherForecast() {
        temperatureViewModel.getWeatherList().observe(this, new Observer<List<TemperatureModel>>() {
            @Override
            public void onChanged(List<TemperatureModel> temperatureModelsList) {
                Log.d(TAG, "onChanged: observerForWeatherForecast"+temperatureModelsList.size());
                List<String> list = new ArrayList<>();
                for(TemperatureModel data : temperatureModelsList) {
                    String val = Math.round(data.getTemperature()) + " \u00B0";
                    list.add(val);
                }
                String str = list.toString().replace(","," ").replace("[", "").replace("]", "");

                forecastText.setText(str);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!isLocationFetched) {
            isLocationFetched = true;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);
                Address address = addressList.get(0);
                temperatureViewModel.fetchCurrentTemperature(address.getLocality());

            } catch (IOException e) {

                Log.d(TAG, "onLocationChanged: IO Exception" + e);
            } catch (NullPointerException e) {

                Log.d(TAG, "onLocationChanged: Null pointer Exception" + e);
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
