package com.byteshaft.locationlogger.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.byteshaft.locationlogger.database.LocationDatabase;
import com.byteshaft.locationlogger.utils.LocationHelpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private int mLocationRecursionCounter;
    private int mLocationChangedCounter;
    private IntentFilter alarmIntent = new IntentFilter("com.byteshaft.LOCATION_ALARM");
    private final String LOG_TAG = "LocationLogger";
    private Handler mHandler;

    private BroadcastReceiver mLocationRequestAlarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startLocationUpdate();
        }
    };

    private void setLocationAlarm(int time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("com.byteshaft.LOCATION_ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
    }

    private void startLocationUpdate() {
        mLocationRequest = LocationHelpers.createLocationRequest();
        connectGoogleApiClient();
    }

    private void stopLocationUpdate() {
        resetReceivingLocation();
//        int requestInterval = Integer.valueOf(getString(R.string.location_interval));
//        int intervalInMillis = (int) TimeUnit.MINUTES.toMillis(requestInterval);
//        setLocationAlarm(intervalInMillis);
    }

    private void resetReceivingLocation() {
        mLocationChangedCounter = 0;
        mLocationRecursionCounter = 0;
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(mLocationRequestAlarmReceiver, alarmIntent);
        startLocationUpdate();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHandler().removeCallbacks(mLocationRunnable);
        resetReceivingLocation();
        unregisterReceiver(mLocationRequestAlarmReceiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        acquireLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationChangedCounter++;
        if (mLocationChangedCounter == 5) {
            mLocation = location;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void connectGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void acquireLocation() {
        Handler handler = getHandler();
        handler.postDelayed(mLocationRunnable, 5000);
    }

    private Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    private Runnable mLocationRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLocation == null && mLocationRecursionCounter > 24) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation != null) {
                    String latLast = String.valueOf(mLocation.getLatitude());
                    String lonLast = String.valueOf(mLocation.getLongitude());
                    Log.w(LOG_TAG, "Failed to get location current location, saving last known location");
                    stopLocationUpdate();
                } else {
                    Log.e(LOG_TAG, "Failed to get location");
                    stopLocationUpdate();
                }
            } else if (mLocation == null) {
                acquireLocation();
                mLocationRecursionCounter++;
                Log.i(LOG_TAG, "Tracker Thread Running: " + mLocationRecursionCounter);
            } else {
                Log.i(LOG_TAG, "Location found, saving to database");
                String lat = String.valueOf(mLocation.getLatitude());
                String lon = String.valueOf(mLocation.getLongitude());
                LocationDatabase database = new LocationDatabase(getApplicationContext());
                database.createNewEntry(
                        lon, lat, LocationHelpers.getTimeStamp(), "10");

                mLocation = null;
                stopLocationUpdate();
            }
        }
    };
}
