package com.byteshaft.locationlogger.wardrive;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.byteshaft.locationlogger.AppGlobals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarDriveHelpers extends BroadcastReceiver {

    private WifiManager wifiManager;
    private List<ScanResult> scanResults;
    private ArrayList<String> mArrayList;
    private ArrayList<HashMap> hashMapArrayList;


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

    public ArrayList<HashMap> getHashMapArrayList() {
        return hashMapArrayList;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onReceive(Context context, Intent intent) {
        mArrayList = new ArrayList<>();
        hashMapArrayList = new ArrayList<>();
        wifiManager = (WifiManager) AppGlobals.getContext().getSystemService(
                AppGlobals.getContext().WIFI_SERVICE);
        scanResults = wifiManager.getScanResults();
        for(ScanResult result : scanResults) {
            String ssid = result.SSID;
            int strength = result.level;
            mArrayList.add(ssid);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ssid, String.valueOf(strength));
            hashMapArrayList.add(hashMap);
        }
    }
}
