package com.byteshaft.locationlogger.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.byteshaft.locationlogger.R;
import com.byteshaft.locationlogger.database.LocationDatabase;
import com.byteshaft.locationlogger.utils.WebServiceHelpers;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationUploadService extends IntentService {

    private static LocationUploadService sInstance;

    public static boolean isRunning() {
        return sInstance != null;
    }

    public LocationUploadService() {
        super("LocationUploadService");
        sInstance = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationDatabase database = new LocationDatabase(getApplicationContext());
        if (database.isEmpty()) {
            database.close();
            return;
        }

        if (isNetworkAvailable() && isInternetWorking()) {
            ArrayList<HashMap> records = database.getAllRecords();
            try {
                String loginEmail = getString(R.string.server_login_email);
                String loginPassword = getString(R.string.server_login_password);
                String sessionId = WebServiceHelpers.getSessionId(loginEmail, loginPassword);
                WebServiceHelpers.writeRecords(sessionId, records);
                database.close();
            } catch (IOException | JSONException e) {
                database.close();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sInstance = null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
