package com.byteshaft.locationlogger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.byteshaft.locationlogger.utils.Network;

public class AppGlobals extends Application {

    private static SharedPreferences sPreferences;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sContext = getApplicationContext();
    }

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    public static void saveMac() {
        sPreferences.edit().putString("device_mac", Network.getMACAddress("wlan0")).apply();
    }

    public static String getDeviceMacAddress() {
        return sPreferences.getString("device_mac", null);
    }
}
