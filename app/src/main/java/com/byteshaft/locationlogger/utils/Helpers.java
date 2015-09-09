package com.byteshaft.locationlogger.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.byteshaft.locationlogger.AppGlobals;

public class Helpers {

    public static boolean isServiceEnabled() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getBoolean("service_enabled", false);
    }

    public static void setServiceEnabled(boolean enabled) {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        preferences.edit().putBoolean("service_enabled", enabled).apply();
    }

    public static void setUserId(String userId) {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        preferences.edit().putString("user_id", userId).apply();
    }

    public static String getUserId() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getString("user_id", null);
    }

    public static String getDeviceMac() {
        WifiManager wifiManager = (WifiManager) AppGlobals.getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }

}
