package com.byteshaft.locationlogger.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;

import com.google.android.gms.location.LocationRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class LocationHelpers extends ContextWrapper {

    private Handler mHandler;
    private LocationRequest mLocationRequest;

    public LocationHelpers(Context base) {
        super(base);
    }

    public LocationRequest getLocationRequest() {
        long INTERVAL = 0;
        long FASTEST_INTERVAL = 0;
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
        return mLocationRequest;
    }

    public static String getTimeStamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.UK);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getLongitudeAsString(Location location) {
        return String.valueOf(location.getLongitude());
    }

    public static String getLatitudeAsString(Location location) {
        return String.valueOf(location.getLatitude());
    }

    public void setLocationAlarm(int time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("com.byteshaft.LOCATION_ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }
}
