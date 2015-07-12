package com.byteshaft.locationlogger.services;

import android.app.IntentService;
import android.content.Intent;

import com.byteshaft.locationlogger.database.LocationDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationUploadService extends IntentService {

    public LocationUploadService() {
        super("LocationUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO: Check if internet is available and only proceed if we get a successful ping
        LocationDatabase database = new LocationDatabase(getApplicationContext());
        ArrayList<HashMap> allRecords = database.getAllRecords();
        for (HashMap map : allRecords) {
            String id = (String) map.get("unique_id");
            String longitude = (String) map.get("longitude");
            String latitude = (String) map.get("latitude");
            String timeStamp = (String) map.get("time_stamp");
            String userId = (String) map.get("user_id");
            // TODO: Implement Upload here: if successful use database.deleteEntry()
        }
    }
}
