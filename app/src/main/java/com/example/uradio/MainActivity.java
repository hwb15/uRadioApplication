package com.example.uradio;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    ListView stationList;
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

        stations.add("Station 1");
        stations.add("Station 2");
        stations.add("Station 3");
        stations.add("Station 4");
        stations.add("Station 5");
        stations.add("Station 6");
        stations.add("Station 7");
        stations.add("Station 8");
        stations.add("Station 9");
        stations.add("+ Add a station here");

        ArrayAdapter stationAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stations);
        stationList.setAdapter(stationAdapter);

        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Selected Station: " + stations.get(position), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
