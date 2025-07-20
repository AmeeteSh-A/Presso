package com.example.attendanceappdraft0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;


public class needHelpMainActivity extends BaseActivity {

    Button supportbutton, ratingbutton, faqbutton, helpmainback, button3, button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_need_help_main);
        supportbutton=findViewById(R.id.supportbutton);
        ratingbutton=findViewById(R.id.ratingbutton);
//        aboutmebutton=findViewById(R.id.aboutmebutton);
        faqbutton=findViewById(R.id.faqbutton);
        helpmainback=findViewById(R.id.helpmainback);
        button3= findViewById(R.id.button3);
        button5= findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeChanger();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        supportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent helpMainToSupport = new Intent(needHelpMainActivity.this, SupportPageActivity.class);
                startActivity(helpMainToSupport);
            }
        });

//        aboutmebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent helpMainToAboutMe = new Intent(needHelpMainActivity.this, AboutMePageActivity.class);
//                startActivity(helpMainToAboutMe);
//            }
//        });

        ratingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(needHelpMainActivity.this, RatingPageActivity.class));
            }
        });

        faqbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(needHelpMainActivity.this, FaqPageActivity.class));
            }
        });

        helpmainback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(needHelpMainActivity.this, loadingscreenActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}