package com.byteshaft.locationlogger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.locationlogger.AppGlobals;
import com.byteshaft.locationlogger.services.LocationService;
import com.byteshaft.locationlogger.utils.Helpers;

public class BootStateReader extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Helpers.isServiceEnabled()) {
            Intent service = new Intent(context, LocationService.class);
            context.startService(service);
        }
    }
}
