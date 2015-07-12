package com.byteshaft.locationlogger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.byteshaft.locationlogger.database.LocationDatabase;
import com.byteshaft.locationlogger.services.LocationService;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener {

    private Switch mServiceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationDatabase database = new LocationDatabase(getApplicationContext());
        ArrayList<HashMap> allRecords = database.getAllRecords();
        final String SEPARATOR = ", ";
        for (HashMap map : allRecords) {
            System.out.println(map.get("unique_id")
                            + SEPARATOR
                            + map.get("longitude")
                            + SEPARATOR
                            + map.get("latitude")
                            + SEPARATOR
                            + map.get("time_stamp")
                            + SEPARATOR
                            + map.get("user_id")
            );
        }
        mServiceToggle = (Switch) findViewById(R.id.switch_track_location);
        mServiceToggle.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mServiceToggle.setChecked(LocationService.isRunning());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_track_location:
                Intent service = new Intent(this, LocationService.class);
                if (b) {
                    startService(service);
                } else {
                    stopService(service);
                }
        }
    }
}
