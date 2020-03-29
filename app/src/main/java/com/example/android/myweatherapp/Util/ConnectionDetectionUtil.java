package com.example.android.myweatherapp.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionDetectionUtil {
    private static final String TAG = "ConnectionDetectionUtil";

    public static boolean isNetworkPresent(Context context) {
        boolean isNetworkAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {

            if (cm != null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null) {
                    switch (netInfo.getType()) {
                        case ConnectivityManager.TYPE_WIFI: {
                            isNetworkAvailable=netInfo.isConnected();
                            break;
                        }
                        case ConnectivityManager.TYPE_ETHERNET: {
                            isNetworkAvailable = netInfo.isConnected();
                            break;
                        }
                        default:
                    }
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        netInfo.isConnected();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG,"--<CONNECTION>--Error",ex);
        }
        return isNetworkAvailable;
    }
}
