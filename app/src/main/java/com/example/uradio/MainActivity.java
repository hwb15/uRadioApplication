package com.example.uradio;

import android.content.Intent;
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


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    ListView stationList;
    TextView title_bar;
    View bottom_player;
    SimpleExoPlayer radioPlayer;
    ImageButton playButton;
    Boolean audio_on;
    ImageButton cogButton;
    SimpleExoPlayer emptyRadioPlayer;
    public static Boolean new_station = false;
    String stationurl;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout view
        setContentView(R.layout.activity_main);
        // Suppress the built-in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // Initalising the station listview
        stationList = (ListView)findViewById(R.id.stationList);
        final ArrayList<String> stations = new ArrayList<>();

        // Variable resets
        audio_on = false;

        // Logging down state of audio on for testing
        Log.v("randomtag", "Audio on state:" + audio_on);

        // If a new station has been saved down within addStationActivity (run method newstation)

        if (new_station == true) {
            newStation();
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

        // Adding items into the stationList array
        stations.add("Rinse FM");
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


                // Implementing exo player controls within list view >> need to change for selection
                // on specific station; and stop control through list view - might need to create a separate method for audio playing
                if (!audio_on) {
                    CreatePlayer();
                    radioPlayer.setPlayWhenReady(true);
                    playButton.setImageResource(R.drawable.exo_controls_pause);
                    audio_on = true;
                    cogButton.setVisibility(View.VISIBLE);
                }

                else if (audio_on) {
                    radioPlayer.stop();
                    playButton.setImageResource(R.drawable.play_button_white);
                    audio_on = false;
                }
            }
        });


        // Test for cog button to see if it changes to another view using intents

        cogButton = (ImageButton) findViewById(R.id.removeStationCog);
        cogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // if cog is pressed, change to add_station activity
                    CreateEmptyPlayer();
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
                Uri.parse("http://206.189.117.157:8000/stream"), // The audio stream the user selects
                new DefaultDataSourceFactory(this, userAgent),
        new DefaultExtractorsFactory(),
        null,
        null
        );
        radioPlayer.prepare(mediaSource);
    }

    private void CreateEmptyPlayer() {
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        TrackSelector trackSelector = new DefaultTrackSelector();
        emptyRadioPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);

        String userAgent = Util.getUserAgent(this, "Play Radio");
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(""), // The audio stream the user selects
                new DefaultDataSourceFactory(this, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        emptyRadioPlayer.prepare(mediaSource);
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
            radioPlayer.release();
            super.onPause();
        }



    public void newStation() {
        // Passing saved URL from addStationActivity into the ArrayList
        Bundle bundle = getIntent().getExtras();
        stationurl = bundle.getString("stationurl");
        Log.v("Station URL TEST", "Station URL: " + stationurl);
    }
}




