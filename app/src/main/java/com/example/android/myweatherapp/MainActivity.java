package com.example.android.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.android.myweatherapp.Network.NetworkUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final static String TAG = "MainActivity";
    private final int LOCATION_REQUEST_CODE = 100;
    private final int MINIMUM_TIME = 5000;
    private final int MINIMUM_DISTANCE = 10;
    private NetworkUtil networkUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkUtil = new NetworkUtil();
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

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);
            Address address = addressList.get(0);
            networkUtil.fetchCurrentWeatherTemperature(address.getLocality());

        } catch (IOException e) {

            Log.d(TAG, "onLocationChanged: IO Exception"+e);
        } catch (NullPointerException e) {

            Log.d(TAG, "onLocationChanged: Null pointer Exception"+e);
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
