package com.example.attendanceappdraft0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;

public class loadingscreenActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loadingscreen);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences loadingprefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String roletemp= loadingprefs.getString("role", "notFound");

                if (roletemp.equals("student")){
                    startActivity(new Intent(loadingscreenActivity.this, MainActivity.class));
                    finish();
                }

                else if(roletemp.equals("professor")){
                    startActivity(new Intent(loadingscreenActivity.this, ProfUIMain.class));
                    finish(); //finishing because this clears backstabs. mtlb this way pressing back wont take the user to the loading screen but the screen before it
                }

                else
                    startActivity(new Intent(loadingscreenActivity.this, SplashActivity.class));
            }
        }, 750);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}