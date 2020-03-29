package com.example.android.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.myweatherapp.Adapter.ForecastAdapter;
import com.example.android.myweatherapp.Listener.OnSwipeTouchListener;
import com.example.android.myweatherapp.Model.TemperatureModel;
import com.example.android.myweatherapp.Util.ConnectionDetectionUtil;
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
    private TemperatureViewModel temperatureViewModel;
    private TextView text;
    private TextView desc;
    private TextView forecastText;
    private LinearLayout hiddenView;
    private RecyclerView forecastList;
    private RelativeLayout layoutContainer;
    private RelativeLayout viewContainer;
    private TextView extraInfo;
    private TextView detailsHeader;
    private TextView swipeInfo;
    private String[] localAddressArray;
    private TextView localAddress;
    private Button retryButton;
    private boolean isEnabled = false;
    private String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutContainer = findViewById(R.id.layout_container);
        viewContainer = findViewById(R.id.view_container);
        text = findViewById(R.id.text);
        desc = findViewById(R.id.desc);
        forecastText = findViewById(R.id.weather_forecast);
        hiddenView = findViewById(R.id.hidden_view);
        forecastList = findViewById(R.id.forecastList);
        extraInfo = findViewById(R.id.extra_info);
        detailsHeader = findViewById(R.id.details_header);
        swipeInfo = findViewById(R.id.swipe_info);
        localAddress = findViewById(R.id.local_address);
        retryButton = findViewById(R.id.retry_button);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        forecastList.setLayoutManager(recyclerLayoutManager);
        ViewModelFactory factory = new ViewModelFactory(getApplication());
        temperatureViewModel = ViewModelProviders.of(this, factory).get(TemperatureViewModel.class);
        observerForCurrentTemp();
        observerForWeatherForecast();
        accessLocation();

        layoutContainer.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                hiddenView.setVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,
                        0,
                        hiddenView.getHeight(),
                        0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                hiddenView.startAnimation(animate);
                text.setAlpha(0.7f);
                desc.setAlpha(0.7f);
                forecastText.setAlpha(0.7f);
                detailsHeader.setAlpha(0.7f);
                swipeInfo.setAlpha(0.7f);
                localAddress.setAlpha(0.7f);
                hiddenView.setAlpha(1.0f);
            }

            public void onSwipeBottom() {
                hiddenView.setVisibility(View.INVISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,
                        0,
                        0,
                        hiddenView.getHeight());
                animate.setDuration(500);
                animate.setFillAfter(true);
                hiddenView.startAnimation(animate);
                text.setAlpha(1.0f);
                desc.setAlpha(1.0f);
                forecastText.setAlpha(1.0f);
                detailsHeader.setAlpha(1.0f);
                swipeInfo.setAlpha(1.0f);
                localAddress.setAlpha(1.0f);
                hiddenView.setAlpha(0.7f);
            }
        });

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnabled = false;
                accessLocation();
            }
        });
    }

    private void accessLocation() {
        LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
        if (locationMgr != null && checkLocationPermission()) {
            locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    accessLocation();
                } else {
                    String text = getString(R.string.error_string_permission);
                    showErrorMessage(text);
                }
                return;
            }
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void observerForCurrentTemp() {
        temperatureViewModel.getCurrentTemperature().observe(this, new Observer<TemperatureModel>() {
            @Override
            public void onChanged(TemperatureModel temperatureModel) {
                if(temperatureModel != null) {
                    Log.d(TAG, "onChanged: observerForCurrentTemp " + temperatureModel.getTemperature());
                    extraInfo.setVisibility(View.GONE);
                    retryButton.setVisibility(View.GONE);
                    viewContainer.setVisibility(View.VISIBLE);
                    localAddress.setText(localAddressArray[1]);
                    String degree = "\u00B0";
                    long temp = Math.round(temperatureModel.getTemperature());
                    text.setText(temp + degree);
                    String description = temperatureModel.getWeatherName() + " " + Math.round(temperatureModel.getMaxTemperature()) + "/" + (Math.round(temperatureModel.getMinTemperature()) + degree + "C");
                    desc.setText(description);
                    temperatureViewModel.fetchTemperatureForecast(city);
                } else {
                    showErrorMessage(getString(R.string.error_string));
                }
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
                    String val = Math.round(data.getTemperature()) + "\u00B0";
                    list.add(val);
                }
                String str = list.toString().replace(","," ").replace("[", "").replace("]", "");

                ForecastAdapter forecastAdapter = new ForecastAdapter(temperatureModelsList);
                forecastList.setAdapter(forecastAdapter);

                forecastText.setText(str);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: "+isEnabled);
        if(!isEnabled) {
            isEnabled = true;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);
                Address address = addressList.get(0);
                Log.d(TAG, "onLocationChanged: " + address.getAddressLine(0));
                localAddressArray = address.getAddressLine(0).split(",");
                Log.d(TAG, "onLocationChanged: " + ConnectionDetectionUtil.isNetworkPresent(getBaseContext()));
                if (ConnectionDetectionUtil.isNetworkPresent(getBaseContext())) {
                    city = address.getLocality();
                    temperatureViewModel.fetchCurrentTemperature(city);
                } else {
                    String text = getString(R.string.error_string_internet);
                    showErrorMessage(text);
                }
            } catch (IOException e) {
                Log.d(TAG, "onLocationChanged: IO Exception" + e);
            } catch (NullPointerException e) {
                Log.d(TAG, "onLocationChanged: Null pointer Exception" + e);
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        String text = getString(R.string.error_string_location);
        showErrorMessage(text);
    }

    private void showErrorMessage(String errorMessage) {
        viewContainer.setVisibility(View.INVISIBLE);
        extraInfo.setText(errorMessage);
        extraInfo.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }
}
