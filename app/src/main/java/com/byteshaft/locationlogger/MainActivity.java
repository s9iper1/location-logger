package com.byteshaft.locationlogger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.locationlogger.database.LocationDatabase;
import com.byteshaft.locationlogger.services.LocationService;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationDatabase database = new LocationDatabase(getApplicationContext());
        ArrayList<HashMap> all_coordinates = database.getCoordinatesForID("10");
        final String SEPARATOR = ", ";
        for (HashMap map : all_coordinates) {
            System.out.println(map.get("longitude")
                    + SEPARATOR
                    + map.get("latitude")
                    + SEPARATOR
                    + map.get("time_stamp")
                    + SEPARATOR
                    + map.get("user_id")
            );
        }
        Intent service = new Intent(this, LocationService.class);
        startService(service);
    }
}
