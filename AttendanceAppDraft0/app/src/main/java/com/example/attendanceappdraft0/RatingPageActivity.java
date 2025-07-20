package com.example.attendanceappdraft0;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;
import com.example.attendanceappdraft0.utils.UIUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingPageActivity extends BaseActivity {

    RatingBar ratingBar;
    TextView ratingText, averageRating, totalRatings;
    Button submitButton, helpmainback2, changeratingbutton, button3, button5;
    RatingBar indicatorRatingBar;
    TextView titletext;
    TextView thankyoutext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating_page);

        SharedPreferences ratingCheck = getSharedPreferences("ratingPrefs", MODE_PRIVATE);
        String checker = ratingCheck.getString("hasRated", "no");
        float rate = ratingCheck.getFloat("rating", 0);

        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.ratingText);
        averageRating = findViewById(R.id.averageRating);
        totalRatings = findViewById(R.id.totalRatings);
        submitButton = findViewById(R.id.submitButton);
        indicatorRatingBar = findViewById(R.id.indicatorRatingBar);
        helpmainback2=findViewById(R.id.helpmainback2);
        titletext=findViewById(R.id.titletext);
        thankyoutext=findViewById(R.id.thankyoutext);
        changeratingbutton=findViewById(R.id.changeratingbutton);

        UIUtils.applyShrinkEffect(submitButton);
        UIUtils.applyShrinkEffect(helpmainback2);
        UIUtils.applyShrinkEffect(changeratingbutton);

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

        helpmainback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RatingPageActivity.this, loadingscreenActivity.class));
            }
        });
        if (checker.equals("yes")) {

            titletext.setVisibility(INVISIBLE);
            thankyoutext.setVisibility(VISIBLE);
            submitButton.setEnabled(false);
            submitButton.setVisibility(INVISIBLE);

            ratingBar.setIsIndicator(true);
            ratingBar.setRating(rate);
            updateReactionText(rate);
            submitButton.setEnabled(false);
            changeratingbutton.setVisibility(VISIBLE);
        } else {

            thankyoutext.setVisibility(INVISIBLE);
            titletext.setVisibility(VISIBLE);
            changeratingbutton.setVisibility(INVISIBLE);
            submitButton.setVisibility(VISIBLE);
            submitButton.setEnabled(true);



            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    updateReactionText(rating);
                }
            });



        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                float rating = ratingBar.getRating();
                DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference("ratings");
                String id = ratingRef.push().getKey();
                if (id != null) {
                    ratingRef.child(id).setValue(rating);
                }

                Toast.makeText(RatingPageActivity.this, "Thank you for rating!", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor ratingEditor = ratingCheck.edit();
                ratingEditor.putString("hasRated", "yes");
                ratingEditor.putFloat("rating", rating);
                ratingEditor.apply();


                titletext.setVisibility(INVISIBLE);
                thankyoutext.setVisibility(VISIBLE);

                ratingBar.setIsIndicator(true);
                ratingBar.setRating(rating);

                updateReactionText(rating);
                submitButton.setEnabled(false);
                submitButton.setVisibility(INVISIBLE);

                updateAverageRating();

                changeratingbutton.setVisibility(VISIBLE);



            }
        });

        changeratingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        thankyoutext.setVisibility(INVISIBLE);
                        titletext.setVisibility(VISIBLE);
                        changeratingbutton.setVisibility(INVISIBLE);
                        submitButton.setVisibility(VISIBLE);
                        submitButton.setEnabled(true);

                        ratingBar.setIsIndicator(false);

                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                updateReactionText(rating);

                            }
                        });


            }
        });

        updateAverageRating();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateReactionText(float rate) {
        if (rate > 0 && rate <= 1) {
            ratingText.setText("💔💔🤚🏻");
        } else if (rate > 1 && rate <= 2) {
            ratingText.setText("\uD83E\uDD40\uD83E\uDD40\uD83E\uDD40");
        } else if (rate > 2 && rate <= 3) {
            ratingText.setText("😐🙏🏻");
        } else if (rate > 3 && rate <= 4) {
            ratingText.setText("😛😋😋");
        } else if (rate > 4 && rate <= 5) {
            ratingText.setText("🥳🥳💪🏻🤙🏻");
        }
    }

    private void updateAverageRating() {
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference("ratings");

        ratingRef.get().addOnSuccessListener(snapshot -> {
            float total = 0;
            int count = 0;

            for (DataSnapshot child : snapshot.getChildren()) {
                Float value = child.getValue(Float.class);
                if (value != null) {
                    total += value;
                    count++;
                }
            }

            if (count > 0) {
                float average = total / count;
                indicatorRatingBar.setRating(average);
                averageRating.setText(String.format("%.1f", average));
                totalRatings.setText("Total Ratings: " + count);
            } else {
                averageRating.setText("No ratings yet");
                totalRatings.setText("Total Ratings: 0");
            }
        });
    }}
