package com.byteshaft.locationlogger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.locationlogger.database.LocationDatabase;
import com.byteshaft.locationlogger.services.LocationUploadService;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Context appContext = context.getApplicationContext();
        LocationDatabase database = new LocationDatabase(appContext);
        if (!database.isEmpty()) {
            Intent uploaderService = new Intent(appContext, LocationUploadService.class);
            appContext.startService(uploaderService);
        }
        database.close();
    }
}
