package com.example.uradio;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static com.example.uradio.MainActivity.stations;

public class SettingsActivity extends AppCompatActivity {

    private Button remove_station_button, about_button;
    private DrawerLayout drawerLayout;
    private NavigationView navMenu;
    private boolean remove_stations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_main);

        // Suppress the built-in action bar
        Toolbar toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_settings);
        navMenu = findViewById(R.id.nav_menu_view_settings);

        remove_stations = false;

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(SettingsActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        Intent home_active = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(home_active);
                        return true;
                    case R.id.nav_settings:
                        Toast.makeText(SettingsActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        return false;
                }
            }
        });

        //Initialising button options within the view

        remove_station_button = (Button) findViewById(R.id.settingsRemoveButton);
        about_button = (Button) findViewById(R.id.aboutButton);

        // Onclick listeners for buttons
        remove_station_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setIcon(R.drawable.ic_delete_forever_24px)
                        .setTitle("Confirm below...")
                        .setMessage("Do you want to remove all Stations you have saved?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeStations();
                                Log.v("Cleared", "Stations have been cleared");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setIcon(R.drawable.ic_help_black_24dp)
                        .setTitle("About...")
                        .setMessage("uRadio.uk by github.com/hwb15")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeStations() {
            String result = "result";
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", result);
            setResult(RESULT_OK, returnIntent);
            finish();
    }
}
