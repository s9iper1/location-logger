package com.byteshaft.locationlogger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

import com.byteshaft.locationlogger.services.LocationService;
import com.byteshaft.locationlogger.utils.Helpers;


public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener,
        TextWatcher {

    private Switch mServiceToggle;
    private EditText mUserNameEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServiceToggle = (Switch) findViewById(R.id.switch_track_location);
        mServiceToggle.setOnCheckedChangeListener(this);
        mUserNameEntry = (EditText) findViewById(R.id.entry_user_name);
        mUserNameEntry.addTextChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!LocationService.isRunning()) {
            mServiceToggle.setChecked(false);
            mUserNameEntry.setEnabled(true);
            mUserNameEntry.setText(Helpers.getUserId());
        } else {
            mUserNameEntry.setEnabled(false);
            mUserNameEntry.setText(Helpers.getUserId());
            mServiceToggle.setChecked(true);
            mServiceToggle.setEnabled(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_track_location:
                Intent service = new Intent(this, LocationService.class);
                if (b) {
                    Helpers.setUserId(mUserNameEntry.getText().toString());
                    startService(service);
                    mUserNameEntry.setEnabled(false);
                } else {
                    stopService(service);
                    mUserNameEntry.setEnabled(true);
                }
                Helpers.setServiceEnabled(b);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().isEmpty() || charSequence.toString().length() < 3) {
            mServiceToggle.setEnabled(false);
        } else {
            mServiceToggle.setEnabled(true);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String result = editable.toString().replaceAll(" ", "");
        if (!editable.toString().equals(result)) {
            mUserNameEntry.setText(result);
            mUserNameEntry.setSelection(result.length());
        }
    }
}
