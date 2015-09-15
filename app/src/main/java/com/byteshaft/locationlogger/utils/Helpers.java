package com.byteshaft.locationlogger.utils;

import android.content.SharedPreferences;

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

}
