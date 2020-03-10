package com.example.uradio;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class addStationActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_station_main);

        // Suppress the built-in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}
