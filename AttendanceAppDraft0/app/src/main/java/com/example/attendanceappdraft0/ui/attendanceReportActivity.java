package com.example.attendanceappdraft0.ui;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.R;
import com.example.attendanceappdraft0.needHelpMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import kotlinx.coroutines.flow.SharedFlow;

public class attendanceReportActivity extends AppCompatActivity {

    Spinner subjectChoosingSpinner, percentChoosingSpinner;
    TextView totalDaysTextShower, presentDaysTextShower, subjectAttendanceShowerTextView, totalAttendanceShowerTextView, subjectAttendancePercentRemarks, totalAttendancePercentRemarks, subjectTotalDaysTextShower, subjectPresentDaysTextShower;
    CircularProgressIndicator attendancePercentCircularIndicator, totalAttendancePercentCircularIndicator;
    String chosenSubject, chosenpercentstring;
    Long total, present;
    String totaltobeused, presenttobeused;
    int totaltobeusedint, presenttobeusedint;
    double totalAttendanceDoubleRounded, percenttobeusedRounded;
    Button needhelpbutton, button3, button5;

    String[] totalAbove0to5 = {
            "Skating just above the danger zone ⛸️",
            "One sneeze away from being short 😷",
            "You're barely there, don't lose the rhythm! 😬",
            "Margin's thin, but it's still a margin \uD83D\uDCCF",
            "Not failing is still a win, right? \uD83E\uDEE1"
    };

    String[] totalAbove5to10 = {
            "Now that’s what we call buffer 🧮",
            "Cruising just fine. Don’t let go of the wheel 🛞",
            "Riding the safe zone like a pro 🛟",
            "Not too much, not too little — chef’s kiss 👨‍🍳",
            "On cruise control. Stay in your lane 🛣️"
    };

    String[] totalAbove10plus = {
            "Bunk a couple. You earned it 😌",
            "Attendance so high, your prof might give *you* proxy 📋",
            "Chill. You’re running this class 🎓",
            "They better not call roll, just salute you 👏",
            "Even your ghost has 75%"
    };

    String[] totalBelow0to5 = {
            "Careful, the slope is real slippery",
            "So close, yet so bunked...",
            "One more class and you're back on track. Or off a cliff 👀",
            "Almost stable. Just... don’t miss Monday.",
            "Just enough to trigger anxiety 💀"
    };

    String[] totalBelow5to15 = {
            "You’re ghosting your classes fr 👻",
            "Are you even in college?",
            "Bro... do your shoes even know the classroom?",
            "You’ve skipped more than you’ve attended.",
            "Your attendance is a case study in missing"
    };

    String[] totalBelow15to30 = {
            "Not even a time machine can save this one 😅",
            "Consider talking to your prof 🙏",
            "Maybe... drop the subject? Just kidding 😬",
            "Better start attending *everything* now 👀",
            "This is beyond bunking. It’s a lifestyle."
    };

    String[] totalBelow30plus = {
            "\uD83D\uDCAA\uD83C\uDFFB\uD83D\uDCAA\uD83C\uDFFB\uD83D\uDE4F\uD83C\uDFFB",
            "\uD83E\uDD40\uD83E\uDD40\uD83E\uDD40\uD83D\uDE4F\uD83C\uDFFB",
            "\uD83D\uDC94\uD83D\uDC94\uD83D\uDC94",
            "\uD83D\uDC80\uD83D\uDE4F\uD83C\uDFFB"
    };

    String[] subjectAbove = {
            "At least one subject’s proud of you 😌",
            "Nice! This one’s under control ✅",
            "Attendance MVP right here 🏆",
            "No worries. This one's your safe zone.",
            "Your professor in this one actually knows you exist!"
    };

    String[] subjectBelow = {
            "Uh-oh. This one’s falling apart 📉",
            "Save this subject before it saves you... an year!",
            "Lowkey forgot this existed, didn’t you?",
            "Too many bunks, not enough guilt.",
            "Warning: This subject is not okay 🚨"
    };




    Random random = new Random();




    double totalAttendanceDouble = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance_report);

        subjectChoosingSpinner = findViewById(R.id.subjectChoosingSpinner);
        percentChoosingSpinner = findViewById(R.id.percentChoosingSpinner);
        totalDaysTextShower = findViewById(R.id.totalDaysTextShower);
        presentDaysTextShower = findViewById(R.id.presentDaysTextShower);
        attendancePercentCircularIndicator = findViewById(R.id.subjectAttendancePercentCircularIndicator);
        totalAttendancePercentCircularIndicator=findViewById(R.id.totalAttendancePercentCircularIndicator);
        totalAttendanceShowerTextView= findViewById(R.id.totalAttendanceShowerTextView);
        subjectAttendanceShowerTextView= findViewById(R.id.subjectAttendanceShowerTextView);
        subjectAttendancePercentRemarks= findViewById(R.id.subjectAttendancePercentRemarks);
        totalAttendancePercentRemarks= findViewById(R.id.totalAttendancePercentRemarks);
        needhelpbutton= findViewById(R.id.needhelpbutton);
        button3= findViewById(R.id.button3);
        button5= findViewById(R.id.button5);

        SharedPreferences themepref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor themeeditor = themepref.edit();

        int currentnightmode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeChanger();
            }
        });

        subjectTotalDaysTextShower= findViewById(R.id.SubjectTotalDaysTextShower);
        subjectPresentDaysTextShower= findViewById(R.id.SubjectPresentDaysTextShower);



        String[] subjectsAttendanceReportPage = {"Maths", "English", "SST", "JAVA", "A", "B", "C", "D"};
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                subjectsAttendanceReportPage
        );
        subjectAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        subjectChoosingSpinner.setAdapter(subjectAdapter);

        subjectChoosingSpinner.setPopupBackgroundResource(R.drawable.spinnerbg);

        subjectAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        subjectChoosingSpinner.setAdapter(subjectAdapter);


        String[] percents = {"75%", "60%", "50%", "30%", "0%", "90%"};

        ArrayAdapter<String> percentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                percents
        );

        percentAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        percentChoosingSpinner.setAdapter(percentAdapter);
        percentChoosingSpinner.setPopupBackgroundResource(R.drawable.spinnerbg);


        SharedPreferences attendanceReportprefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String namesanitized = attendanceReportprefs.getString("username", "student").toLowerCase().trim().replaceAll("\\s", "");

//        Toast.makeText(this, "name: "+ namesanitized, Toast.LENGTH_SHORT).show();
        //function call to calculate total attendance for all subjects
        calculateTotalAttendance(namesanitized);

        needhelpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(attendanceReportActivity.this, needHelpMainActivity.class));
            }
        });

        subjectChoosingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenSubject = parent.getItemAtPosition(position).toString();
                String chosenSubjectSanitized = chosenSubject.toLowerCase().trim().replaceAll("\\s", "");

                DatabaseReference totalfetcher = FirebaseDatabase.getInstance().getReference("attendance").child(chosenSubjectSanitized).child("TotalDays");
                totalfetcher.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        total = dataSnapshot.getValue(Long.class); //default firebase storage: long. for cases where the result can be null, use Integer and not int. int crashes if null, Integer is a wrapper and handles it., same with long and all. getvalue me L caps because it requres a class not a datatype

                        if (total != null) {
                            totaltobeused = total.toString();
                        } else {
                            totaltobeused = "0";
                        }

                        subjectTotalDaysTextShower.setText(totaltobeused);


//                        totalDaysTextShower.setText(totaltobeused);
                        totaltobeusedint = Integer.parseInt(totaltobeused);

                        DatabaseReference presentfetcher = FirebaseDatabase.getInstance().getReference("attendance").child(chosenSubjectSanitized).child("summary").child(namesanitized).child("present");
                        presentfetcher.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                present = dataSnapshot.getValue(Long.class);

                                if (present != null) {
                                    presenttobeused = present.toString();
                                } else {
                                    presenttobeused = "0";
                                }

                                subjectPresentDaysTextShower.setText(presenttobeused);
//                                presentDaysTextShower.setText(presenttobeused);
                                presenttobeusedint = Integer.parseInt(presenttobeused);

                                double percenttobeused;
                                if (totaltobeusedint == 0) {
                                    percenttobeused = 0;
                                } else {
                                    percenttobeused = ((double) presenttobeusedint / totaltobeusedint) * 100.0;
                                    percenttobeusedRounded = Double.parseDouble(String.format("%.2f", percenttobeused));
                                }

                                String percenttobeuseddoubestring = percenttobeusedRounded + "%";
                                int percenttobeusedint = (int) percenttobeused;

                                attendancePercentCircularIndicator.setProgressCompat(percenttobeusedint, true);
                                String requiredPercentString = percentChoosingSpinner.getSelectedItem().toString().replaceAll("%", "");
                                float requiredPercent = Float.parseFloat(requiredPercentString);
                                float subjectDiff = (float) percenttobeused - requiredPercent;

                                int subjectColor = getAttendanceColor(subjectDiff);
                                attendancePercentCircularIndicator.setIndicatorColor(subjectColor);

                                subjectAttendanceShowerTextView.setText(percenttobeuseddoubestring);

                                if (percenttobeusedint>75){

//                                    Random randomsubject = new Random();             // commented out because ek bar banake same use kro instead of making multiple
                                    int indexsubject = random.nextInt(5);
                                    subjectAttendancePercentRemarks.setText(subjectAbove[indexsubject]);
                                }

                                if (percenttobeusedint<=75){

//                                    Random randomsubject = new Random();
                                    int indexsubject = random.nextInt(5);
                                    subjectAttendancePercentRemarks.setText(subjectBelow[indexsubject]);
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        percentChoosingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                chosenpercentstring = parent.getItemAtPosition(position).toString().replaceAll("%", "");
//                int chosenpercent= Integer.parseInt(chosenpercentstring); //isme string isliye daala kyuki textview ko int dene pe wo pagal hai usko id samajh leta hai isliye pure int ho to bhi tostring karke dena hai
//                presentDaysTextShower.setText(chosenpercentstring);
//                attendancePercentCircularIndicator.setProgressCompat(chosenpercent, true);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


//        String choosenpercentstring= percentChoosingSpinner.getSelectedItem().toString().replaceAll("%", "");
//        int chosenpercent = Integer.parseInt(choosenpercentstring);
//
//        totalDaysTextShower.setText(chosenSubject);
//        presentDaysTextShower.setText(chosenpercent);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //total attendance across all subjects
    private void calculateTotalAttendance(String namesanitized) {

        String[] subjects = {"maths", "english", "sst", "java", "a", "b", "c", "d"};

        int totalSubjects = subjects.length;

        int[] completedFetches = {0};
        int[] totalAllSubjects = {0};
        int[] presentAllSubjects = {0};

        for (int i = 0; i < totalSubjects; i++) {
            String subject = subjects[i];

            DatabaseReference totalRef = FirebaseDatabase.getInstance()
                    .getReference("attendance")
                    .child(subject)
                    .child("TotalDays");

            totalRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot totalSnapshot) {

                    Long totalLong = totalSnapshot.getValue(Long.class);
                    int totalValue;

                    if (totalLong != null) {
                        totalValue = totalLong.intValue();
                    } else {
                        totalValue = 0;
                    }

                    totalAllSubjects[0] += totalValue;

                    DatabaseReference presentRef = FirebaseDatabase.getInstance()
                            .getReference("attendance")
                            .child(subject)
                            .child("summary")
                            .child(namesanitized)
                            .child("present");

                    presentRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot presentSnapshot) {

                            Long presentLong = presentSnapshot.getValue(Long.class);
                            int presentValue;

                            if (presentLong != null) {
                                presentValue = presentLong.intValue();
                            } else {
                                presentValue = 0;
                            }

                            presentAllSubjects[0] += presentValue;

                            completedFetches[0]++;

                            if (completedFetches[0] == totalSubjects) {         // Check if all Firebase fetches for total and present across all subjects are done before calculating and updating total attendance. for loop fires all get calls instantly and fir firebase se random order me aata rehta hai jab sab aa jata hai to maza aa jata hai


                                if (totalAllSubjects[0] == 0) {
                                    totalAttendanceDouble = 0.0;
                                } else {
                                    totalAttendanceDouble = ((double) presentAllSubjects[0] * 100.0) / (double) totalAllSubjects[0];  // Using single-element arrays to allow modification inside inner Firebase callbacks (Java requires variables to be effectively final)
                                    totalAttendanceDoubleRounded = Double.parseDouble(String.format("%.2f", totalAttendanceDouble));
                                    int totaltemp = totalAllSubjects[0];
                                    int totalpresenttemp = presentAllSubjects[0];
                                    String totalpresentfortextview = totalpresenttemp+"";
                                    String totalfortextview = totaltemp + "";
                                    totalDaysTextShower.setText(totalfortextview);
                                    presentDaysTextShower.setText(totalpresentfortextview);
                                }
                                String totalAttendanceDoubleString = totalAttendanceDoubleRounded + "%";
                                int totalAttendanceDoubleInt= (int) totalAttendanceDouble;
                                totalAttendancePercentCircularIndicator.setProgressCompat(totalAttendanceDoubleInt, true);
                                String requiredPercentString = percentChoosingSpinner.getSelectedItem().toString().replaceAll("%", "");
                                float requiredPercent = Float.parseFloat(requiredPercentString);
                                float totalDiff = (float) totalAttendanceDouble - requiredPercent;

                                int totalColor = getAttendanceColor(totalDiff);
                                totalAttendancePercentCircularIndicator.setIndicatorColor(totalColor);

                                if (totaltobeusedint != 0) {
                                    float subjectAttendance = ((float) presenttobeusedint / totaltobeusedint) * 100f;
                                    float subjectDiff = subjectAttendance - requiredPercent;
                                    int subjectColor = getAttendanceColor(subjectDiff);
                                    attendancePercentCircularIndicator.setIndicatorColor(subjectColor);
                                }



                                totalAttendanceShowerTextView.setText(totalAttendanceDoubleString);

                                // Fetch selected value directly from spinner (default is 75%)
                                String defaultPercentString = percentChoosingSpinner.getSelectedItem().toString().replaceAll("%", "");
                                double defaultPercent = Double.parseDouble(defaultPercentString);

                                // Evaluate and show the remarks immediately, even before user touches spinner
                                double diff = totalAttendanceDouble - defaultPercent;

                                if ((int) diff < 0 && (int) diff >= -5) {
                                    totalAttendancePercentRemarks.setText(totalBelow0to5[random.nextInt(5)]);
                                } else if ((int) diff < -5 && (int) diff >= -15) {
                                    totalAttendancePercentRemarks.setText(totalBelow5to15[random.nextInt(5)]);
                                } else if ((int) diff < -15 && (int) diff >= -30) {
                                    totalAttendancePercentRemarks.setText(totalBelow15to30[random.nextInt(5)]);
                                } else if ((int) diff < -30) {
                                    totalAttendancePercentRemarks.setText(totalBelow30plus[random.nextInt(4)]);
                                } else if ((int) diff >= 0 && (int) diff < 5) {
                                    totalAttendancePercentRemarks.setText(totalAbove0to5[random.nextInt(5)]);
                                } else if ((int) diff >= 5 && (int) diff < 10) {
                                    totalAttendancePercentRemarks.setText(totalAbove5to10[random.nextInt(5)]);
                                } else if ((int) diff >= 10) {
                                    totalAttendancePercentRemarks.setText(totalAbove10plus[random.nextInt(5)]);
                                }


                                percentChoosingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        chosenpercentstring = parent.getItemAtPosition(position).toString().trim().replaceAll("%", "");
                                        double chosenPercentDouble = Double.parseDouble(chosenpercentstring);
                                        String requiredPercentString = percentChoosingSpinner.getSelectedItem().toString().replaceAll("%", "");
                                        float requiredPercent = Float.parseFloat(requiredPercentString);
                                        float totalDiff = (float) totalAttendanceDouble - requiredPercent;

                                        int totalColor = getAttendanceColor(totalDiff);
                                        totalAttendancePercentCircularIndicator.setIndicatorColor(totalColor);


                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) < 0 && (int)(totalAttendanceDouble - chosenPercentDouble) >= -5){
//                                            Random random = new Random();
                                            int index = random.nextInt(5);

                                            totalAttendancePercentRemarks.setText(totalBelow0to5[index]);
                                        }

                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) < -5 && (int)(totalAttendanceDouble - chosenPercentDouble) >= -15){
//                                            Random random = new Random();
                                            int index = random.nextInt(5);

                                            totalAttendancePercentRemarks.setText(totalBelow5to15[index]);
                                        }

                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) < -15 && (int)(totalAttendanceDouble - chosenPercentDouble) >= -30){
//                                            Random random = new Random();
                                            int index = random.nextInt(5);

                                            totalAttendancePercentRemarks.setText(totalBelow15to30[index]);
                                        }

                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) < -30){
//                                            Random random = new Random();
                                            int index = random.nextInt(4);

                                            totalAttendancePercentRemarks.setText(totalBelow30plus[index]);
                                        }

                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) >= 0 && (int)(totalAttendanceDouble - chosenPercentDouble) < 5){
//                                            Random random = new Random();
                                            int index = random.nextInt(5);

                                            totalAttendancePercentRemarks.setText(totalAbove0to5[index]);
                                        }

                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) >= 5 && (int)(totalAttendanceDouble - chosenPercentDouble) < 10){
//                                            Random random = new Random();
                                            int index = random.nextInt(5);

                                            totalAttendancePercentRemarks.setText(totalAbove5to10[index]);
                                        }

                                        if ((int)(totalAttendanceDouble - chosenPercentDouble) >= 10){
//                                            Random random = new Random();
                                            int index = random.nextInt(5);

                                            totalAttendancePercentRemarks.setText(totalAbove10plus[index]);
                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });




                            }
                        }
                    });
                }
            });
        }
    }

    public void themeChanger(){
        SharedPreferences themepref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor themeeditor = themepref.edit();

        int currentnightmode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                if (currentnightmode == Configuration.UI_MODE_NIGHT_YES){
                    setDefaultNightMode(MODE_NIGHT_NO);

                    themeeditor.putInt("currentTheme", Configuration.UI_MODE_NIGHT_NO);
                }

                else {
                    setDefaultNightMode(MODE_NIGHT_YES);
                    themeeditor.putInt("currentTheme", Configuration.UI_MODE_NIGHT_YES);
                }

                themeeditor.apply();
                recreate();
    }

    public void themePreserver(){
        SharedPreferences themepref = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        int themeSet = themepref.getInt("currentTheme", getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK);

//        int currentnightmode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                if (themeSet == Configuration.UI_MODE_NIGHT_YES){
                    setDefaultNightMode(MODE_NIGHT_YES);
                }

                else {
                    setDefaultNightMode(MODE_NIGHT_NO);
                }



    }

    private int getAttendanceColor(float diffPercent) {
        if (diffPercent <= -10) {
            return ContextCompat.getColor(this, R.color.barRed);
        } else if (diffPercent <= -5) {
            return ContextCompat.getColor(this, R.color.barYellowish_red);
        } else if (diffPercent < 5) {
            return ContextCompat.getColor(this, R.color.barYellow);
        } else if (diffPercent < 10) {
            return ContextCompat.getColor(this, R.color.barYellowish_green);
        } else {
            return ContextCompat.getColor(this, R.color.barGreen);
        }
    }


}




