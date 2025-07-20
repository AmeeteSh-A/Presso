package com.example.attendanceappdraft0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        //handler: used for running delayed task. post se instant, postdelayed se kuch time ke baad (can be specified) and removecallback se remove a post or some.
        //looper: like an infinite loop, runs on the main thread.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String role = prefs.getString("role", "none");

                if (role.equals("student")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else if (role.equals("professor")) {
                    startActivity(new Intent(SplashActivity.this, ProfUIMain.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, RoleSelectionActivity.class));
                }

                finish();
            }
        }, 2000);
        // isme looper is used to keep the handler on the main thread, just an efficiency recommendation. prevents the handler from crashing in some cases when there might be some process going on on some other thread.

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
