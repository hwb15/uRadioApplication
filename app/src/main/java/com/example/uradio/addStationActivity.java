package com.example.uradio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class addStationActivity extends AppCompatActivity {

    String stationurl;
    String stationname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_station_main);

        // Suppress the built-in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // initializing variable;
        stationurl = "default";
        stationname = "default";

        // Save button actions

        // initializing the view using findViewById

        Button saveStation_btn = (Button) findViewById(R.id.savestation_button);
        Button cancelInput_btn = (Button) findViewById(R.id.cancelstation_button);

        final EditText user_station_input = (EditText) findViewById(R.id.radiostation_userinput);
        final EditText user_stationname_input = (EditText) findViewById(R.id.radiostationname_userinput);

        // Save down the station and return to the main activity -
        // passing the station url that the user inputted

        saveStation_btn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        // Saving the http to string to be passed to main activity
                        MainActivity.new_station = true;
                        stationname = user_stationname_input.getText().toString();
                        stationurl = user_station_input.getText().toString();
                        Intent passStation = new Intent(addStationActivity.this, MainActivity.class);
                        passStation.putExtra("stationurl", stationurl);
                        passStation.putExtra("stationname", stationname);
                        startActivity(passStation);
                    }
                }
        );

        cancelInput_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Changing back to the main activity
                        MainActivity.new_station = false;
                        Intent cancelStation = new Intent(addStationActivity.this, MainActivity.class);
                        startActivity(cancelStation);
                    }
                }
        );
    }

}
