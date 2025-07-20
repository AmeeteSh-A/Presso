package com.example.attendanceappdraft0;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.ui.attendanceReportActivity;
import com.example.attendanceappdraft0.utils.BaseActivity;
import com.example.attendanceappdraft0.utils.UIUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity {

    private static final int LOCATION_REQUEST_CODE = 102;
    private String pendingSubject = null;
    private String pendingStudentName = null;
    FusedLocationProviderClient fusedLocationClient;

    Button button2, button4, maths, english, java, sst, a,b,c,d, button3, button5;
    Button mathsrefresh, englishrefresh, javarefresh, sstrefresh, arefresh,brefresh,crefresh,drefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        mathsrefresh= findViewById(R.id.mathsrefresh);
        englishrefresh= findViewById(R.id.englishrefresh);
        javarefresh= findViewById(R.id.javarefresh);
        sstrefresh= findViewById(R.id.sstrefresh);
        arefresh= findViewById(R.id.arefresh);
        brefresh= findViewById(R.id.brefresh);
        crefresh= findViewById(R.id.crefresh);
        drefresh= findViewById(R.id.drefresh);


        button2=findViewById(R.id.button2);
        button4=findViewById(R.id.button4);
        maths= findViewById(R.id.maths);
        english= findViewById(R.id.english);
        java= findViewById(R.id.java);
        sst= findViewById(R.id.sst);
        a= findViewById(R.id.a);
        b= findViewById(R.id.b);
        c= findViewById(R.id.c);
        d= findViewById(R.id.d);

        button3= findViewById(R.id.button3);
        button5= findViewById(R.id.button5);

        UIUtils.applyShrinkEffect(maths);
        UIUtils.applyShrinkEffect(english);
        UIUtils.applyShrinkEffect(java);
        UIUtils.applyShrinkEffect(sst);
        UIUtils.applyShrinkEffect(a);
        UIUtils.applyShrinkEffect(b);
        UIUtils.applyShrinkEffect(c);
        UIUtils.applyShrinkEffect(d);
        UIUtils.applyShrinkEffect(button2);
        UIUtils.applyShrinkEffect(button4);

        UIUtils.applyRotateEffect(mathsrefresh);
        UIUtils.applyRotateEffect(englishrefresh);
        UIUtils.applyRotateEffect(javarefresh);
        UIUtils.applyRotateEffect(sstrefresh);
        UIUtils.applyRotateEffect(arefresh);
        UIUtils.applyRotateEffect(brefresh);
        UIUtils.applyRotateEffect(crefresh);
        UIUtils.applyRotateEffect(drefresh);

        setupRefreshListeners();



        TextView studentuiactivitywelcometext = findViewById(R.id.studentuiactivitywelcometext);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String studentname = prefs.getString("username", "Student");

        studentuiactivitywelcometext.setText("Welcome " + studentname + "!");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, attendanceReportActivity.class));
            }
        });


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

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, needHelpMainActivity.class));
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

    }

    public void markAttendance(View view) {
        String subject = view.getResources().getResourceEntryName(view.getId()).trim().toLowerCase();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String studentName = prefs.getString("username", "defaultStudent").trim().toLowerCase();

        pendingSubject = subject;
        pendingStudentName = studentName;

//        Toast.makeText(this, "Clicked subject: " + subject, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        checkAndMarkAttendance(subject, studentName);
    }

    private void checkAndMarkAttendance(String subject, String studentName) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("attendance").child(subject);

        dbRef.child("ct").get().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                if (snapshot.exists() && "yes".equals(snapshot.getValue(String.class))) {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        double studentLat = location.getLatitude();
                                        double studentLong = location.getLongitude();

                                        dbRef.child("profLat").get().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot latSnap) {
                                                dbRef.child("profLong").get().addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DataSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DataSnapshot longSnap) {
                                                        if (latSnap.exists() && longSnap.exists()) {
                                                            double profLat = latSnap.getValue(Double.class);
                                                            double profLong = longSnap.getValue(Double.class);

                                                            float[] result = new float[1];
                                                            Location.distanceBetween(profLat, profLong, studentLat, studentLong, result);

                                                            if (result[0] <= 50.0) {
                                                                String sanitizedStudentKey = studentName.trim().toLowerCase().replaceAll("\\s+", "");
                                                                dbRef.child("students").child(sanitizedStudentKey).setValue("P")
                                                                        .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(MainActivity.this, "✅ Attendance marked!", Toast.LENGTH_SHORT).show();
                                                                                // Status will update through the Firebase listener
                                                                                updateStatus(subject);
                                                                            }
                                                                        });
                                                            } else {
                                                                Toast.makeText(MainActivity.this, "❌ Too far from class!\nDistance: " + result[0] + " meters", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "⚠️ Professor location not available yet.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(MainActivity.this, "❌ Couldn't fetch your location.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "⚠️ Attendance is not active for " + subject, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updateStatus(String subject) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String studentName = prefs.getString("username", "defaultStudent").trim().toLowerCase();
        String sanitizedStudentKey = studentName.replaceAll("\\s+", "");

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("attendance").child(subject);
        dbRef.child("students").child(sanitizedStudentKey).get().addOnSuccessListener(snapshot -> {
            String status = snapshot.exists() ? snapshot.getValue(String.class) : "A";
            int ID = getResources().getIdentifier(subject + "status", "id", getPackageName());
            TextView statustext = findViewById(ID);
            if (statustext != null) {
                statustext.setText("P".equals(status) ? "Status: Present" : "Status: Absent");
            }
        });
    }


    private void setupRefreshListeners() {
        mathsrefresh.setOnClickListener(v -> updateStatus("maths"));
        englishrefresh.setOnClickListener(v -> updateStatus("english"));
        javarefresh.setOnClickListener(v -> updateStatus("java"));
        sstrefresh.setOnClickListener(v -> updateStatus("sst"));
        arefresh.setOnClickListener(v -> updateStatus("a"));
        brefresh.setOnClickListener(v -> updateStatus("b"));
        crefresh.setOnClickListener(v -> updateStatus("c"));
        drefresh.setOnClickListener(v -> updateStatus("d"));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingSubject != null && pendingStudentName != null) {
                    checkAndMarkAttendance(pendingSubject, pendingStudentName);
                }
            } else {
                Toast.makeText(this, "⚠️ Location permission is required to mark attendance.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
