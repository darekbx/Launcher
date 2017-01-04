package com.mlauncher.logic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by INFOR PL on 2015-01-28.
 */
public class NetworkUtils {

    public static void setWiFiState(Context context, boolean enabled) {
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        manager.setWifiEnabled(enabled);
    }

    public static boolean isWiFiEnabled(Context context) {
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return manager.isWifiEnabled();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}