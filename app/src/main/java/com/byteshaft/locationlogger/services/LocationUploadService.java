package com.byteshaft.locationlogger.services;

import android.app.IntentService;
import android.content.Intent;

public class LocationUploadService extends IntentService {

    public LocationUploadService() {
        super("LocationUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
