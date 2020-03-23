package com.example.uradio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class addStationActivity extends AppCompatActivity {

    String stationurl;
    String stationname;
    boolean cansave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_station_main);

        // Suppress the built-in action bar
        Toolbar toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);

        // initializing variable;
        stationurl = "default";
        stationname = "default";
        cansave = false;

        // Save button actions

        // initializing the view using findViewById

        final Button saveStation_btn = (Button) findViewById(R.id.savestation_button);
        Button cancelInput_btn = (Button) findViewById(R.id.cancelstation_button);

        final EditText user_station_input = (EditText) findViewById(R.id.radiostation_userinput);
        final EditText user_stationname_input = (EditText) findViewById(R.id.radiostationname_userinput);


        // Validation rules for the text fields

        if (user_stationname_input.getText().toString().length() == 0 || user_stationname_input.getText().toString().length() > 20) {
            user_stationname_input.setError("The station name has to be within the range of 1 - 20 characters! ");
        } else {
            user_stationname_input.setError(null);
        }

        String station_url_inputted = user_station_input.getText().toString();

        if (user_station_input.getText().toString().isEmpty() || !user_station_input.getText().toString().startsWith("http://") || !user_station_input.getText().toString().startsWith("https://")) {
            user_station_input.setError("Enter a valid url, starting with 'http://' or 'https://'! ");
        } else {
            user_station_input.setError(null);

        }

        // Save down the station and return to the main activity -
        // passing the station url that the user inputted

        saveStation_btn.setOnClickListener(
                new View.OnClickListener() {
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
