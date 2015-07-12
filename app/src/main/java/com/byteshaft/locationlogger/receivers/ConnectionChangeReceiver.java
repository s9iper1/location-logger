package com.byteshaft.locationlogger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.byteshaft.locationlogger.database.LocationDatabase;
import com.byteshaft.locationlogger.services.LocationUploadService;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (LocationUploadService.isRunning() || !isNetworkAvailable(context)) {
            return;
        }
        Context appContext = context.getApplicationContext();
        LocationDatabase database = new LocationDatabase(appContext);
        if (!database.isEmpty()) {
            Intent uploaderService = new Intent(appContext, LocationUploadService.class);
            appContext.startService(uploaderService);
        }
        database.close();
    }

    private boolean isNetworkAvailable(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
