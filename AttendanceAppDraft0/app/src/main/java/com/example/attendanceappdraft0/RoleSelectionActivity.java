package com.example.attendanceappdraft0;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;
import com.example.attendanceappdraft0.utils.UIUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class RoleSelectionActivity extends BaseActivity {
    Spinner spinner, professorsubjectchooserspinner, studentbranchchooserspinner;
    EditText professornameedittext, studentnameedittext, studentrollnumberedittext, professorpasscodeedittext;
    Button button, button4;
    TextView branchtextview, subjecttextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rollselection);

        // ❌ EMULATOR CHECK
        if (isProbablyAnEmulator()) {
            Toast.makeText(this, "❌ Emulator use is not allowed", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 📍 LOCATION PERMISSION CHECK
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            checkForMockLocation();
        }

        // 🔗 INIT
        professornameedittext = findViewById(R.id.professornameedittext);
        professorsubjectchooserspinner = findViewById(R.id.professorsubjectchooserspinner);
        professorpasscodeedittext = findViewById(R.id.professorpasscodeedittext);

        studentnameedittext = findViewById(R.id.studentnameedittext);
        studentrollnumberedittext = findViewById(R.id.studentrollnumberedittext);
        studentbranchchooserspinner = findViewById(R.id.studentbranchchooserspinner);
        branchtextview = findViewById(R.id.branchtextview);
        subjecttextview = findViewById(R.id.subjecttextview);
        button4 = findViewById(R.id.button4);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.button);

        UIUtils.applyShrinkEffect(button);
        UIUtils.applyShrinkEffect(button4);

        button4.setOnClickListener(v ->
                startActivity(new Intent(RoleSelectionActivity.this, needHelpMainActivity.class)));

        // 🔧 SETUP SPINNERS
        String[] subjects = {"Maths", "English", "SST", "JAVA", "A", "B", "C", "D"};
        professorsubjectchooserspinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinnerlayout, subjects));
        professorsubjectchooserspinner.setPopupBackgroundResource(R.drawable.spinnerbg);

        String[] branches = {"B.Tech", "B.Tech + M.Tech"};
        studentbranchchooserspinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinnerlayout, branches));
        studentbranchchooserspinner.setPopupBackgroundResource(R.drawable.spinnerbg);

        String[] roles = {"Professor", "Student"};
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinnerlayout, roles));
        spinner.setPopupBackgroundResource(R.drawable.spinnerbg);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isProfessor = spinner.getSelectedItem().toString().equals("Professor");
                professornameedittext.setVisibility(isProfessor ? View.VISIBLE : View.GONE);
                professorsubjectchooserspinner.setVisibility(isProfessor ? View.VISIBLE : View.GONE);
                professorpasscodeedittext.setVisibility(isProfessor ? View.VISIBLE : View.GONE);
                subjecttextview.setVisibility(isProfessor ? View.VISIBLE : View.GONE);

                studentnameedittext.setVisibility(!isProfessor ? View.VISIBLE : View.GONE);
                studentrollnumberedittext.setVisibility(!isProfessor ? View.VISIBLE : View.GONE);
                studentbranchchooserspinner.setVisibility(!isProfessor ? View.VISIBLE : View.GONE);
                branchtextview.setVisibility(!isProfessor ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        button.setOnClickListener(v -> {
            String role = spinner.getSelectedItem().toString();
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            if (role.equals("Professor")) {
                String profName = professornameedittext.getText().toString().trim();
                String subject = professorsubjectchooserspinner.getSelectedItem().toString();
                String code = professorpasscodeedittext.getText().toString().trim();

                if (profName.isEmpty() || code.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    if (Integer.parseInt(code) != 0000) {
                        Toast.makeText(this, "Wrong code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid code format", Toast.LENGTH_SHORT).show();
                    return;
                }

                editor.putString("role", "professor");
                editor.putString("profName", profName);
                editor.putString("profSubject", subject);
                editor.apply();

                startActivity(new Intent(this, ProfUIMain.class));
                finish();

            } else {
                String studentName = studentnameedittext.getText().toString().trim();
                String rollStr = studentrollnumberedittext.getText().toString().trim();
                String branch = studentbranchchooserspinner.getSelectedItem().toString();

                if (studentName.isEmpty() || rollStr.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int roll;
                try {
                    roll = Integer.parseInt(rollStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid roll number", Toast.LENGTH_SHORT).show();
                    return;
                }

                editor.putString("role", "student");
                editor.putString("username", studentName);
                editor.putInt("roll", roll);
                editor.putString("branch", branch);

                // Firebase-friendly key
                String firebaseKey = studentName.toLowerCase().replaceAll("\\s+", "");
                editor.putString("studentsFirebaseKey", firebaseKey);
                editor.apply();

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 🧠 Detect Emulators
    private boolean isProbablyAnEmulator() {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("emulator")
                || Build.MODEL.contains("Emulator")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk".equals(Build.PRODUCT)
                || Build.PRODUCT.contains("sdk_gphone"));
    }

    // 🚨 Spoof Location Check
    private void checkForMockLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null && location.isFromMockProvider()) {
                        Toast.makeText(this, "❌ Spoofed location detected!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
