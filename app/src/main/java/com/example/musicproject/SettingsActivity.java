package com.example.musicproject;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private String backgroundColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        backgroundColor = (sharedPreferences.getString("color", "none"));

        switch (backgroundColor) {

            case "Pink":
                setTheme(R.style.AppTheme1);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings_activity);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings_activity);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings_activity);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings_activity);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings_activity);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.settings_activity);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


    }

