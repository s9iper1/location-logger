package com.byteshaft.locationlogger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppGlobals extends Application {

    private static SharedPreferences sPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
    }
}
