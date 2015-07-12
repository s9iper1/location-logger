package com.byteshaft.locationlogger.utils;

import com.google.android.gms.location.LocationRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class LocationHelpers {

    public static LocationRequest createLocationRequest() {
        long INTERVAL = 0;
        long FASTEST_INTERVAL = 0;
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public static String getTimeStamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.UK);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(calendar.getTime());
    }
}
