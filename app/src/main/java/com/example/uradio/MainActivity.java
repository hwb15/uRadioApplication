package com.example.uradio;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.navigation.NavigationView;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    ListView stationList;
    TextView title_bar;
    View bottom_player;
    SimpleExoPlayer radioPlayer;
    ImageButton playButton;
    Boolean audio_on;
    ImageButton plusStation;
    public static Boolean new_station = false;
    String stationurl;
    ArrayList<radio_station> stations = new ArrayList<radio_station>();
    String selectedstation;
    String stationname;
    TextView now_playing;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load saved station list from gson
        loadStations();
        // Set layout view
        setContentView(R.layout.activity_main);
        // Suppress the built-in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Initalising the station listview
        stationList = (ListView)findViewById(R.id.stationList);


        // Variable resets
        audio_on = false;

        // Logging down state of audio on for testing
        Log.v("randomtag", "Audio on state:" + audio_on);

        // If a new station has been saved down within addStationActivity (run method newstation)

        if (new_station == true) {
            newStation();
            new_station = false;
        }

        // Implementing control for the play button in the botton nav bar

        playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!audio_on) {
                CreatePlayer();
                radioPlayer.setPlayWhenReady(true);
                playButton.setImageResource(R.drawable.exo_controls_pause);
                audio_on = true;
            }

                else if (audio_on) {
                radioPlayer.stop();
                playButton.setImageResource(R.drawable.play_button_white);
                audio_on = false;
            }
        }
        });




        final ArrayAdapter stationAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stations) {
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

        // Setting station adapter into the listview

        stationList.setAdapter(stationAdapter);

        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Selected Station: " + stations.get(position), Toast.LENGTH_SHORT).show();

                // Sets the variable selected station to the station user has pressed on (to be loaded to uri)
                selectedstation = stations.get(position).getStation_url();

                // Set the now playing text view to the station name saved by user
                now_playing = findViewById(R.id.now_playing_label);


                // Implementing exo player controls within list view >> need to change for selection
                // on specific station; and stop control through list view - might need to create a separate method for audio playing
                if (!audio_on) {
                    CreatePlayer();
                    now_playing.setText(stations.get(position).getStation_name());
                    radioPlayer.setPlayWhenReady(true);
                    playButton.setImageResource(R.drawable.exo_controls_pause);
                    audio_on = true;
                }

                else if (audio_on) {
                    radioPlayer.stop();
                    playButton.setImageResource(R.drawable.play_button_white);
                    audio_on = false;
                }
            }
        });

        // Setup long press on listview item for to delete a station from the list

        stationList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_delete_forever_24px)
                        .setTitle("Confirm below...")
                        .setMessage("Do you want to remove this station?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stations.remove(position);
                                stationAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });


        // Test for plus button to see if it changes to another view using intents

        plusStation = (ImageButton) findViewById(R.id.plus_station_button);
        plusStation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // if plus button is pressed, change to add_station activity
                    Intent intent = new Intent(v.getContext(), addStationActivity.class);
                    v.getContext().startActivity(intent);
                    Log.i("Main Activity", "Add Station Activity");
                }
            });
        }


    // ExoPlayer implementations

    private void CreatePlayer() {
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        TrackSelector trackSelector = new DefaultTrackSelector();
        radioPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);

        String userAgent = Util.getUserAgent(this, "Play Radio");
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(selectedstation), // The audio stream the user selects
                new DefaultDataSourceFactory(this, userAgent),
        new DefaultExtractorsFactory(),
        null,
        null
        );
        radioPlayer.prepare(mediaSource);
    }


    private void release_player() {
        radioPlayer.release();
    }

    @Override
    protected void onDestroy() {
        radioPlayer.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (audio_on) {
            radioPlayer.release();
            saveStations();
            super.onPause();
        } else {
            saveStations();
            super.onPause();
        }
    }

    private void saveStations() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(stations);
        editor.putString("station list", json);
        editor.apply();
    }

    private void loadStations() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("station list", null);
        Type type = new TypeToken<ArrayList<radio_station>>() {}.getType();
        stations = gson.fromJson(json, type);

        // check for null
        if (stations == null) {
            stations = new ArrayList<>();
        }
    }

    public void newStation() {
        // Passing saved URL from addStationActivity into the ArrayList
        Bundle bundle = getIntent().getExtras();
        stationurl = bundle.getString("stationurl");
        stationname = bundle.getString("stationname");
        Log.v("Station NAME TEST", "Station NAME: " + stationname);
        Log.v("Station URL TEST", "Station URL: " + stationurl);
        radio_station newStation = new radio_station(stationname, stationurl);
        stations.add(newStation);
    }
}




