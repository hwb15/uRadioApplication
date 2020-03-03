package com.example.uradio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    ListView stationList;
    TextView title_bar;
    View bottom_player;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Suppress the built-in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Initalising the station listview
        stationList=(ListView)findViewById(R.id.stationList);
        final ArrayList<String> stations = new ArrayList<>();

        // Adding items into the stationList array
        stations.add("Station 1");
        stations.add("Station 2");
        stations.add("Station 3");
        stations.add("+ Add a station here");


        ArrayAdapter stationAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stations) {
            // Alternating the colour in the List View
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get current item from the stationList
                View view = super.getView(position, convertView, parent);
                if (position % 2 == 1) {
                    // Set background color for first row
                    view.setBackgroundColor(Color.parseColor("#66D5D5D5"));
                } else {
                    // Set background color for alternate row
                    view.setBackgroundColor(Color.parseColor("#66000000"));
                }

                return view;
            }
        };

        stationList.setAdapter(stationAdapter);

        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Selected Station: " + stations.get(position), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
