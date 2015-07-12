package com.byteshaft.locationlogger.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.byteshaft.locationlogger.database.LocationDatabase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationUploadService extends IntentService {

    public LocationUploadService() {
        super("LocationUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isNetworkAvailable()) {
            if (isInternetWorking()) {
                LocationDatabase database = new LocationDatabase(getApplicationContext());
                ArrayList<HashMap> allRecords = database.getAllRecords();
                for (HashMap map : allRecords) {
                    String id = (String) map.get("unique_id");
                    String longitude = (String) map.get("longitude");
                    String latitude = (String) map.get("latitude");
                    String timeStamp = (String) map.get("time_stamp");
                    String userId = (String) map.get("user_id");
                    System.out.println("Internet is working: will upload" + id);
                    boolean uploaded = false;
                    // TODO: Implement Upload here:
                    if (uploaded) {
                        database.deleteEntry(Integer.valueOf(id));
                    }
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("http://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
