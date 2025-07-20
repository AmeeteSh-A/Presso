package com.example.attendanceappdraft0;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.attendanceappdraft0.utils.BaseActivity;
import com.example.attendanceappdraft0.utils.UIUtils;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProfUIMain extends BaseActivity {
    TextView profwelcometext, subjecttext;
    ToggleButton togglebutton;
    Button button4, csvexportbutton, button3, button5;
    HashMap<String, TextView> statusViews = new HashMap<>();
    HashMap<String, ToggleButton> toggleButtons = new HashMap<>();
    Handler handler = new Handler(Looper.getMainLooper());
    Set<String> updatedToday = new HashSet<>();
    private static final int EXPORT_CSV_REQUEST_CODE = 1001;
    boolean suppressToggleChange = false;

    private void exportCsvUsingSAF() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "Attendance.csv");
        startActivityForResult(intent, EXPORT_CSV_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prof_uimain);



        subjecttext = findViewById(R.id.subjecttext);
        profwelcometext = findViewById(R.id.profwelcometext);
        togglebutton = findViewById(R.id.togglebutton);
        button4 = findViewById(R.id.button4);
        csvexportbutton = findViewById(R.id.csvexporterbutton);
        UIUtils.applyShrinkEffect(button4);
        UIUtils.applyShrinkEffect(csvexportbutton);
        UIUtils.applyShrinkEffect(togglebutton);

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


        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String namedisplay = prefs.getString("profName", "Professor");
        String subjectName = prefs.getString("profSubject", "unknown_subject").trim().toLowerCase();

        button4.setOnClickListener(v -> startActivity(new Intent(ProfUIMain.this, needHelpMainActivity.class)));

        subjecttext.setText("Taking Attendance for: " + subjectName);
        profwelcometext.setText("Welcome " + namedisplay + "!");

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("attendance").child(subjectName);
        List<String> studentNames = Arrays.asList("Student 1", "Student 2", "Student 3", "Student 4", "Student 5", "Student 6", "Student 7", "Student 8", "Student 9", "Student 10");
        LinearLayout container = findViewById(R.id.studentlistcontainer);

        handleNewDayAttendanceReset(subjectName, studentNames);

        createCsvIfNotExist(this, studentNames);

        csvexportbutton.setOnClickListener(v -> exportCsvUsingSAF());

        togglebutton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(ProfUIMain.this);
            handler.removeCallbacksAndMessages(null);

            if (isChecked) {
                checkAndUpdateTotalDays(subjectName);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    togglebutton.setChecked(false);
                    return;
                }

                fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();

                                dbRef.child("profLat").setValue(lat);
                                dbRef.child("profLong").setValue(lon);
                                dbRef.child("ct").setValue("yes");

                                Toast.makeText(this, "✅ Attendance started for " + subjectName, Toast.LENGTH_SHORT).show();

                                dbRef.child("students").get().addOnSuccessListener(snapshot -> {
                                    String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                    for (String student : studentNames) {
                                        String key = student.trim().toLowerCase().replaceAll("\\s+", "");
                                        String status = snapshot.child(key).getValue(String.class);
                                        boolean wasPresent = "P".equalsIgnoreCase(status);

                                        if (!updatedToday.contains(student + today)) {
                                            updateCsv(this, subjectName, student, wasPresent);
                                            updatedToday.add(student + today);

                                            DatabaseReference summaryRef = dbRef.child("summary").child(key);
                                            summaryRef.child("total").get().addOnSuccessListener(totalSnap -> {
                                                long total = totalSnap.exists() ? totalSnap.getValue(Long.class) : 0;
                                                long newTotal = total + 1;
                                                summaryRef.child("total").setValue(newTotal);

                                                summaryRef.child("present").get().addOnSuccessListener(pSnap -> {
                                                    long present = pSnap.exists() ? pSnap.getValue(Long.class) : 0;
                                                    long newPresent = wasPresent ? present + 1 : present;
                                                    summaryRef.child("present").setValue(newPresent);

                                                    long percent = newTotal == 0 ? 0 : (newPresent * 100) / newTotal;
                                                    summaryRef.child("percentage").setValue(percent);
                                                });
                                            });
                                        }
                                    }
                                });

                                new Handler().postDelayed(() -> {
                                    dbRef.child("ct").setValue("no");
                                    Toast.makeText(this, "🕒 Attendance closed", Toast.LENGTH_SHORT).show();

                                    dbRef.child("students").get().addOnSuccessListener(snapshot -> {
                                        for (String student : studentNames) {
                                            String key = student.trim().toLowerCase().replaceAll("\\s+", "");
                                            if (!snapshot.hasChild(key)) {
                                                dbRef.child("students").child(key).setValue("A");
                                                updateCsv(this, subjectName, student, false);

                                                DatabaseReference summaryRef = dbRef.child("summary").child(key);
                                                summaryRef.child("total").get().addOnSuccessListener(totalSnap -> {
                                                    long total = totalSnap.exists() ? totalSnap.getValue(Long.class) : 0;
                                                    long newTotal = total + 1;
                                                    summaryRef.child("total").setValue(newTotal);

                                                    summaryRef.child("present").get().addOnSuccessListener(pSnap -> {
                                                        long present = pSnap.exists() ? pSnap.getValue(Long.class) : 0;
                                                        long percent = (present * 100) / newTotal;
                                                        summaryRef.child("percentage").setValue(percent);
                                                    });
                                                });
                                            }
                                        }
                                    });
                                }, 90 * 1000);
                            } else {
                                Toast.makeText(this, "❌ Couldn't fetch location", Toast.LENGTH_SHORT).show();
                                togglebutton.setChecked(false);
                            }
                        });

                new Handler().postDelayed(() -> togglebutton.setChecked(false), 90 * 1000);
            } else {
                dbRef.child("ct").setValue("no");
                Toast.makeText(this, "🚫 Attendance turned OFF", Toast.LENGTH_SHORT).show();
            }
        });

        for (String studentName : studentNames) {
            View view = LayoutInflater.from(this).inflate(R.layout.student_row_item, container, false);

            TextView nameTextView = view.findViewById(R.id.nameTextView);
            TextView statusTextView = view.findViewById(R.id.statusTextView);
            ToggleButton overrideToggle = view.findViewById(R.id.profoverridetoggle);

            nameTextView.setText(studentName);
            statusTextView.setText("");

            String idKey = studentName.trim().toLowerCase().replaceAll("\\s+", "");
            statusViews.put(idKey, statusTextView);
            toggleButtons.put(idKey, overrideToggle);

            dbRef.child("students").child(idKey).get().addOnSuccessListener(snapshot -> {
                String status = snapshot.getValue(String.class);
                statusTextView.setText(status);

                suppressToggleChange = true;
                if ("A".equals(status)) {
                    overrideToggle.setChecked(true);
                    overrideToggle.setText("Mark Present");
                } else {
                    overrideToggle.setChecked(false);
                    overrideToggle.setText("Mark Absent");
                }
                suppressToggleChange = false;
            });

            overrideToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (suppressToggleChange) return;

                String newStatus = isChecked ? "A" : "P";
                dbRef.child("students").child(idKey).setValue(newStatus);
                statusTextView.setText(newStatus);

                updateCsvCorrection(this, subjectName, studentName, isChecked);

                if (isChecked) {
                    overrideToggle.setText("Mark Present");
                } else {
                    overrideToggle.setText("Mark Absent");
                }

                DatabaseReference summaryRef = dbRef.child("summary").child(idKey);
                summaryRef.child("total").get().addOnSuccessListener(totalSnap -> {
                    long total = totalSnap.exists() ? totalSnap.getValue(Long.class) : 0;

                    summaryRef.child("present").get().addOnSuccessListener(presentSnap -> {
                        long present = presentSnap.exists() ? presentSnap.getValue(Long.class) : 0;
                        long updatedPresent = isChecked ? Math.max(0, present - 1) : present + 1;

                        summaryRef.child("present").setValue(updatedPresent);
                        long newPercent = (total == 0) ? 0 : (updatedPresent * 100) / total;
                        summaryRef.child("percentage").setValue(newPercent);
                    });
                });
            });

            container.addView(view);
        }

        dbRef.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studentSnap : snapshot.getChildren()) {
                    String rawName = studentSnap.getKey();
                    String status = studentSnap.getValue(String.class);
                    String sanitizedKey = rawName.trim().toLowerCase().replaceAll("\\s+", "");

                    if (statusViews.containsKey(sanitizedKey)) {
                        statusViews.get(sanitizedKey).setText(status);
                    }

                    if (toggleButtons.containsKey(sanitizedKey)) {
                        ToggleButton overrideToggle = toggleButtons.get(sanitizedKey);

                        suppressToggleChange = true;
                        if ("A".equals(status)) {
                            overrideToggle.setChecked(true);
                            overrideToggle.setText("Mark Present");
                        } else {
                            overrideToggle.setChecked(false);
                            overrideToggle.setText("Mark Absent");
                        }
                        suppressToggleChange = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfUIMain.this, "⚠️ Failed to fetch updates", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // CSV update methods (unchanged)
    // checkAndUpdateTotalDays(), updateCsv(), updateCsvCorrection(), getSubjectIndex(), createCsvIfNotExist()
    // -- keep as you already have them in full


public void checkAndUpdateTotalDays(String subjectName) {
        DatabaseReference totalDaysRef = FirebaseDatabase.getInstance().getReference("attendance").child(subjectName).child("TotalDays");
        totalDaysRef.get().addOnSuccessListener(snapshot -> {
            Long current = snapshot.getValue(Long.class);
            if (current == null) current = 0L;

            SharedPreferences dates = getSharedPreferences("datespref", MODE_PRIVATE);
            SharedPreferences.Editor dateseditor = dates.edit();

            String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String lastSaved = dates.getString("today", "none");

            if (!today.equals(lastSaved)) {
                totalDaysRef.setValue(current + 1);
                dateseditor.putString("today", today);
                dateseditor.apply();
                updatedToday.clear(); // clear hash set when day changes
            }
        });
    }

    private void updateCsv(Context context, String subject, String studentName, boolean wasPresent) {
        File file = new File(context.getFilesDir(), "Attendance.csv");
        List<String[]> updatedRows = new ArrayList<>();
        String[] header = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int lineIndex = 0;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (lineIndex == 0) {
                    header = row;
                    updatedRows.add(row);
                } else if (row[0].equalsIgnoreCase(studentName)) {
                    int subIndex = getSubjectIndex(subject);
                    int total = Integer.parseInt(row[subIndex]);
                    int present = Integer.parseInt(row[subIndex + 1]);

                    total++;
                    if (wasPresent) present++;

                    row[subIndex] = String.valueOf(total);
                    row[subIndex + 1] = String.valueOf(present);
                    row[subIndex + 2] = total == 0 ? "0" : String.valueOf((present * 100) / total); // ✅ safe %

                    int totalIndex = header.length - 3;
                    int totalTotal = Integer.parseInt(row[totalIndex]);
                    int totalP = Integer.parseInt(row[totalIndex + 1]);

                    row[totalIndex] = String.valueOf(totalTotal + 1);
                    row[totalIndex + 1] = String.valueOf(totalP + (wasPresent ? 1 : 0));
                    row[totalIndex + 2] = totalTotal == 0 ? "0" : String.valueOf((Integer.parseInt(row[totalIndex + 1]) * 100) / Integer.parseInt(row[totalIndex]));

                    updatedRows.add(row);
                } else {
                    updatedRows.add(row);
                }
                lineIndex++;
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed reading CSV", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileWriter writer = new FileWriter(file, false);
            for (String[] row : updatedRows) {
                writer.append(TextUtils.join(",", row)).append("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to write CSV", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCsvCorrection(Context context, String subject, String studentName, boolean nowAbsent) {
        File file = new File(context.getFilesDir(), "Attendance.csv");
        List<String[]> updatedRows = new ArrayList<>();
        String[] header = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int lineIndex = 0;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (lineIndex == 0) {
                    header = row;
                    updatedRows.add(row);
                } else if (row[0].equalsIgnoreCase(studentName)) {
                    int subIndex = getSubjectIndex(subject);
                    int total = Integer.parseInt(row[subIndex]);
                    int present = Integer.parseInt(row[subIndex + 1]);

                    if (nowAbsent && present > 0) {
                        present--;
                    } else if (!nowAbsent) {
                        present++;
                    }

                    row[subIndex + 1] = String.valueOf(present);
                    row[subIndex + 2] = total == 0 ? "0" : String.valueOf((present * 100) / total);

                    int totalIndex = header.length - 3;
                    int totalTotal = Integer.parseInt(row[totalIndex]);
                    int totalP = Integer.parseInt(row[totalIndex + 1]);

                    if (nowAbsent && totalP > 0) totalP--;
                    else if (!nowAbsent) totalP++;

                    row[totalIndex + 1] = String.valueOf(totalP);
                    row[totalIndex + 2] = totalTotal == 0 ? "0" : String.valueOf((totalP * 100) / totalTotal);

                    updatedRows.add(row);
                } else {
                    updatedRows.add(row);
                }
                lineIndex++;
            }
            reader.close();

            FileWriter writer = new FileWriter(file, false);
            for (String[] row : updatedRows) {
                writer.append(TextUtils.join(",", row)).append("\n");
            }
            writer.flush();
            writer.close();

//            Toast.makeText(context, "✔ Attendance corrected in CSV", Toast.LENGTH_SHORT).show(); //Added
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Correction failed", Toast.LENGTH_SHORT).show();
        }
    }

    private int getSubjectIndex(String subject) {
        switch (subject.toLowerCase()) {
            case "english": return 1;
            case "maths": return 4;
            case "java": return 7;
            case "sst": return 10;
            case "a": return 13;
            case "b": return 16;
            case "c": return 19;
            case "d": return 22;
            default: return -1;
        }
    }

    private void createCsvIfNotExist(Context context, List<String> studentNames) {
        File file = new File(context.getFilesDir(), "Attendance.csv");

        if (file.exists()) return;

        try {
            FileWriter writer = new FileWriter(file);
            String[] header = {"Student's Name", "English: Total", "Present", "Attendance %", "Maths: Total", "Present", "Attendance %", "Java: Total", "Present", "Attendance %", "SST: Total", "Present", "Attendance %", "A: Total", "Present", "Attendance %", "B: Total", "Present", "Attendance %", "C: Total", "Present", "Attendance %", "D: Total", "Present", "Attendance %", "Total Classes", "Total Present", "Total Attendance %"};
            writer.append(TextUtils.join(",", header)).append("\n");

            for (String name : studentNames) {
                List<String> row = new ArrayList<>();
                row.add(name);
                for (int i = 0; i < 8 * 3 + 3; i++) row.add("0");
                writer.append(TextUtils.join(",", row)).append("\n");
            }

            writer.flush();
            writer.close();
            Toast.makeText(context, "CSV created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to create CSV", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXPORT_CSV_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                writeCsvToUri(uri);
            }
        }
    }

    private void writeCsvToUri(Uri uri) {
        File file = new File(getFilesDir(), "Attendance.csv");

        if (!file.exists()) {
            Toast.makeText(this, "❌ No CSV found to export", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileInputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = getContentResolver().openOutputStream(uri);

            if (outputStream == null) {
                Toast.makeText(this, "❌ Couldn't open output stream", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "✅ Exported CSV successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "❌ Failed to export: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleNewDayAttendanceReset(String subjectName, List<String> studentNames) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("attendance").child(subjectName);

        SharedPreferences prefs = getSharedPreferences("profDayResetPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String lastSavedDate = prefs.getString("lastDate", "");

        dbRef.child("ct").get().addOnSuccessListener(ctSnap -> {
            String ct = ctSnap.exists() ? ctSnap.getValue(String.class) : "no";

            if (!today.equals(lastSavedDate) && !"yes".equals(ct)) {
                for (String student : studentNames) {
                    String key = student.trim().toLowerCase().replaceAll("\\s+", "");
                    dbRef.child("students").child(key).setValue("A");

                    updateCsv(this, subjectName, student, false);

                    DatabaseReference summaryRef = dbRef.child("summary").child(key);
                    summaryRef.child("total").get().addOnSuccessListener(totalSnap -> {
                        long total = totalSnap.exists() ? totalSnap.getValue(Long.class) : 0;
                        long newTotal = total + 1;
                        summaryRef.child("total").setValue(newTotal);

                        summaryRef.child("present").get().addOnSuccessListener(pSnap -> {
                            long present = pSnap.exists() ? pSnap.getValue(Long.class) : 0;
                            long percent = (present * 100) / newTotal;
                            summaryRef.child("percentage").setValue(percent);
                        });
                    });
                }

                editor.putString("lastDate", today);
                editor.apply();
                Toast.makeText(this, "✅ Marked all students Absent for new day", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
