package com.example.android.myweatherapp.ViewModel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mApplication;

    public ViewModelFactory(Application application) {
        mApplication = application;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(TemperatureViewModel.class)) {
            return (T) new TemperatureViewModel(mApplication);
        }
        throw new IllegalArgumentException("Error: please instantiate from valid class");
    }
}
