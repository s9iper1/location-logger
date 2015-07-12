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
        mLocationChangedCounter = 0;
        mLocationRecursionCounter = 0;
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
//        int requestInterval = Integer.valueOf(getString(R.string.location_interval));
//        int intervalInMillis = (int) TimeUnit.MINUTES.toMillis(requestInterval);
//        setLocationAlarm(intervalInMillis);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(mLocationRequestAlarmReceiver, alarmIntent);
        startLocationUpdate();

        return START_STICKY;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLocation == null && mLocationRecursionCounter > 120) {
                    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLocation != null) {
                        String latLast = String.valueOf(mLocation.getLatitude());
                        String lonLast = String.valueOf(mLocation.getLongitude());
                        String text = String.format(
                                "Cannot acquire currect location. " +
                                        "Last known: Longitude: %s, Latitude %s", latLast, lonLast);
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        stopLocationUpdate();
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Current location cannot be acquired",
                                Toast.LENGTH_SHORT).show();
                        stopLocationUpdate();
                    }
                } else if (mLocation == null) {
                    acquireLocation();
                    mLocationRecursionCounter++;
                    Log.i("TrackBuddy", "Tracker Thread Running... " + mLocationRecursionCounter);
                } else {
                    String lat = String.valueOf(mLocation.getLatitude());
                    String lon = String.valueOf(mLocation.getLongitude());
                    LocationDatabase database = new LocationDatabase(getApplicationContext());
                    database.createNewEntry(
                            lon, lat, LocationHelpers.getTimeStamp(), "10");

                    mLocation = null;
                    stopLocationUpdate();
                }
            }
        }, 1000);
    }
}
