package com.example.uradio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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
    SimpleExoPlayer radioPlayer;
    ImageButton playButton;
    Boolean audio_on;
    ImageButton plusStation;
    public static Boolean new_station = false;
    String stationurl;
    static ArrayList<radio_station> stations = new ArrayList<>();
    String selectedstation;
    String stationname;
    TextView now_playing;
    private DrawerLayout drawerLayout;
    private NavigationView navMenu;
    ArrayAdapter stationAdapter;
    int LAUNCH_SETTINGS_ACTIVITY = 1;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.uRadioTheme);
        super.onCreate(savedInstanceState);

        // load saved station list from gson
        loadStations();
        // Set layout view
        setContentView(R.layout.activity_main);

        // Top toolbar
        Toolbar toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);

        // Coding Navigation Menu
        // Intialising nav drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navMenu = findViewById(R.id.nav_menu_view);

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_settings:
                        Intent settings_active = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(settings_active, LAUNCH_SETTINGS_ACTIVITY);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Initalising the station listview
        stationList = findViewById(R.id.stationList);


        // Variable resets
        audio_on = false;


        // If a new station has been saved down within addStationActivity (run method newstation)

        if (new_station) {
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
                    playButton.setImageResource(R.drawable.ic_u_radio_pause);
                    audio_on = true;
                } else if (audio_on) {
                    radioPlayer.stop();
                    playButton.setImageResource(R.drawable.ic_u_radio_play);
                    audio_on = false;
                }
            }
        });


        stationAdapter = new ArrayAdapter(this, R.layout.simple_list_item_1_radiolist, stations) {
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
                now_playing = findViewById(R.id.station_name);

                // Implementing exo player controls within list view >> need to change for selection
                // on specific station; and stop control through list view - might need to create a separate method for audio playing
                if (!audio_on) {
                    CreatePlayer();
                    now_playing.setText(stations.get(position).getStation_name());
                    radioPlayer.setPlayWhenReady(true);
                    playButton.setImageResource(R.drawable.ic_u_radio_pause);
                    audio_on = true;
                } else if (audio_on) {
                    radioPlayer.stop();
                    playButton.setImageResource(R.drawable.ic_u_radio_play);
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


        plusStation = findViewById(R.id.plus_station_button);
        plusStation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
        radioPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                switch (error.type) {
                    case ExoPlaybackException.TYPE_SOURCE:
                        Log.v("SOURCE ERROR", "DSOURCE ERROR TEST SOURCE ERROR TEST");
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.ic_u_radio_pause)
                                .setTitle("Issue connecting to the stream you provided!")
                                .setMessage("We cannot connect to the radio stream.\nPlease remove the station, check and re-enter with the correct URL.")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        break;

                    case ExoPlaybackException.TYPE_RENDERER:
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.exo_notification_small_icon)
                                .setMessage("This station cannot be played back right now.\nPlease remove the station and check the URL is correct.\nIf this issue persists, please contact us on twitter @uradiouk.")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        break;

                    case ExoPlaybackException.TYPE_UNEXPECTED:
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.exo_notification_small_icon)
                                .setMessage("This station cannot be played back.\nPlease remove the station and check the URL is correct.\nIf this issue persists, please contact us on twitter @uradiouk.")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        break;
                }
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        radioPlayer.prepare(mediaSource);
    }


    private void release_player() {
        radioPlayer.release();
    }

    @Override
    protected void onDestroy() {
        if (audio_on) {
            radioPlayer.release();
            super.onDestroy();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SETTINGS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                stations.clear();
                stationAdapter.notifyDataSetChanged();
                Log.v("Adapter Set Change", "Dataset Changed");
            }
            if (resultCode == RESULT_CANCELED) {
                Log.v("No options selected", "X selected in settings");
            }
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
        radio_station newStation = new radio_station(stationname, stationurl);
        stations.add(newStation);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}




