package com.byteshaft.locationlogger.utils;

import com.google.android.gms.location.LocationRequest;

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
}
