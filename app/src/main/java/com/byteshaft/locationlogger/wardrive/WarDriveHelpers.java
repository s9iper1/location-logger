package com.byteshaft.locationlogger.wardrive;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.byteshaft.locationlogger.AppGlobals;
import com.byteshaft.locationlogger.database.LocationDatabase;

import java.util.ArrayList;
import java.util.List;

public class WarDriveHelpers extends BroadcastReceiver {

    private WifiManager wifiManager;
    private List<ScanResult> scanResults;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mUniqueArrayList;
    private LocationDatabase mLocationDatabase;


    @SuppressWarnings("static-access")
    public void searchWifi() {
        wifiManager = (WifiManager) AppGlobals.getContext().getSystemService(
                AppGlobals.getContext().WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
    }

    public ArrayList<String> getSSIDArrayList() {
        return mArrayList;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onReceive(Context context, Intent intent) {
        mArrayList = new ArrayList<>();
        mLocationDatabase = new LocationDatabase(context);
        wifiManager = (WifiManager) AppGlobals.getContext().getSystemService(
                AppGlobals.getContext().WIFI_SERVICE);
        scanResults = wifiManager.getScanResults();
        for(ScanResult result : scanResults) {
            mArrayList.add(result.SSID);
        }
    }
}
