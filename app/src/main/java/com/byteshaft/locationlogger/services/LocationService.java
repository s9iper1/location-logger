package com.byteshaft.locationlogger.services;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.byteshaft.locationlogger.utils.LocationHelpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class LocationService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private int mLocationRecursionCounter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationRequest = LocationHelpers.createLocationRequest();
        connectingGoogleApiClient();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopLocationService();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
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

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void connectingGoogleApiClient() {
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

    private void stopLocationService() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
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
                        stopSelf();
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Current location cannot be acquired",
                                Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                } else if (mLocation == null) {
                    acquireLocation();
                    mLocationRecursionCounter++;
                    Log.i("TrackBuddy", "Tracker Thread Running... " + mLocationRecursionCounter);
                } else {
                    double accuracy;
                    String lat = String.valueOf(mLocation.getLatitude());
                    String lon = String.valueOf(mLocation.getLongitude());
                    accuracy = mLocation.getAccuracy();
                    int roundedAccuracy = (int) accuracy;
                    String text = String.format(
                            "Longitude: %s, Latitude %s, accuracy %s", lon, lat, roundedAccuracy);
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> result = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                        Log.i("ADDRESS", result.get(0).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    stopSelf();
                    mLocation = null;
                }
            }
        }, 1000);
    }
}
