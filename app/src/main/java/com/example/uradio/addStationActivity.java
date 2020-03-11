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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_station_main);

        // Suppress the built-in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // initializing variable;
        stationurl = "default";

        // Save button actions

        // initializing the view using findViewById

        Button saveStation_btn = (Button) findViewById(R.id.savestation_button);
        final EditText user_station_input = (EditText) findViewById(R.id.radiostation_userinput);

        saveStation_btn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        // Saving the http to string to be passed to main activity
                        MainActivity.new_station = true;
                        stationurl = user_station_input.getText().toString();
                        Intent passStation = new Intent(addStationActivity.this, MainActivity.class);
                        passStation.putExtra("stationurl", stationurl);
                        startActivity(passStation);
                    }
                }
        );
    }

}
