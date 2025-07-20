package com.example.attendanceappdraft0.utils;

import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {

    public void themePreserver() {
        SharedPreferences themepref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int themeSet = themepref.getInt("currentTheme", getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);
        if (themeSet == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void themeChanger() {
        SharedPreferences themepref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor themeeditor = themepref.edit();
        int currentnightmode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentnightmode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            themeeditor.putInt("currentTheme", Configuration.UI_MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            themeeditor.putInt("currentTheme", Configuration.UI_MODE_NIGHT_YES);
        }

        themeeditor.apply();
        recreate();
    }
}

