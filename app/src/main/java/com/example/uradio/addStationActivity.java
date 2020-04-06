package com.example.uradio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

public class addStationActivity extends AppCompatActivity {

    String stationurl;
    String stationname;

    private EditText user_stationname_input, user_station_input;
    private Button saveStation_btn, cancelInput_btn;
    private TextInputLayout inputLayoutStationName, inputLayoutStationURL;
    private ImageButton add_station_button;

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


        // initializing the view using findViewById
        // text input layouts
        inputLayoutStationName = (TextInputLayout) findViewById(R.id.inputLayoutStationName);
        inputLayoutStationURL = (TextInputLayout) findViewById(R.id.inputLayoutStationURL);
        //buttons
        saveStation_btn = (Button) findViewById(R.id.savestation_button);
        cancelInput_btn = (Button) findViewById(R.id.cancelstation_button);
        // edit text fields
        user_station_input = (EditText) findViewById(R.id.radiostation_userinput);
        user_stationname_input = (EditText) findViewById(R.id.radiostationname_userinput);

        // Hiding the add button
        add_station_button = (ImageButton) findViewById(R.id.plus_station_button);
        add_station_button.setVisibility(View.INVISIBLE);



        // Save down the station and return to the main activity -
        // passing the station url that the user inputted

        saveStation_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validateStation();

                    }
                }
        );


        cancelInput_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelSaveStation();
                    }
                }
        );
    }

    private void validateStation() {
        boolean isValid = true;
        if (user_stationname_input.getText().toString().isEmpty()) {
            inputLayoutStationName.setError("The station name is empty! Fill this in!");
            isValid = false;
        } else {
            inputLayoutStationName.setErrorEnabled(false);
        }

        if (user_station_input.getText().toString().startsWith("http://") || user_station_input.getText().toString().startsWith("https://")) {
            inputLayoutStationURL.setErrorEnabled(false);
        } else {
            inputLayoutStationURL.setError("Station URL needs to start with either 'http://' or 'https://'");
            isValid = false;
        }

        if (isValid) {
            saveStation();
        }
    }

    private void saveStation() {
        // Saving the http to string to be passed to main activity
        MainActivity.new_station = true;
        stationname = user_stationname_input.getText().toString();
        stationurl = user_station_input.getText().toString();
        Intent passStation = new Intent(addStationActivity.this, MainActivity.class);
        passStation.putExtra("stationurl", stationurl);
        passStation.putExtra("stationname", stationname);
        startActivity(passStation);
    }

    private void cancelSaveStation() {
        // Changing back to the main activity
        MainActivity.new_station = false;
        Intent cancelStation = new Intent(addStationActivity.this, MainActivity.class);
        startActivity(cancelStation);
    }
}
